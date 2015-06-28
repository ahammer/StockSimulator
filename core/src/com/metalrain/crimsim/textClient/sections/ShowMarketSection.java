package com.metalrain.crimsim.textClient.sections;

import com.metalrain.crimsim.textClient.ITextClientSection;
import com.metalrain.crimsim.textClient.TextClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class ShowMarketSection implements ITextClientSection {
    private final TextClient client;

    public ShowMarketSection(TextClient textClient) {
        this.client = textClient;
    }

    @Override
    public String getName() {
        return "Market";
    }

    @Override
    public String getDescription() {
        return "Show the current Market State";
    }

    @Override
    public void invoke(String command, Scanner scanner) {
        client.getState().printMarketPrices();
    }
}
