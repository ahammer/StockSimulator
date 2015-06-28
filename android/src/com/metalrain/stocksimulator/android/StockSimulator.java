package com.metalrain.stocksimulator.android;

import android.app.Application;

import com.metalrain.stocksimulator.state.GameState;

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
        instance = this;
    }

    public static void initializeGameState(int seed, int iterations) {
        instance.state = new GameState(seed, iterations);
    }

    public static boolean hasGameStarted() {
        return (instance.state != null);
    }
}
