package com.metalrain.crimsim.state.entities;

import com.badlogic.ashley.core.Entity;
import com.metalrain.crimsim.state.components.IterationComponent;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class MarketEntity extends Entity {
    public MarketEntity() {
        add(new IterationComponent());
    }


}
