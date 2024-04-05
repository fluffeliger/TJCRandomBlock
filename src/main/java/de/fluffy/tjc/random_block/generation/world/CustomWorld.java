package de.fluffy.tjc.random_block.generation.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class CustomWorld {

    private ChunkGenerator chunkGenerator = null;
    private World.Environment environment = null;
    private World world = null;
    private final String name;

    public CustomWorld(@NotNull String name) {
        this.name = name;
    }

    public void setEnvironment(@NotNull World.Environment environment) {
        this.environment = environment;
    }

    public void setChunkGenerator(@NotNull ChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }

    public void loadAndGenerate() {
        WorldCreator creator = new WorldCreator(this.name);
        if (this.chunkGenerator != null) creator.generator(this.chunkGenerator);
        if (this.environment != null) creator.environment(this.environment);
        this.world = creator.createWorld();
    }

    public void loadStartingArea() {
        if (!this.worldAvailable()) return;
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                this.world.loadChunk(x, z);
            }
        }
    }

    public boolean unload(boolean save) {
        if (!this.worldAvailable()) return false;
        boolean result = Bukkit.unloadWorld(this.world, save);
        this.world = null;
        return result;
    }

    public boolean deleteWorldFolder() {
        if (this.worldAvailable()) {
            Bukkit.unloadWorld(this.world, false);
            this.world = null;
        }
        try {
            FileUtils.deleteDirectory(new File("./" + this.name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean copyTo(@NotNull File file) {
        if (this.worldAvailable()) this.world.save();
        try {
            FileUtils.copyDirectoryStructure(new File("./" + this.name), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean worldAvailable() {
        return this.world != null;
    }

    @NotNull public String getName() {
        return this.name;
    }

    @Nullable public World getWorld() {
        return world;
    }

}
