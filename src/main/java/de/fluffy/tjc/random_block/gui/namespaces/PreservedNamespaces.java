package de.fluffy.tjc.random_block.gui.namespaces;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public enum PreservedNamespaces {

    LOCKED_IN_INVENTORY("LOCKED"),
    GUI_ITEM_ID("GUI_ITEM_ID");

    private final String key;

    PreservedNamespaces(String key) {
        this.key = key;
    }

    public NamespacedKey getKey(JavaPlugin instance) {
        return new NamespacedKey(instance, key);
    }

    public String getKey() {
        return key;
    }
}
