package de.fluffy.tjc.random_block.gui.inventory.preset;

import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.gui.inventory.GUIInventory;
import de.fluffy.tjc.random_block.gui.item.preset.BackgroundItem;
import de.fluffy.tjc.random_block.gui.item.preset.KitInventoryItem;
import de.fluffy.tjc.random_block.gui.item.preset.KitShopItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class KitInventory extends InventoryPreset {

    private static KitInventory instance;

    public KitInventory() {
        super(3, MiniMessage.miniMessage().deserialize("Kits"));
        instance = this;
    }

    @Override
    public void prepare() {}

    @Override
    @NotNull public Inventory live(@NotNull GamePlayer player) {
        GUIInventory guiInventory = getGUIInventory();
        guiInventory.fill(BackgroundItem.getItem());
        guiInventory.setItem(12, KitInventoryItem.getInstance().live(player));
        guiInventory.setItem(14, KitShopItem.getItem());
        return guiInventory.build();
    }

    public static KitInventory getInstance() {
        return instance;
    }
}
