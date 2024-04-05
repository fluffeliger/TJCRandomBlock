package de.fluffy.tjc.random_block.data;

import de.fluffy.tjc.random_block.data.config.ConfigFile;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DataInterface {

    private static DataInterface dataInterfaceInstance = null;
    private final HashMap<String, File> fileMap = new HashMap<>();
    private final HashMap<String, ConfigFile> configMap = new HashMap<>();

    public void registerDirectory(@NotNull File file) {
        dataInterfaceInstance = this;
        registerDirectory(file, null);
    }

    public void registerDirectory(@NotNull File file, @Nullable String name) {
        if (name != null) fileMap.put(name, file);
        if (file.exists()) return;
        file.mkdir();
    }

    public void registerFile(@NotNull File file, @NotNull String name) {
        fileMap.put(name, file);
    }

    @Nullable public ConfigFile getConfig(@NotNull String name) {
        if (configMap.containsKey(name)) return configMap.get(name);
        File file = fileMap.get(name);
        if (file == null) return null;
        if (!file.exists()) return null;
        ConfigFile configFile = new ConfigFile(file);
        configMap.put(name, configFile);
        return configFile;
    }

    @Nullable public File getFile(String name) {
        return this.fileMap.get(name);
     }

    public static DataInterface getInstance() {
        return dataInterfaceInstance;
    }

    public static void copyDir(File source, File target) {
        try {
            FileUtils.copyDirectoryStructure(source, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteDir(File source) {
        try {
            FileUtils.deleteDirectory(source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
