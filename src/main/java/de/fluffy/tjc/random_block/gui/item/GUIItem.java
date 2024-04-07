package de.fluffy.tjc.random_block.gui.item;

import de.fluffy.tjc.random_block.RandomBlockPlugin;
import de.fluffy.tjc.random_block.gui.item.preset.BackgroundItem;
import de.fluffy.tjc.random_block.gui.item.preset.KitChestItem;
import de.fluffy.tjc.random_block.gui.item.preset.KitInventoryItem;
import de.fluffy.tjc.random_block.gui.item.preset.KitShopItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class GUIItem {

    private Material material;
    private int amount = 1;
    private Component displayName = null;
    private final ArrayList<Component> lore = new ArrayList<>();
    private final HashMap<String, String> keys = new HashMap<>();
    private final JavaPlugin instance;
    private OfflinePlayer skullOwner = null;

    public GUIItem(@NotNull Material material) {
        this.material = material;
        this.instance = RandomBlockPlugin.getInstance();
    }

    @NotNull public GUIItem setMaterial(@NotNull Material material) {
        this.material = material;
        return this;
    }

    @NotNull  public GUIItem setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @NotNull public GUIItem setDisplayName(@NotNull Component component) {
        this.displayName = component.decoration(TextDecoration.ITALIC, false);
        return this;
    }

    @NotNull public GUIItem addLoreLine(@NotNull Component line) {
        this.lore.add(line.decoration(TextDecoration.ITALIC, false));
        return this;
    }

    @NotNull public GUIItem setKey(@NotNull String key, @NotNull String value) {
        this.keys.put(key, value);
        return this;
    }

    @NotNull public GUIItem setSkullOwner(@NotNull OfflinePlayer skullOwner) {
        this.skullOwner = skullOwner;
        return this;
    }

    @NotNull public ItemStack build() {
        ItemStack item = new ItemStack(this.material, this.amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.lore(this.lore);
            if (displayName != null) meta.displayName(displayName);
            PersistentDataContainer container = meta.getPersistentDataContainer();
            this.keys.forEach((key, value) -> container.set(new NamespacedKey(instance, key), PersistentDataType.STRING, value));
            item.setItemMeta(meta);

            if (this.material == Material.PLAYER_HEAD && skullOwner != null) {
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setOwningPlayer(this.skullOwner);
                item.setItemMeta(skullMeta);
            }
        }
        return item;
    }

    @NotNull public GUIItem clone() {
        GUIItem guiItem = new GUIItem(this.material);
        guiItem.amount = this.amount;
        guiItem.displayName = this.displayName;
        guiItem.lore.addAll(this.lore);
        guiItem.keys.putAll(this.keys);
        guiItem.skullOwner = skullOwner;
        return guiItem;
    }

    public static void prepareItems() {
        new BackgroundItem();
        new KitChestItem();
        new KitInventoryItem();
        new KitShopItem();
    }

}
