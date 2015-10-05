package com.metalrain.stocksimulator.textClient.sections;

import com.metalrain.stocksimulator.textClient.ITextClientSection;
import com.metalrain.stocksimulator.textClient.TextClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class SerializeGameSection implements ITextClientSection {
    private final TextClient client;

    public SerializeGameSection(TextClient textClient) {
        this.client = textClient;
    }

    @Override
    public String getName() {
        return "Save";
    }

    @Override
    public String getDescription() {
        return "Save the state";
    }

    @Override
    public void invoke(String command, Scanner scanner) {
        String json = client.getState().saveToJson();
        System.out.println(json);
        client.savedGameJson = json;
    }
}
