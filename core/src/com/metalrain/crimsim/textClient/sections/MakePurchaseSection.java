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

import javax.xml.soap.Text;

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
