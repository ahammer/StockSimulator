package com.metalrain.stocksimulator.state.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class PriceComponent extends Component {
    /**
     * Price in pennies
     */
    public int price;

    public PriceComponent(int price) {
        this.price = price;
    }
}
