package de.fluffy.tjc.random_block.event.listeners;

import de.fluffy.tjc.random_block.game.player.GamePlayerManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AsyncChatListener implements Listener {

    public AsyncChatListener(@NotNull JavaPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onEvent(@NotNull AsyncChatEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);
        MiniMessage msg = MiniMessage.miniMessage();
        Bukkit.broadcast(event.getPlayer().displayName().color(NamedTextColor.GRAY).append(
                msg.deserialize(" <dark_gray>» ").append(event.message().color(NamedTextColor.GRAY))
        ));
    }

}
