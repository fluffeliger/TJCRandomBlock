package de.fluffy.tjc.random_block.event.listeners;

import de.fluffy.tjc.random_block.gui.namespaces.PreservedNamespaces;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class InventoryClickListener implements Listener {

    private final JavaPlugin instance;

    public InventoryClickListener(@NotNull JavaPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
        this.instance = instance;
    }

    @EventHandler
    public void onEvent(@NotNull InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(PreservedNamespaces.LOCKED_IN_INVENTORY.getKey(instance))) event.setCancelled(true);
    }

}
