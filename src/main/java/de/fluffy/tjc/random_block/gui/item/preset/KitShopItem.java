package de.fluffy.tjc.random_block.gui.item.preset;

import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.gui.item.GUIItem;
import de.fluffy.tjc.random_block.gui.namespaces.PreservedNamespaces;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KitShopItem extends ItemPreset {

    private static ItemStack item;

    public KitShopItem() {
        super(Material.CHEST);
    }

    @Override
    public void prepare() {
        GUIItem guiItem = getGUIItem();
        MiniMessage msg = MiniMessage.miniMessage();
        guiItem.setDisplayName(msg.deserialize(
                "<gold>Kit Shop"
        )).addLoreLine(msg.deserialize(
                "<gray>Kaufe neue Kits"
        ))
            .setKey(PreservedNamespaces.GUI_ITEM_ID.getKey(), "lobby:kits:buy")
            .setKey(PreservedNamespaces.LOCKED_IN_INVENTORY.getKey(), "locked");
        item = guiItem.build();
    }

    @Override
    public @Nullable ItemStack live(@NotNull GamePlayer player) {
        return null;
    }

    public static ItemStack getItem() {
        return item;
    }
}
