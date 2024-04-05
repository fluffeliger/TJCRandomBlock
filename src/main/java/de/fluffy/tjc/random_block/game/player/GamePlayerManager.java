package de.fluffy.tjc.random_block.game.player;

import de.fluffy.tjc.random_block.game.GameHandler;
import de.fluffy.tjc.random_block.game.GameState;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;

public class GamePlayerManager {

    private static GamePlayerManager gamePlayerManagerInstance = null;
    private final HashMap<Player, GamePlayer> lobby = new HashMap<>();
    private final HashMap<Player, GamePlayer> alive = new HashMap<>();
    private final HashMap<Player, GamePlayer> dead = new HashMap<>();

    public GamePlayerManager() {
        gamePlayerManagerInstance = this;
    }

    public void register(@NotNull Player player) {
        GameHandler gameHandler = GameHandler.getInstance();
        GamePlayer gamePlayer = new GamePlayer(player);
        switch (gameHandler.getGameState()) {
            case RUNNING, ENDING -> this.dead.put(player, gamePlayer);
            case WAITING, STARTING -> this.lobby.put(player, gamePlayer);
        }
        gamePlayer.update();
        GameHandler.getInstance().playerUpdate();
    }

    public void unregister(@NotNull Player player) {
        silentUnregister(player);
        GameHandler.getInstance().playerUpdate();
    }

    public void silentUnregister(@NotNull Player player) {
        this.lobby.remove(player);
        this.alive.remove(player);
        this.dead.remove(player);
    }

    @Nullable public GamePlayer getGamePlayer(Player player) {
        if (lobby.containsKey(player)) return lobby.get(player);
        if (alive.containsKey(player)) return alive.get(player);
        if (dead.containsKey(player)) return dead.get(player);
        return null;
    }

    public HashSet<GamePlayer> getAllPlayers(boolean allowLobby, boolean allowAlive, boolean allowDead) {
        HashSet<GamePlayer> players = new HashSet<>();
        if (allowLobby) players.addAll(getLobbyPlayers());
        if (allowAlive) players.addAll(getAlivePlayers());
        if (allowDead) players.addAll(getDeadPlayers());
        return players;
    }

    @Nullable public PlayerState getPlayerState(@NotNull Player player) {
        if (lobby.containsKey(player)) return PlayerState.LOBBY;
        if (alive.containsKey(player)) return PlayerState.ALIVE;
        if (dead.containsKey(player)) return PlayerState.DEAD;
        return null;
    }

    @Nullable public PlayerState getPlayerState(@NotNull GamePlayer gamePlayer) {
        return getPlayerState(gamePlayer.getPlayer());
    }

    public void updatePlayerState(@NotNull GamePlayer gamePlayer, @NotNull PlayerState state) {
        Player player = gamePlayer.getPlayer();
        silentUnregister(player);
        switch (state) {
            case LOBBY -> lobby.put(player, gamePlayer);
            case ALIVE -> alive.put(player, gamePlayer);
            case DEAD -> dead.put(player, gamePlayer);
        }
        GameHandler.getInstance().playerUpdate();
        gamePlayer.update();
    }

    public void moveAllToLobby() {
        new HashSet<>(getAllPlayers(false, true, true)).forEach(player -> {
            player.setState(PlayerState.LOBBY);
        });
    }

    @NotNull public HashSet<GamePlayer> getLobbyPlayers() {
        return new HashSet<>(lobby.values());
    }

    @NotNull public HashSet<GamePlayer> getAlivePlayers() {
        return new HashSet<>(alive.values());
    }

    @NotNull public HashSet<GamePlayer> getDeadPlayers() {
        return new HashSet<>(dead.values());
    }

    @NotNull public static GamePlayerManager getInstance() {
        return gamePlayerManagerInstance;
    }
}
