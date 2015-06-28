package com.metalrain.crimsim.state.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Adam Hammer on 15-06-22.
 */
public class MarketDemandComponent extends Component {
    public double supply = 0;
    public double demand = 0;
    public double supplyFalloff = 0.99;
    public double demandFalloff = 0.99;
}
