package de.fluffy.tjc.random_block.game;

import de.fluffy.tjc.random_block.game.player.GameStateType;

public enum GameState {

    WAITING(GameStateType.OUT_GAME),
    STARTING(GameStateType.OUT_GAME),
    SETUP_RUNNING(GameStateType.OUT_GAME),
    RUNNING(GameStateType.IN_GAME),
    ENDING(GameStateType.IN_GAME);

    private final GameStateType type;

    GameState(GameStateType type) {
        this.type = type;
    }

    public GameStateType getType() {
        return type;
    }
}
