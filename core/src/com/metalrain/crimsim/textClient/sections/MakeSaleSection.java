package com.metalrain.crimsim.textClient.sections;

import com.badlogic.ashley.core.Family;
import com.metalrain.crimsim.state.components.InventoryComponent;
import com.metalrain.crimsim.state.components.MarketItemComponent;
import com.metalrain.crimsim.state.components.NameComponent;
import com.metalrain.crimsim.state.components.PriceComponent;
import com.metalrain.crimsim.state.components.WalletComponent;
import com.metalrain.crimsim.state.entities.MarketItemEntity;
import com.metalrain.crimsim.state.entities.PlayerEntity;
import com.metalrain.crimsim.textClient.ITextClientSection;
import com.metalrain.crimsim.textClient.TextClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class MakeSaleSection implements ITextClientSection {

    private final TextClient client;

    public MakeSaleSection(TextClient textClient) {

        this.client = textClient;
        Family marketItems = Family.all(MarketItemComponent.class, NameComponent.class).get();
    }

    @Override
    public String getName() {
        return "Sell";
    }

    @Override
    public String getDescription() {
        return "Sell some inventory";
    }

    @Override
    public void invoke(String command, Scanner scanner) {
        client.getState().printMarketPrices();
        System.out.println("What do you want to sell: ");

        System.out.print("Enter Product Name: ");
        String item = scanner.nextLine();

        MarketItemEntity marketItem = client.getState().getMarketItemByName(item);

        if (marketItem == null) {
            System.out.println("\nCould not find product\n");
            return;
        }

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();



        PlayerEntity playerEntity = client.getState().getCurrentPlayer();
        InventoryComponent inventoryComponent = playerEntity.getComponent(InventoryComponent.class);
        int total = marketItem.getComponent(PriceComponent.class).price*quantity;
        WalletComponent wallet = playerEntity.getComponent(WalletComponent.class);
        Integer inventoryCount = inventoryComponent.inventory.get(marketItem.getComponent(NameComponent.class));
        if (inventoryCount == null) inventoryCount = 0;
        if (inventoryCount < quantity) {
            System.out.println("Insufficient Quantity to make sale");
            return;
        } else {
            inventoryComponent.inventory.put(marketItem.getComponent(NameComponent.class),inventoryCount-quantity);
            wallet.balance+=total;
        }



    }
}
