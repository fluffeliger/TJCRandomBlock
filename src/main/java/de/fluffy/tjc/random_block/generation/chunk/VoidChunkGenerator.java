package de.fluffy.tjc.random_block.generation.chunk;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class VoidChunkGenerator extends ChunkGenerator {

    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int y = worldInfo.getMinHeight(); y < worldInfo.getMaxHeight(); y++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, y, z, Material.AIR);
                }
            }
        }
    }

}
