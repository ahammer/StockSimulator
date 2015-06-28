package com.metalrain.stocksimulator.textClient.sections;

import com.metalrain.stocksimulator.textClient.ITextClientSection;
import com.metalrain.stocksimulator.textClient.TextClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class HelpSection implements ITextClientSection {
    private final TextClient client;

    public HelpSection(TextClient textClient) {
        this.client = textClient;
    }

    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String getDescription() {
        return "Help with the text Client";
    }

    @Override
    public void invoke(String command, Scanner scanner) {
        for (ITextClientSection section:client.getSectionList()) {
            System.out.println(section.getName()+"\t\t\t"+section.getDescription());
        }
    }
}
