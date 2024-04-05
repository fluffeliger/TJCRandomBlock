package de.fluffy.tjc.random_block.data.config.cinterface;

import de.fluffy.tjc.random_block.data.config.ConfigFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InterfaceItem<T> {

    private final ConfigFile configFile;
    private final String path;
    private final Class<?> typeClass;
    private final T defaultValue;

    public InterfaceItem(ConfigFile configFile, String path, Class<?> typeClass, T defaultValue) {
        this.configFile = configFile;
        this.path = path;
        this.typeClass = typeClass;
        this.defaultValue = defaultValue;
    }

    public void reset() {
        set(defaultValue);
    }

    public void set(@Nullable T data) {
        this.configFile.getYML().set(this.path, data);
        this.configFile.save();
    }

    @SuppressWarnings("unchecked")
    @Nullable public T get() {
        Object data = this.configFile.getYML().get(this.path);
        if (data == null || !data.getClass().isAssignableFrom(typeClass)) return null;
        return (T) data;
    }

}
