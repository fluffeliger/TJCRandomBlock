package de.fluffy.tjc.random_block.event.listeners;

import de.fluffy.tjc.random_block.game.GameHandler;
import de.fluffy.tjc.random_block.game.GameState;
import de.fluffy.tjc.random_block.game.message.MessageTemplate;
import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.game.player.GamePlayerManager;
import de.fluffy.tjc.random_block.game.player.GameStateType;
import de.fluffy.tjc.random_block.game.player.PlayerState;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

    public PlayerQuitListener(@NotNull JavaPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onEvent(@NotNull PlayerQuitEvent event) {
        GameState gameState = GameHandler.getInstance().getGameState();
        event.quitMessage(null);
        Player player = event.getPlayer();
        GamePlayerManager.getInstance().unregister(player);
        if (gameState.getType() == GameStateType.OUT_GAME) {
            MessageTemplate.PLAYER_QUIT.broadcast(player.displayName().color(NamedTextColor.GREEN));
            return;
        }
        MessageTemplate.PLAYER_DEATH_QUIT.broadcast(player.displayName().color(NamedTextColor.DARK_GRAY));
        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);
        if (gamePlayer == null) return;
        if (gamePlayer.getState() != PlayerState.ALIVE) return;
        gamePlayer.getConfigPlayerInterface().addLosses(1);
    }

}
