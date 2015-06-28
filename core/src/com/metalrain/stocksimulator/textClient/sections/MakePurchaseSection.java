package com.metalrain.stocksimulator.textClient.sections;

import com.badlogic.ashley.core.Family;
import com.metalrain.stocksimulator.state.components.InventoryComponent;
import com.metalrain.stocksimulator.state.components.MarketItemComponent;
import com.metalrain.stocksimulator.state.components.NameComponent;
import com.metalrain.stocksimulator.state.components.PriceComponent;
import com.metalrain.stocksimulator.state.components.WalletComponent;
import com.metalrain.stocksimulator.state.entities.MarketItemEntity;
import com.metalrain.stocksimulator.state.entities.PlayerEntity;
import com.metalrain.stocksimulator.textClient.ITextClientSection;
import com.metalrain.stocksimulator.textClient.TextClient;

import java.util.Scanner;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class MakePurchaseSection implements ITextClientSection {

    private final TextClient client;

    public MakePurchaseSection(TextClient textClient) {

        this.client = textClient;
        Family marketItems = Family.all(MarketItemComponent.class, NameComponent.class).get();
    }

    @Override
    public String getName() {
        return "Buy";
    }

    @Override
    public String getDescription() {
        return "Buy something illicit";
    }

    @Override
    public void invoke(String command, Scanner scanner) {
        client.getState().printMarketPrices();
        System.out.println("What do you want to buy: ");

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
        int total = marketItem.getComponent(PriceComponent.class).price*quantity;
        WalletComponent wallet = playerEntity.getComponent(WalletComponent.class);
        if (total > wallet.balance) {
            System.out.println("Not enough balance to do this. "+(total-wallet.balance) + " Short\n");
            return;
        } else {
            wallet.balance -= total;
            InventoryComponent inventoryComponent = playerEntity.getComponent(InventoryComponent.class);
            Integer orig_quantity = inventoryComponent.inventory.get(marketItem.getComponent(NameComponent.class));
            if (orig_quantity == null) orig_quantity = 0;
            inventoryComponent.inventory.put(marketItem.getComponent(NameComponent.class),orig_quantity+quantity);
        }


    }
}
