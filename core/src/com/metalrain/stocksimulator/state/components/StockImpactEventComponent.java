package com.metalrain.stocksimulator.state.components;

import com.badlogic.ashley.core.Component;
import com.metalrain.stocksimulator.state.GameState;

import java.util.Random;

import javax.swing.text.html.parser.Entity;

import sun.rmi.runtime.Log;


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


    String toString="nope";
    private static int integer=1;
    private static String displayData="";

    public static String name1;
    public static String name2;
    public static String name3;
    public static String name4;
    public static String name5;

    public StockImpactEventComponent(String message, int iteration, String[] market_names, int[] supply_impact, int[] demand_impact) {

        integer++;
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
    public  void setNames(String name1, String name2,String name3,String name4, String name5){
        this.name1=name1;
        this.name2=name2;
        this.name3=name3;
        this.name4=name4;
        this.name5=name5;

    }

    public void apply(NameComponent nc, MarketDemandComponent demandComponent) {
        int index = 0;
        String supplyHolder="";
        String demandHolder="";
        String demandViewData="\nDemand:   ";
        String supplyViewData="Supply:   ";
        int counter=0;

        for (String market_name:market_names) {

            demandComponent.demand += demand_impact[index];
            demandComponent.supply += supply_impact[index];

            demandHolder=(" "+market_name+":"+String.valueOf(demand_impact[index]));
            demandViewData+=demandHolder;
            supplyHolder=(" "+market_name+":"+String.valueOf(supply_impact[index]));
            supplyViewData+=supplyHolder;

            System.out.println(String.valueOf(counter));
            counter++;



            index++;

        }

        displayData=supplyViewData+demandViewData;

        integer++;

    }
    @Override
    public String toString(){

        return displayData;
    }


}
