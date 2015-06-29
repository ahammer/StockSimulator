package com.metalrain.stocksimulator.textClient.sections;

import com.metalrain.stocksimulator.textClient.ITextClientSection;
import com.metalrain.stocksimulator.textClient.TextClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class TestDeserializeSection implements ITextClientSection {
    private final TextClient client;

    public TestDeserializeSection(TextClient textClient) {
        this.client = textClient;
    }

    @Override
    public String getName() {
        return "Deserialize";
    }

    @Override
    public String getDescription() {
        return "Serialize and then De-serialize the state";
    }

    @Override
    public void invoke(String command, Scanner scanner) {

        client.getState().deserialize(client.getState().serialize());
    }
}
