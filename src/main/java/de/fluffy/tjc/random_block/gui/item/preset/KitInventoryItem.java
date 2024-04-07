package de.fluffy.tjc.random_block.gui.item.preset;

import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.gui.item.GUIItem;
import de.fluffy.tjc.random_block.gui.namespaces.PreservedNamespaces;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KitInventoryItem extends ItemPreset {

    private static KitInventoryItem instance;

    public KitInventoryItem() {
        super(Material.MINECART);
        instance = this;
    }

    @Override
    public void prepare() {
    }

    @Override
    public @NotNull ItemStack live(@NotNull GamePlayer player) {
        // TODO USE PLAYER KIT STATE
        GUIItem guiItem = getGUIItem().clone();
        MiniMessage msg = MiniMessage.miniMessage();
        guiItem.setDisplayName(msg.deserialize(
                "<red>Kein Kit Ausgewählt"
        )).addLoreLine(msg.deserialize(
                "<gray>Wähle ein Kit aus..."
        ))
            .setKey(PreservedNamespaces.GUI_ITEM_ID.getKey(), "lobby:kits:inventory")
                .setKey(PreservedNamespaces.LOCKED_IN_INVENTORY.getKey(), "locked");
        return guiItem.build();
    }

    public static KitInventoryItem getInstance() {
        return instance;
    }
}
