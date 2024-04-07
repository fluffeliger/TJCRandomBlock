package de.fluffy.tjc.random_block.event.listeners;

import de.fluffy.tjc.random_block.gui.namespaces.PreservedNamespaces;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerDropItemListener implements Listener {

    private final JavaPlugin instance;

    public PlayerDropItemListener(@NotNull JavaPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
        this.instance = instance;
    }

    @EventHandler
    public void onEvent(@NotNull PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(PreservedNamespaces.LOCKED_IN_INVENTORY.getKey(instance))) event.setCancelled(true);
    }

}
