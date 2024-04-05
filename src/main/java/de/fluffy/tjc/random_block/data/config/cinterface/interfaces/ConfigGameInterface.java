package de.fluffy.tjc.random_block.data.config.cinterface.interfaces;

import de.fluffy.tjc.random_block.data.config.ConfigFile;
import de.fluffy.tjc.random_block.data.config.cinterface.InterfaceItem;
import de.fluffy.tjc.random_block.data.config.path.ConfigPaths;
import org.jetbrains.annotations.NotNull;

public class ConfigGameInterface {
    private final InterfaceItem<Integer> minPlayers;
    private final InterfaceItem<Integer> countdownSeconds;

    public ConfigGameInterface(@NotNull ConfigFile file) {
        minPlayers = new InterfaceItem<>(
                file,
                ConfigPaths.CONFIG_MIN_PLAYERS.getPath(),
                Integer.class,
                (Integer)ConfigPaths.CONFIG_MIN_PLAYERS.getDefault()
        );
        countdownSeconds = new InterfaceItem<>(
                file,
                ConfigPaths.CONFIG_COUNTDOWN.getPath(),
                Integer.class,
                (Integer)ConfigPaths.CONFIG_COUNTDOWN.getDefault()
        );
    }

    public void reset() {
        minPlayers.reset();
        countdownSeconds.reset();
    }

    public Integer getMinPlayers() {
        Integer amount = minPlayers.get();
        return amount != null ? amount : 0;
    }

    public Integer getCountdownSeconds() {
        Integer amount = countdownSeconds.get();
        return amount != null ? amount : 0;
    }

}
