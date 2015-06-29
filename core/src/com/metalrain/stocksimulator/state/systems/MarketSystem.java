package com.metalrain.stocksimulator.state.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.metalrain.stocksimulator.state.RxBus;
import com.metalrain.stocksimulator.state.components.GameStateComponent;
import com.metalrain.stocksimulator.state.components.InventoryComponent;
import com.metalrain.stocksimulator.state.components.MarketDemandComponent;
import com.metalrain.stocksimulator.state.components.MarketItemComponent;
import com.metalrain.stocksimulator.state.components.MarketVolatilityComponent;
import com.metalrain.stocksimulator.state.components.NameComponent;
import com.metalrain.stocksimulator.state.components.PriceComponent;
import com.metalrain.stocksimulator.state.components.PriceHistoryComponent;
import com.metalrain.stocksimulator.state.components.StockImpactEventComponent;
import com.metalrain.stocksimulator.state.components.WalletComponent;

import java.util.Random;


/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class MarketSystem extends IntervalSystem {
    private final RxBus bus;
    private final Random random;
    private final int maxIntevals;

    public int getMaxIntevals() {
        return maxIntevals;
    }

    int interval = 0;
    private Family gameStateFamily;
    private Family marketItemFamily;
    private Family stockImpactEventComponent;
    private Family playerFamily;
    private Engine engine;

    public int getInterval() {
        return interval;
    }

    public MarketSystem(RxBus bus, int seed, int max_intervals) {
        super(1f);
        random = new Random(seed);
        this.bus = bus;
        this.maxIntevals = max_intervals;
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        this.marketItemFamily = Family.all(MarketItemComponent.class, MarketVolatilityComponent.class, PriceComponent.class, PriceHistoryComponent.class).get();
        playerFamily = Family.all(WalletComponent.class, NameComponent.class, InventoryComponent.class).get();

        this.gameStateFamily = Family.all(GameStateComponent.class).get();
        this.stockImpactEventComponent = Family.all(StockImpactEventComponent.class).get();
    }


    @Override
    public void updateInterval() {
        if (interval >= maxIntevals) return;
        interval++;
        StockImpactEventComponent component = injectMarketEvents();
        performMarketCalculations(component);
        bus.post(new MarketUpdatedMessage());
        if (interval == maxIntevals)bus.post(new TimesUpMessage());

    }

    private void performMarketCalculations(StockImpactEventComponent current_event) {
        try {

            Entity gameStateEntity = engine.getEntitiesFor(gameStateFamily).first();
            GameStateComponent gameStateComponent = gameStateEntity.getComponent(GameStateComponent.class);
            gameStateComponent.market_interval++;

            //Grab a list of
            ImmutableArray<Entity> entities = engine.getEntitiesFor(marketItemFamily);


            for (Entity e : entities) {
                PriceComponent pc = e.getComponent(PriceComponent.class);
                NameComponent nc = e.getComponent(NameComponent.class);
                MarketVolatilityComponent volatilityComponent = e.getComponent(MarketVolatilityComponent.class);
                MarketDemandComponent demandComponent = e.getComponent(MarketDemandComponent.class);
                PriceHistoryComponent priceHistoryComponent = e.getComponent(PriceHistoryComponent.class);
                priceHistoryComponent.registerPrice(pc, gameStateComponent.market_interval);


                if (current_event != null && current_event.appliesTo(nc.name)) current_event.apply(nc, demandComponent);

                //Clamp price in range
                if (pc.price < volatilityComponent.min_price) {
                    demandComponent.demand = demandComponent.supply + 1;
                    pc.price = volatilityComponent.min_price;
                }

                //Market Crash!!
                if (random.nextDouble() < (volatilityComponent.volatility/10000f))
                { demandComponent.demand = -(random.nextDouble()*volatilityComponent.volatility * 500); }

                //Regular market activity
                int delta = (int) (random.nextDouble() * ((demandComponent.demand - demandComponent.supply)/50f) * (volatilityComponent.volatility));
                pc.price += delta;

                demandComponent.demand *= demandComponent.demandFalloff;
                demandComponent.supply *= demandComponent.supplyFalloff;

                //Bonus demand to keep things looking on up
                demandComponent.demand += 1;
            }

            updatePlayerValues(entities);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void updatePlayerValues(ImmutableArray<Entity> entities) {
        ImmutableArray<Entity> players = engine.getEntitiesFor(playerFamily);
        for (Entity p:players) {
            int wallet_amount = p.getComponent(WalletComponent.class).balance;
            int investment_amount = p.getComponent(InventoryComponent.class).getInventoryValue(entities);
            p.getComponent(PriceHistoryComponent.class).registerPrice(new PriceComponent(investment_amount+wallet_amount),interval);
        }


    }

    private StockImpactEventComponent generateRandom() {
        if (random.nextBoolean()) {
            return new StockImpactEventComponent("Demand UP!", interval, new String[]{"a", "b", "c", "d", "e", "f", "g"},
                    new int[]{0, 0, 0, 0, 0, 0, 0},
                    new int[]{
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200)}
            );
        } else {
            return new StockImpactEventComponent("Supply Changed", interval, new String[]{"a", "b", "c", "d", "e", "f", "g"},
                    new int[]{
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200),
                            (random.nextBoolean()?1:-1)*random.nextInt(200)},
                    new int[]{0, 0, 0, 0, 0, 0, 0}
            );

        }
    }


    private StockImpactEventComponent injectMarketEvents() {
        if (random.nextDouble() > 0.1f) return null;
        Entity e = new Entity();
        StockImpactEventComponent impact = generateRandom();
        e.add(impact);
        engine.addEntity(e);
        bus.post(impact);
        return impact;
    }

}
