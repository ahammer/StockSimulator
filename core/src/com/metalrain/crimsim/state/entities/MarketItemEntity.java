package com.metalrain.crimsim.state.entities;

import com.badlogic.ashley.core.Entity;
import com.metalrain.crimsim.state.components.MarketDemandComponent;
import com.metalrain.crimsim.state.components.MarketItemComponent;
import com.metalrain.crimsim.state.components.MarketVolatilityComponent;
import com.metalrain.crimsim.state.components.NameComponent;
import com.metalrain.crimsim.state.components.PriceComponent;
import com.metalrain.crimsim.state.components.PriceHistoryComponent;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class MarketItemEntity extends Entity {
    public MarketItemEntity(String name, int initialPrice, int minPrice, int volatility) {
        add(new NameComponent(name));
        add(new PriceComponent(initialPrice));
        add(new MarketItemComponent());
        add(new PriceHistoryComponent());
        add(new MarketVolatilityComponent(minPrice, volatility));
        add(new MarketDemandComponent());
    }
}
