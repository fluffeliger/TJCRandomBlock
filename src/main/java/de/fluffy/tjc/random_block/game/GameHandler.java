package de.fluffy.tjc.random_block.game;

import de.fluffy.tjc.random_block.RandomBlockPlugin;
import de.fluffy.tjc.random_block.data.config.ConfigFile;
import de.fluffy.tjc.random_block.data.config.cinterface.interfaces.ConfigGameInterface;
import de.fluffy.tjc.random_block.data.config.path.ConfigPaths;
import de.fluffy.tjc.random_block.game.message.MessageTemplate;
import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.game.player.GamePlayerManager;
import de.fluffy.tjc.random_block.game.player.PlayerState;
import de.fluffy.tjc.random_block.generation.world.CustomWorld;
import de.fluffy.tjc.random_block.generation.world.WorldManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameHandler {

    private static GameHandler gameHandlerInstance = null;
    private GameState gameState = GameState.WAITING;
    private GameState lastUpdateGameState = null;
    private final ConfigGameInterface configGameInterface;
    private BukkitTask countDownTask = null;
    private BukkitTask waitingTask = null;
    private final ArrayList<Material> randomMaterials = new ArrayList<>();
    private int currentLobbyMaxPlayers = 0;

    public GameHandler() {
        gameHandlerInstance = this;

        CustomWorld voidCustomWorld = WorldManager.getInstance().getWorld("void_copy");
        assert voidCustomWorld != null;
        World voidWorld = voidCustomWorld.getWorld();
        assert voidWorld != null;

        for (Material material : Material.values()) {
            if (!material.isItem()) continue;
            if (material.toString().contains("command")) continue;
            if (material.isAir()) continue;
            if (!material.isEnabledByFeature(voidWorld)) continue;
            randomMaterials.add(material);
        }

        ConfigFile config = new ConfigFile(Path.of(
                RandomBlockPlugin.getInstance().getDataFolder().getPath(), "config.yml"
        ).toFile());
        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(ConfigPaths.CONFIG_COUNTDOWN.getPath(), ConfigPaths.CONFIG_COUNTDOWN.getDefault());
        defaults.put(ConfigPaths.CONFIG_MIN_PLAYERS.getPath(), ConfigPaths.CONFIG_MIN_PLAYERS.getDefault());
        config.setup(defaults);
        configGameInterface = new ConfigGameInterface(config);
    }

    public void playerUpdate() {
        GamePlayerManager gamePlayerManager = GamePlayerManager.getInstance();
        HashSet<GamePlayer> lobbyPlayers = gamePlayerManager.getLobbyPlayers();
        GameState oldGameState = gameState;
        switch (getGameState()) {
            case WAITING -> {
                if (lobbyPlayers.size() >= configGameInterface.getMinPlayers()) gameState = GameState.STARTING;
            }
            case STARTING -> {
                if (lobbyPlayers.size() < configGameInterface.getMinPlayers()) gameState = GameState.WAITING;
            }
            case RUNNING -> {
                HashSet<GamePlayer> alivePlayers = gamePlayerManager.getAlivePlayers();
                if (alivePlayers.size() <= 1) {
                    gameState = GameState.ENDING;
                }
            }
        }
        if (oldGameState != gameState) update();
    }

    @NotNull private World prepareGameWorld() {
        CustomWorld customWorld = WorldManager.getInstance().getWorld("void_copy");
        assert customWorld != null;
        World world = customWorld.getWorld();
        assert world != null;
        world.setTime(1600);
        world.setThundering(false);
        return world;
    }

    @NotNull private HashMap<GamePlayer, Location> prepare() {
        HashSet<GamePlayer> lobbyPlayers = GamePlayerManager.getInstance().getLobbyPlayers();
        int playerCount = lobbyPlayers.size();
        Iterator<GamePlayer> gamePlayers = lobbyPlayers.iterator();
        double radius = playerCount * 15 / (2 * Math.PI);
        double delta_angle = 2 * Math.PI / playerCount;
        World world = prepareGameWorld();
        Location originLocation = new Location(world, 0, 64, 0);
        HashMap<GamePlayer, Location> players = new HashMap<>();
        for (int i = 0; i < playerCount; i++) {
            double angle = i * delta_angle;
            Block block = new Location(world, radius * Math.cos(angle) + .5F, 64, radius * Math.sin(angle) + .5F).getBlock();
            block.setType(Material.BEDROCK);

            GamePlayer gamePlayer = gamePlayers.next();
            Player player = gamePlayer.getPlayer();
            Vector direction = originLocation.toVector().subtract(player.getLocation().toVector()).normalize();
            float yaw = (float) Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
            float pitch = (float) Math.toDegrees(Math.asin(direction.getY()));

            players.put(gamePlayer, new Location(
                    world,
                    block.getX() + .5F,
                    block.getY() + 1.5F,
                    block.getZ() + .5F,
                    yaw,
                    pitch
            ));
        }
        return players;
    }

    private void removeCountDownTask() {
        if (countDownTask != null) {
            countDownTask.cancel();
            countDownTask = null;
        }
    }

    private void removeWaitingTask() {
        if (waitingTask != null) {
            waitingTask.cancel();
            waitingTask = null;
        }
        Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(
                MiniMessage.miniMessage().deserialize("")
        ));
    }

    public void update() {
        if (lastUpdateGameState == gameState) return;
        GamePlayerManager gamePlayerManager = GamePlayerManager.getInstance();
        switch (getGameState()) {
            case WAITING -> {
                removeCountDownTask();
                removeWaitingTask();
                waitingTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            int needing = configGameInterface.getMinPlayers() - gamePlayerManager.getLobbyPlayers().size();
                            Component component;
                            if (needing == 0) component = MessageTemplate.COUNTDOWN_STARTING_NOW.getComponent();
                            else if (needing == 1) component = MessageTemplate.LOBBY_WAITING_FOR_ONE.getComponent();
                            else component = MessageTemplate.LOBBY_WAITING_FOR.getComponent(needing);
                            player.sendActionBar(component);
                        });
                    }
                }.runTaskTimer(RandomBlockPlugin.getInstance(), 0, 20);
                playerUpdate();
            }
            case STARTING -> {
                removeCountDownTask();
                removeWaitingTask();
                AtomicInteger seconds = new AtomicInteger(configGameInterface.getCountdownSeconds()+1);
                countDownTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        int left = seconds.decrementAndGet();
                        if (left == 0) {
                            removeCountDownTask();
                            MessageTemplate.COUNTDOWN_STARTING_NOW.broadcast();
                            gameState = GameState.SETUP_RUNNING;
                            update();
                            return;
                        }
                        if (left <= 3 || left % 5 == 0) {
                            if (left == 1) MessageTemplate.COUNTDOWN_STARTING_IN_ONE.broadcast();
                            else MessageTemplate.COUNTDOWN_STARTING_IN.broadcast(seconds.get());
                        }
                    }
                }.runTaskTimer(RandomBlockPlugin.getInstance(), 0, 20);
            }
            case SETUP_RUNNING -> {
                HashMap<GamePlayer, Location> players = prepare();
                players.forEach((gamePlayer, location) -> {
                    Player player = gamePlayer.getPlayer();
                    player.teleport(location);
                    gamePlayer.resetTicks();
                    gamePlayer.setState(PlayerState.ALIVE);
                    currentLobbyMaxPlayers = players.size();
                    gameState = GameState.RUNNING;
                });
            }
            case RUNNING -> {
                gamePlayerManager.getAlivePlayers().forEach(player -> {
                    player.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(
                            "<gray>Changed GameState to <aqua>" + gameState
                    ));
                });
            }
            case ENDING -> {
                GamePlayer winner = gamePlayerManager.getAlivePlayers().size() == 1 ? gamePlayerManager.getAlivePlayers().iterator().next() : null;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        gameState = GameState.WAITING;
                        gamePlayerManager.moveAllToLobby();
                        RandomBlockPlugin.getInstance().resetVoidWorld();
                        update();
                    }
                }.runTaskLater(RandomBlockPlugin.getInstance(), 20*7);
                if (winner == null) return;
                winner.removeGameTask();
                winner.getPlayer().setAllowFlight(true);
                winner.getConfigPlayerInterface().addWins(1);
                MessageTemplate.PLAYER_IS_WINNER.broadcast(winner.getPlayer().displayName().color(NamedTextColor.AQUA));
                Integer rewardingCoins = getMaxPossibleRewardForThisRound();
                winner.reward(rewardingCoins != null ? rewardingCoins : 0);
            }
        }
        lastUpdateGameState = gameState;
    }

    @Nullable public Integer getRewardingCoinsForThisRound() {
        if (!List.of(GameState.RUNNING, GameState.ENDING).contains(gameState)) return null;
        return 5 * ( currentLobbyMaxPlayers - 1 - GamePlayerManager.getInstance().getAlivePlayers().size() );
    }

    @Nullable public Integer getMaxPossibleRewardForThisRound() {
        if (!List.of(GameState.RUNNING, GameState.ENDING).contains(gameState)) return null;
        return 5 * ( currentLobbyMaxPlayers - 1 );
    }

    @NotNull public Material getRandomPossibleMaterial() {
        return randomMaterials.get(new Random().nextInt(randomMaterials.size()));
    }

    @NotNull public ItemStack getRandomPossibleItemstack() {
        return new ItemStack(getRandomPossibleMaterial());
    }

    @NotNull public static GameHandler getInstance() {
        return gameHandlerInstance;
    }

    @NotNull public GameState getGameState() {
        return this.gameState;
    }

}
