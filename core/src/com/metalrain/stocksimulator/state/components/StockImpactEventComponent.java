package com.metalrain.stocksimulator.state.components;

import com.badlogic.ashley.core.Component;

import java.util.Random;

import javax.swing.text.html.parser.Entity;

/**
 * Created by Adam Hammer on 15-06-25.
 */
public class StockImpactEventComponent extends Component{
    public final String message;
    public final int iteration;
    private final int[] demand_impact;
    private final String[] market_names;
    private final int[] supply_impact;
    ///new vars
    private String toString;
    String demandViewData="\nDemand:   ";
    String supplyViewData="Supply:   ";

    public StockImpactEventComponent(String message, int iteration, String[] market_names, int[] supply_impact, int[] demand_impact) {
        this.message = message;
        this.iteration = iteration;
        this.market_names = market_names;
        this.supply_impact = supply_impact;
        this.demand_impact = demand_impact;
        assert (market_names.length == supply_impact.length);
        assert (market_names.length == demand_impact.length);

    }


    public boolean appliesTo(String name) {
        for (String market_name:market_names) {
            if (market_name.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public void apply(NameComponent nc, MarketDemandComponent demandComponent) {
        int index = 0;
        String supplyHolder="";
        String demandHolder="";

        for (String market_name:market_names) {
            if (market_name.equalsIgnoreCase(nc.name)) {
                demandComponent.demand += demand_impact[index];
                demandComponent.supply += supply_impact[index];
                demandHolder=(" "+market_name+":"+String.valueOf(demand_impact[index]));
                demandViewData+=demandHolder;
                supplyHolder=(" "+market_name+":"+String.valueOf(supply_impact[index]));
                supplyViewData+=demandHolder;

            }
            index++;

        }
        toString=supplyViewData+demandViewData;

    }
    @Override
    public String toString(){
        return toString;
    }


}
