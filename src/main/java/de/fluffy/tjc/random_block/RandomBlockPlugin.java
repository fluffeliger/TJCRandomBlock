package de.fluffy.tjc.random_block;

import de.fluffy.tjc.random_block.commands.WorldCommand;
import de.fluffy.tjc.random_block.generation.chunk.VoidChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomBlockPlugin extends JavaPlugin {

    private static RandomBlockPlugin pluginInstance = null;
    private World voidWorld;

    @Override
    public void onEnable() {
        pluginInstance = this;

        voidWorld = loadVoidWorld();

        new WorldCommand(this, "world");
    }

    @Override
    public void onDisable() {


        pluginInstance = null;
    }

    private World loadVoidWorld() {
        World world = Bukkit.getWorld("void");
        if (world != null) return world;
        WorldCreator worldCreator = new WorldCreator("void");
        worldCreator.type(WorldType.NORMAL);
        worldCreator.generator(new VoidChunkGenerator());
        return worldCreator.createWorld();
    }

    public static RandomBlockPlugin getPluginInstance() {
        return pluginInstance;
    }

    public World getVoidWorld() {
        return voidWorld;
    }
}