package de.fluffy.tjc.random_block.gui.item.preset;

import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.gui.item.GUIItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ItemPreset {

    private final GUIItem guiItem;

    public ItemPreset(@NotNull Material material) {
        this.guiItem = new GUIItem(material);
        prepare();
    }

    public abstract void prepare();
    @Nullable public abstract ItemStack live(@NotNull GamePlayer player);

    @NotNull public GUIItem getGUIItem() {
        return guiItem;
    }
}
