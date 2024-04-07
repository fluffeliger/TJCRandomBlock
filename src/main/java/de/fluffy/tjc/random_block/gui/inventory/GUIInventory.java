package de.fluffy.tjc.random_block.gui.inventory;

import de.fluffy.tjc.random_block.gui.inventory.preset.KitInventory;
import de.fluffy.tjc.random_block.gui.item.GUIItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GUIInventory {
    private final HashMap<Integer, ItemStack> data = new HashMap<>();
    private final int inventorySize;
    private final Component title;
    private final InventoryType inventoryType;

    public GUIInventory(int rows, @NotNull Component title) {
        this.inventorySize = 9 * rows;
        this.inventoryType = null;
        this.title = title;
    }

    public GUIInventory(@NotNull InventoryType inventoryType, @NotNull Component title) {
        this.inventorySize = inventoryType.getDefaultSize();
        this.inventoryType = inventoryType;
        this.title = title;
    }

    @NotNull public GUIInventory setItem(int slot, @NotNull ItemStack itemStack) {
        data.put(slot, itemStack);
        return this;
    }

    @NotNull GUIInventory setItem(int slot, @NotNull GUIItem item) {
        return setItem(slot, item.build());
    }

    @NotNull public GUIInventory fill(@NotNull ItemStack itemStack) {
        for (int i = 0; i < this.inventorySize; i++) setItem(i, itemStack);
        return this;
    }

    @NotNull public GUIInventory fill(@NotNull GUIItem item) {
        return fill(item.build());
    }

    @NotNull public GUIInventory fromTo(int start, int end, @NotNull ItemStack itemStack) {
        for (int i = start; i < this.inventorySize || i <= end; i++) setItem(i, itemStack);
        return this;
    }

    @NotNull public GUIInventory fromTo(int start, int end, @NotNull GUIItem item) {
        return fromTo(start, end, item.build());
    }

    public void clear() {
        this.data.clear();
    }

    public Inventory build() {
        Inventory inventory;
        if (inventoryType != null) inventory = Bukkit.createInventory(null, inventoryType, title);
        else inventory = Bukkit.createInventory(null, inventorySize, title);
        this.data.forEach(inventory::setItem);
        return inventory;
    }

    public static void prepareInventories() {
        new KitInventory();
    }

}
