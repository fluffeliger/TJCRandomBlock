package de.fluffy.tjc.random_block.game.player;

import de.fluffy.tjc.random_block.RandomBlockPlugin;
import de.fluffy.tjc.random_block.data.DataInterface;
import de.fluffy.tjc.random_block.data.config.ConfigFile;
import de.fluffy.tjc.random_block.data.config.cinterface.interfaces.ConfigPlayerInterface;
import de.fluffy.tjc.random_block.data.config.path.ConfigPaths;
import de.fluffy.tjc.random_block.game.GameHandler;
import de.fluffy.tjc.random_block.game.GameState;
import de.fluffy.tjc.random_block.game.message.MessageTemplate;
import de.fluffy.tjc.random_block.game.message.ui.SoundBundle;
import de.fluffy.tjc.random_block.game.player.scoreboard.ScoreboardPlayer;
import de.fluffy.tjc.random_block.generation.world.CustomWorld;
import de.fluffy.tjc.random_block.generation.world.WorldManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class GamePlayer {

    private final Player player;
    private final ConfigPlayerInterface configPlayerInterface;
    private PlayerState lastUpdateState = null;
    private BukkitTask gameTask = null;
    private int maxWaitTicks = 20*10;
    private int currentWaitTicks = maxWaitTicks;
    private SoundBundle itemDropSoundBundle = new SoundBundle(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    private final ScoreboardPlayer scoreboardPlayer;

    public GamePlayer(@NotNull Player player) {
        this.player = player;
        DataInterface dataInterface = DataInterface.getInstance();
        File userFolder = dataInterface.getFile("users");
        assert userFolder != null;

        ConfigFile configFile = new ConfigFile(Path.of(userFolder.getPath(), player.getUniqueId() + ".yml").toFile());
        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(ConfigPaths.PLAYER_MONEY.getPath(), ConfigPaths.PLAYER_MONEY.getDefault());
        defaults.put(ConfigPaths.PLAYER_WINS.getPath(), ConfigPaths.PLAYER_WINS.getDefault());
        defaults.put(ConfigPaths.PLAYER_LOSSES.getPath(), ConfigPaths.PLAYER_LOSSES.getDefault());
        configFile.setup(defaults);
        configPlayerInterface = new ConfigPlayerInterface(configFile);

        scoreboardPlayer = new ScoreboardPlayer(player, configPlayerInterface);
    }

    public void reward(@NotNull Integer rewardCoins) {
        getConfigPlayerInterface().addCoins(rewardCoins);
        MessageTemplate.PLAYER_REWARD_COINS.send(player, true, rewardCoins);
    }

    public void updateXPBar() {
        int ticks = getCurrentWaitTicks();
        int maxTicks = getMaxWaitTicks();
        player.setLevel(Math.min((int)(ticks / 20F) + 1, (int)(maxTicks / 20F)));
        player.setExp((float) ticks / maxTicks);
    }

    public void updateScoreboard() {
        scoreboardPlayer.updateScoreboard();
    }

    public void dropItem() {
        if (getState() != PlayerState.ALIVE) return;
        ItemStack item = GameHandler.getInstance().getRandomPossibleItemstack();
        HashMap<Integer, ItemStack> failedItems = player.getInventory().addItem(item);
        failedItems.forEach((slot, failedItem) -> player.getWorld().dropItem(player.getLocation(), failedItem));
    }

    public void update() {
        PlayerState state = getState();
        if (lastUpdateState == state) return;
        lastUpdateState = state;
        player.setHealth(20);
        player.setSaturation(20);
        player.closeInventory();
        player.setExp(0);
        player.setLevel(0);
        player.clearActivePotionEffects();
        player.getEnderChest().clear();
        player.setFlying(false);
        player.setAllowFlight(false);
        updateInventory();
        removeGameTask();

        switch (state) {
            case LOBBY -> {
                player.setGameMode(GameMode.ADVENTURE);
                teleportToLobbyLocation();
            }
            case ALIVE -> {
                player.setGameMode(GameMode.SURVIVAL);
                gameTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        updateXPBar();
                        if (getCurrentWaitTicks() == 0) {
                            itemDropSoundBundle.play(player);
                            dropItem();
                        }
                        tick();
                    }
                }.runTaskTimer(RandomBlockPlugin.getInstance(), 0, 1);
            }
            case DEAD -> {
                player.setGameMode(GameMode.SPECTATOR);
                teleportToSpectatorLocation();
            }
        }
    }

    public void teleportToSpectatorLocation() {
        WorldManager worldManager = WorldManager.getInstance();
        CustomWorld customWorld = worldManager.getWorld("void_copy");
        assert customWorld != null;
        World world = customWorld.getWorld();
        Location location = new Location(world, 0, 64, 0, 0, 0);
        player.teleport(location);
    }

    public void teleportToLobbyLocation() {
        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    }

    @NotNull public PlayerState getState() {
        PlayerState state = GamePlayerManager.getInstance().getPlayerState(this);
        assert state != null;
        return state;
    }

    public void removeGameTask() {
        if (gameTask != null) {
            gameTask.cancel();
            gameTask = null;
        }
    }

    public void setMaxWaitTicks(int ticks) {
        maxWaitTicks = ticks;
    }

    public int getMaxWaitTicks() {
        return maxWaitTicks;
    }

    public int getCurrentWaitTicks() {
        if (currentWaitTicks > maxWaitTicks) currentWaitTicks = maxWaitTicks;
        if (currentWaitTicks < 0) currentWaitTicks = maxWaitTicks;
        return currentWaitTicks;
    }

    public void resetTicks() {
        currentWaitTicks = maxWaitTicks;
    }

    public void tick() {
        currentWaitTicks --;
    }

    public void setState(@NotNull PlayerState state) {
        GamePlayerManager.getInstance().updatePlayerState(this, state);
    }

    public void onDamage(@NotNull EntityDamageEvent event) {
        event.setCancelled(getState() != PlayerState.ALIVE);
    }

    public void onDeath(@NotNull PlayerDeathEvent event) {
        event.setCancelled(true);
        player.setHealth(20);
        GameHandler gameHandler = GameHandler.getInstance();
        if (gameHandler.getGameState() != GameState.RUNNING) return;
        if (getState() != PlayerState.ALIVE) return;
        MessageTemplate.PLAYER_DEATH.broadcast(player.displayName().color(NamedTextColor.DARK_RED));
        getConfigPlayerInterface().addLosses(1);
        setState(PlayerState.DEAD);
        update();
    }

    public void onMove(@NotNull PlayerMoveEvent event) {
        if (getState() != PlayerState.ALIVE) return;
        if (GameHandler.getInstance().getGameState() != GameState.RUNNING) return;
        if (player.getLocation().getY() > 20) return;
        player.setHealth(0);
    }

    public void updateInventory() {
        switch (getState()) {
            case LOBBY -> {
                player.getInventory().clear();
                // TODO
            }
            case ALIVE, DEAD -> player.getInventory().clear();
        }
    }

    @NotNull public Player getPlayer() {
        return player;
    }

    @NotNull public ConfigPlayerInterface getConfigPlayerInterface() {
        return configPlayerInterface;
    }
}
