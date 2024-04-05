package de.fluffy.tjc.random_block.event.listeners;

import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.game.player.GamePlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class PlayerDeathListener implements Listener {

    public PlayerDeathListener(@NotNull JavaPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onEvent(@NotNull PlayerDeathEvent event) {
        if (event.isCancelled()) return;
        Player target = event.getPlayer();
        HashSet<GamePlayer> players = GamePlayerManager.getInstance().getAllPlayers(true, true, true);
        players.forEach(gamePlayer -> {
            if (!gamePlayer.getPlayer().equals(target)) return;
            gamePlayer.onDeath(event);
        });

    }

}
