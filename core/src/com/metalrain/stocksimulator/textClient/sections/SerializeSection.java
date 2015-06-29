package com.metalrain.stocksimulator.textClient.sections;

import com.metalrain.stocksimulator.textClient.ITextClientSection;
import com.metalrain.stocksimulator.textClient.TextClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class SerializeSection implements ITextClientSection {
    private final TextClient client;

    public SerializeSection(TextClient textClient) {
        this.client = textClient;
    }

    @Override
    public String getName() {
        return "Serialize";
    }

    @Override
    public String getDescription() {
        return "Serialize the state";
    }

    @Override
    public void invoke(String command, Scanner scanner) {
        System.out.println(client.getState().serialize());
    }
}
