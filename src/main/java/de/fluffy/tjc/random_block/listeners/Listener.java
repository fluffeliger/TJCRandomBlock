package de.fluffy.tjc.random_block.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Listener<T extends Event> implements org.bukkit.event.Listener {

    private final JavaPlugin pluginInstance;
    private boolean unregister = false;

    public Listener(JavaPlugin pluginInstance) {
        this.pluginInstance = pluginInstance;
    }

    public void register() {
        this.unregister = false;
        this.pluginInstance.getServer().getPluginManager().registerEvents(this, pluginInstance);
    }

    public void unregister() {
        this.unregister = true;
    }

    @EventHandler
    private void onEvent(T event) {
        if (unregister) {
            event.getHandlers().unregister(this);
            return;
        }
        this.event(event);
    }

    public abstract void event(T event);

}
