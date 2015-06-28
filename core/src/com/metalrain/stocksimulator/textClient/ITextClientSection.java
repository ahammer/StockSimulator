package com.metalrain.stocksimulator.textClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public interface ITextClientSection {
    public String getName();
    public String getDescription();
    public void invoke(String command, Scanner scanner);
}
