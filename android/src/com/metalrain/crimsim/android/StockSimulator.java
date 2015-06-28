package com.metalrain.crimsim.android;

import android.app.Application;

import com.metalrain.crimsim.state.GameState;

/**
 * Created by Adam Hammer on 15-06-24.
 */
public class StockSimulator extends Application {

    private GameState state;

    private static StockSimulator instance;

    public static GameState getGameState() {
        return instance.state;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        state = new GameState();
        instance = this;
    }
}
