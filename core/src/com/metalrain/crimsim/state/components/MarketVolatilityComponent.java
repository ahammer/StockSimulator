package com.metalrain.crimsim.state.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class MarketVolatilityComponent extends Component{
    public final int min_price;
    public final int volatility;

    public MarketVolatilityComponent(int min_price, int volatility) {
        this.min_price = min_price;
        this.volatility = volatility;

    }
}
