package com.metalrain.stocksimulator.state.entities;

import com.badlogic.ashley.core.Entity;
import com.metalrain.stocksimulator.state.components.IterationComponent;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class MarketEntity extends Entity {
    public MarketEntity() {
        add(new IterationComponent());
    }


}
