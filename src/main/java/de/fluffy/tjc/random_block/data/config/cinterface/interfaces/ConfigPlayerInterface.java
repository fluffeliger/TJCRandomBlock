package de.fluffy.tjc.random_block.data.config.cinterface.interfaces;

import de.fluffy.tjc.random_block.data.config.ConfigFile;
import de.fluffy.tjc.random_block.data.config.cinterface.InterfaceItem;
import de.fluffy.tjc.random_block.data.config.path.ConfigPaths;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ConfigPlayerInterface {
    private final InterfaceItem<Integer> coins;
    private final InterfaceItem<Integer> wins;
    private final InterfaceItem<Integer> losses;
    private Consumer<ConfigPlayerInterface> updateLambda = null;

    public ConfigPlayerInterface(@NotNull ConfigFile file) {
        coins = new InterfaceItem<>(file, ConfigPaths.PLAYER_MONEY.getPath(), Integer.class, (Integer)ConfigPaths.PLAYER_MONEY.getDefault());
        wins = new InterfaceItem<>(file, ConfigPaths.PLAYER_WINS.getPath(), Integer.class, (Integer)ConfigPaths.PLAYER_WINS.getDefault());
        losses = new InterfaceItem<>(file, ConfigPaths.PLAYER_LOSSES.getPath(), Integer.class,(Integer)ConfigPaths.PLAYER_LOSSES.getDefault());
    }

    public void setUpdateLambda(@NotNull Consumer<ConfigPlayerInterface> lambda) {
        this.updateLambda = lambda;
    }

    public void reset() {
        coins.reset();
        wins.reset();
        losses.reset();
    }

    private void runUpdate() {
        if (updateLambda != null) List.of(this).forEach(updateLambda);
    }

    @NotNull public Integer getCoins() {
        Integer amount = coins.get();
        return amount != null ? amount : 0;
    }

    public void setCoins(@NotNull Integer amount) {
        coins.set(amount);
        runUpdate();
    }

    public void addCoins(@NotNull Integer amount) { setCoins(getCoins() + amount); }

    @NotNull public Integer getWins() {
        Integer amount = wins.get();
        return amount != null ? amount : 0;
    }

    public void setWins(@NotNull Integer amount) {
        wins.set(amount);
        runUpdate();
    }

    public void addWins(@NotNull Integer amount) { setWins(getWins() + amount); }

    @NotNull public Integer getLosses() {
        Integer amount = losses.get();
        return amount != null ? amount : 0;
    }

    public void setLosses(@NotNull Integer amount) {
        losses.set(amount);
        runUpdate();
    }

    public void addLosses(@NotNull Integer amount) { setLosses(getLosses() + amount); }

}
