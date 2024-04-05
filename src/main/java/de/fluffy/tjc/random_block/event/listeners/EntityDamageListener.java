package de.fluffy.tjc.random_block.event.listeners;

import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.game.player.GamePlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.stream.Stream;

public class EntityDamageListener implements Listener {

    public EntityDamageListener(@NotNull JavaPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onEvent(@NotNull EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player target)) return;
        HashSet<GamePlayer> players = GamePlayerManager.getInstance().getAllPlayers(true, true, true);
        players.forEach(gamePlayer -> {
            if (!gamePlayer.getPlayer().equals(target)) return;
            gamePlayer.onDamage(event);
        });
    }

}
