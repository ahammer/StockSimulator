package com.metalrain.stocksimulator.state.entities;

import com.badlogic.ashley.core.Entity;
import com.metalrain.stocksimulator.state.components.InventoryComponent;
import com.metalrain.stocksimulator.state.components.NameComponent;
import com.metalrain.stocksimulator.state.components.PriceHistoryComponent;
import com.metalrain.stocksimulator.state.components.RegistrationComponent;
import com.metalrain.stocksimulator.state.components.WalletComponent;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class PlayerEntity extends Entity {
    public PlayerEntity() {}

    public PlayerEntity(String name, String email, String password) {
        add(new NameComponent(name));
        add(new RegistrationComponent(email, password));
        add(new InventoryComponent());
        add(new WalletComponent());
        add(new PriceHistoryComponent());
    }
}
