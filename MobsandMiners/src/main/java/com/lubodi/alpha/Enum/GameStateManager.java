package com.lubodi.alpha.Enum;

public class GameStateManager {
    private static GameStateManager instance;
    private GameState gameState;

    private GameStateManager() {
        this.gameState = GameState.NoLive;
    }

    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return this.gameState;
    }
}
