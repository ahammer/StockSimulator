package com.metalrain.crimsim.textClient;

import com.metalrain.crimsim.state.GameState;
import com.metalrain.crimsim.textClient.sections.HelpSection;
import com.metalrain.crimsim.textClient.sections.MakePurchaseSection;
import com.metalrain.crimsim.textClient.sections.MakeSaleSection;
import com.metalrain.crimsim.textClient.sections.MarketHistorySection;
import com.metalrain.crimsim.textClient.sections.PlayerSection;
import com.metalrain.crimsim.textClient.sections.ShowMarketSection;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class TextClient {

    private final GameState state;

    private final List<ITextClientSection> sectionList = Arrays.asList(
            new HelpSection(this),
            new ShowMarketSection(this),
            new MarketHistorySection(this),
            new PlayerSection(this),
            new MakePurchaseSection(this),
            new MakeSaleSection(this)


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
