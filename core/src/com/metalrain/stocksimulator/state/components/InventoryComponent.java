package com.metalrain.stocksimulator.state.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class InventoryComponent extends Component {
    public final Map<NameComponent, Integer> inventory = new HashMap<NameComponent, Integer>();

    public void printInventory(ImmutableArray<Entity> marketItems) {
        for (Entity e:marketItems) {
            NameComponent nc = e.getComponent(NameComponent.class);
            if (inventory.containsKey(nc)) {
                PriceComponent pc = e.getComponent(PriceComponent.class);
                System.out.println(nc.name + " : "+inventory.get(nc) + " = $" + inventory.get(nc)*pc.price);
            }
        }
    }


    public int getInventoryValue(ImmutableArray<Entity> marketItems) {
        int value = 0;
        for (Entity e:marketItems) {
            NameComponent nc = e.getComponent(NameComponent.class);
            if (inventory.containsKey(nc)) {
                PriceComponent pc = e.getComponent(PriceComponent.class);
                value+= inventory.get(nc)*pc.price;
            }
        }
        return value;
    }

    public long getInventoryQuantity(NameComponent nc) {
        if (inventory.containsKey(nc))
            return inventory.get(nc);
        else return 0;
    }



}
