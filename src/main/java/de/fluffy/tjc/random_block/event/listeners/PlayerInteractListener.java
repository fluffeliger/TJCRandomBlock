package de.fluffy.tjc.random_block.event.listeners;

import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.game.player.GamePlayerManager;
import de.fluffy.tjc.random_block.gui.inventory.preset.KitInventory;
import de.fluffy.tjc.random_block.gui.item.preset.KitChestItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractListener implements Listener {

    public PlayerInteractListener(@NotNull JavaPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onEvent(@NotNull PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;
        if (!event.getAction().isRightClick()) return;
        if (!item.equals(KitChestItem.getItem())) return;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);
        if (gamePlayer == null) return;
        player.openInventory(KitInventory.getInstance().live(gamePlayer));
    }

}
