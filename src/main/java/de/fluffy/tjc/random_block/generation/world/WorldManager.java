package de.fluffy.tjc.random_block.generation.world;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class WorldManager {

    private static WorldManager worldManagerInstance = null;
    private final HashMap<String, CustomWorld> worldMap = new HashMap<>();

    public WorldManager() {
        worldManagerInstance = this;
    }

    public CustomWorld register(@NotNull String name, @Nullable ChunkGenerator chunkGenerator, @Nullable World.Environment environment) {
        CustomWorld world = new CustomWorld(name);
        if (chunkGenerator != null) world.setChunkGenerator(chunkGenerator);
        if (environment != null) world.setEnvironment(environment);
        this.worldMap.put(name, world);
        return world;
    }

    @Nullable public CustomWorld getWorld(@NotNull String name) {
        return worldMap.get(name);
    }

    public static WorldManager getInstance() {
        return worldManagerInstance;
    }
}
