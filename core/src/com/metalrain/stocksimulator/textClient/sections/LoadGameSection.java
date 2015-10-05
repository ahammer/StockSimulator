package com.metalrain.stocksimulator.textClient.sections;

import com.metalrain.stocksimulator.textClient.ITextClientSection;
import com.metalrain.stocksimulator.textClient.TextClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class LoadGameSection implements ITextClientSection {
    private final TextClient client;

    public LoadGameSection(TextClient textClient) {
        this.client = textClient;
    }

    @Override
    public String getName() {
        return "Load";
    }

    @Override
    public String getDescription() {
        return "Load the state";
    }

    @Override
    public void invoke(String command, Scanner scanner) {
        if (client.savedGameJson != null) {
            client.getState().loadFromJson(client.savedGameJson);
        } else {
            System.out.println("Game save not available");
        }
    }
}
