package com.metalrain.crimsim.state.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.metalrain.crimsim.state.components.InventoryComponent;
import com.metalrain.crimsim.state.components.NameComponent;
import com.metalrain.crimsim.state.components.PriceHistoryComponent;
import com.metalrain.crimsim.state.components.RegistrationComponent;
import com.metalrain.crimsim.state.components.WalletComponent;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class PlayerEntity extends Entity {
    public PlayerEntity(String name, String email, String password) {
        add(new NameComponent(name));
        add(new RegistrationComponent(email, password));
        add(new InventoryComponent());
        add(new WalletComponent());
        add(new PriceHistoryComponent());
    }
}
