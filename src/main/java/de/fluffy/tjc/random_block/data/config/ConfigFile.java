package de.fluffy.tjc.random_block.data.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigFile {

    private final File file;
    private YamlConfiguration yml = new YamlConfiguration();

    public ConfigFile(@NotNull File file) {
        this.file = file;
    }

    public boolean exists() {
        return file.exists();
    }

    public void setup(HashMap<String, Object> defaults) {
        if (exists()) load();
        defaults.forEach((key, value) -> {
            if (!yml.isSet(key)) yml.set(key, value);
        });
        save();
    }

    public void load() {
        if (!exists()) return;
        try { yml.load(file); }
        catch (IOException | InvalidConfigurationException e) { throw new RuntimeException(e); }
    }

    public void save() {
        try { yml.save(file); }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    @NotNull public YamlConfiguration getYML() {
        return yml;
    }

}
