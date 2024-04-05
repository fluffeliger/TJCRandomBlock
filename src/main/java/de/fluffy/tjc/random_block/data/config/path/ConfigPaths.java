package de.fluffy.tjc.random_block.data.config.path;

import org.jetbrains.annotations.NotNull;

public enum ConfigPaths {

    PLAYER_MONEY("coins", 20),
    PLAYER_WINS("wins", 0),
    PLAYER_LOSSES("losses", 0),
    CONFIG_COUNTDOWN("static.countdown", 10),
    CONFIG_MIN_PLAYERS("static.players.min", 2);

    private final String path;
    private final Object defaultValue;

    ConfigPaths(@NotNull String path, @NotNull Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    @NotNull public String getPath(@NotNull Object... replacements) {
        return this.path.formatted(replacements);
    }

    @NotNull public Object getDefault() {
        return this.defaultValue;
    }

}
