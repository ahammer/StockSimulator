package com.metalrain.stocksimulator;

import com.metalrain.stocksimulator.state.GameState;
import com.metalrain.stocksimulator.textClient.TextClient;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class crimsim_text {
    public static void main(String ... args) {
        System.out.println("Welcome to Crimsim Text Mode");
        GameState state = new GameState((int) (Math.random()*10000), 5000);
        state.startThread();
        TextClient textClient = new TextClient(state);
        textClient.start();
        state.stopThread();
    }
}
