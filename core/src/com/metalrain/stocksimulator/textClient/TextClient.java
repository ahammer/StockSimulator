package com.metalrain.stocksimulator.textClient;

import com.metalrain.stocksimulator.state.GameState;
import com.metalrain.stocksimulator.textClient.sections.HelpSection;
import com.metalrain.stocksimulator.textClient.sections.LoadGameSection;
import com.metalrain.stocksimulator.textClient.sections.MakePurchaseSection;
import com.metalrain.stocksimulator.textClient.sections.MakeSaleSection;
import com.metalrain.stocksimulator.textClient.sections.MarketHistorySection;
import com.metalrain.stocksimulator.textClient.sections.PlayerSection;
import com.metalrain.stocksimulator.textClient.sections.SerializeGameSection;
import com.metalrain.stocksimulator.textClient.sections.ShowMarketSection;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class TextClient {

    private final GameState state;
    public String savedGameJson;

    private final List<ITextClientSection> sectionList = Arrays.asList(
            new HelpSection(this),
            new ShowMarketSection(this),
            new MarketHistorySection(this),
            new PlayerSection(this),
            new MakePurchaseSection(this),
            new MakeSaleSection(this),
            new SerializeGameSection(this),
            new LoadGameSection(this)
    );

    public TextClient(GameState state) {
        this.state = state;

    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String nextLine = "";

        do {
            System.out.print("Enter a command (help): ");
            try {
                nextLine = scanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Goodbye");
                return;
            }
            for (ITextClientSection section:sectionList) {
                if (section.getName().equalsIgnoreCase(nextLine)){
                    System.out.println("\n---- Invoking " + section.getName() + " ----");
                    section.invoke(nextLine, scanner);
                    System.out.println();
                }
            }
        } while (!nextLine.equalsIgnoreCase("quit"));

    }

    public List<ITextClientSection> getSectionList() {
        return sectionList;

    }

    public GameState getState() {
        return state;
    }
}
