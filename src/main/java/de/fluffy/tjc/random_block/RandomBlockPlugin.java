package de.fluffy.tjc.random_block;

import org.bukkit.plugin.java.JavaPlugin;

public class RandomBlockPlugin extends JavaPlugin {

    private static RandomBlockPlugin pluginInstance = null;

    @Override
    public void onEnable() {
        pluginInstance = this;


    }

    @Override
    public void onDisable() {


        pluginInstance = null;
    }

    public static RandomBlockPlugin getPluginInstance() {
        return pluginInstance;
    }
}