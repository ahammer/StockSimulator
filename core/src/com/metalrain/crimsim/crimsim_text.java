package com.metalrain.crimsim;

import com.metalrain.crimsim.state.GameState;
import com.metalrain.crimsim.textClient.TextClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class crimsim_text {
    public static void main(String ... args) {
        System.out.println("Welcome to Crimsim Text Mode");
        GameState state = new GameState();
        state.startThread();
        TextClient textClient = new TextClient(state);
        textClient.start();
        state.stopThread();
    }
}
