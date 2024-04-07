package de.fluffy.tjc.random_block.gui.inventory.preset;

import de.fluffy.tjc.random_block.game.player.GamePlayer;
import de.fluffy.tjc.random_block.gui.inventory.GUIInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class InventoryPreset {

    private final GUIInventory inventory;

    public InventoryPreset(int size, @NotNull Component title) {
        this.inventory = new GUIInventory(size, title);
        prepare();
    }

    public InventoryPreset(InventoryType type, @NotNull Component title) {
        this.inventory = new GUIInventory(type, title);
    }

    public abstract void prepare();

    public @Nullable abstract Inventory live(@NotNull GamePlayer player);

    public GUIInventory getGUIInventory() {
        return inventory;
    }
}
