package com.metalrain.stocksimulator.state;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.metalrain.stocksimulator.state.components.InventoryComponent;
import com.metalrain.stocksimulator.state.components.MarketItemComponent;
import com.metalrain.stocksimulator.state.components.NameComponent;
import com.metalrain.stocksimulator.state.components.PriceComponent;
import com.metalrain.stocksimulator.state.components.PriceHistoryComponent;
import com.metalrain.stocksimulator.state.components.WalletComponent;
import com.metalrain.stocksimulator.state.entities.GameStateEntity;
import com.metalrain.stocksimulator.state.entities.MarketItemEntity;
import com.metalrain.stocksimulator.state.entities.PlayerEntity;
import com.metalrain.stocksimulator.state.systems.MarketSystem;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class GameState {
    public final static int MARKET_WARMUP_ITERATIONS = 4000;
    public RxBus bus = new RxBus();
    private final MarketSystem system;
    Engine entityEngine = new Engine();
    long last_time = System.currentTimeMillis();
    Executor executorService = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;
    private int gameSpeed = 1;

    public GameState(int seed, int max_iterations) {
        entityEngine.addEntity(new PlayerEntity("Player", "adamhammer2@gmail.com", "test"));
        entityEngine.addEntity(new MarketItemEntity("a",
                500,
                100,
                1));
        entityEngine.addEntity(new MarketItemEntity("b",
                500,
                100,
                2));
        entityEngine.addEntity(new MarketItemEntity("c",
                500,
                100,
                3));

        entityEngine.addEntity(new MarketItemEntity("d",
                500,
                100,
                4));

        entityEngine.addEntity(new MarketItemEntity("e",
                500,
                100,
                5));

        entityEngine.addEntity(new MarketItemEntity("f",
                500,
                100,
                6));


        entityEngine.addEntity(new GameStateEntity());
        entityEngine.addSystem(system = new MarketSystem(bus, seed,max_iterations+MARKET_WARMUP_ITERATIONS));
        entityEngine.update(MARKET_WARMUP_ITERATIONS);
    }

    public void serialize() {
        for (Entity e : entityEngine.getEntities()) {

        }
    }
    private void iterate() {
        long current_time = System.currentTimeMillis();
        long delta_time = current_time - last_time;
        last_time = current_time;
        delta_time *= gameSpeed;
        entityEngine.update(delta_time/1000f);

    }

    public void printMarketPrices() {
        Family market = Family.all(MarketItemComponent.class, PriceComponent.class, NameComponent.class).get();
        ImmutableArray<Entity> entities = entityEngine.getEntitiesFor(market);
        System.out.println("Current Prices: ");
        for (Entity e:entities) {
            PriceComponent p = e.getComponent(PriceComponent.class);
            NameComponent n = e.getComponent(NameComponent.class);
            System.out.println("Name: "+n.name + " Price: "+p.price);
        }
    }

    public void startThread() {
        running = true;
        last_time = System.currentTimeMillis(); //Always reset the timer
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    iterate();

                }
            }
        });
    }

    public void stopThread() {
        running = false;
    }

    public void printUserData() {
        Family market = Family.all(MarketItemComponent.class, PriceComponent.class, NameComponent.class, PriceHistoryComponent.class).get();
        ImmutableArray<Entity> marketItemEntities = entityEngine.getEntitiesFor(market);

        Family players = Family.all(WalletComponent.class, NameComponent.class, InventoryComponent.class).get();
        for (Entity player:entityEngine.getEntitiesFor(players)) {
            NameComponent nc = player.getComponent(NameComponent.class);
            WalletComponent wc = player.getComponent(WalletComponent.class);
            InventoryComponent ic = player.getComponent(InventoryComponent.class);
            System.out.println("Name: "+nc.name);
            ic.printInventory(marketItemEntities);
            long value = ic.getInventoryValue(marketItemEntities);
            System.out.println("Inventory Value: $"+value);
            System.out.println(" Wallet Balance: $"+wc.balance);
            System.out.println("    Total Value: $"+(value+wc.balance));
        }
    }

    public void printMarketHistory() {
        Family market = Family.all(MarketItemComponent.class, PriceComponent.class, NameComponent.class, PriceHistoryComponent.class).get();
        ImmutableArray<Entity> entities = entityEngine.getEntitiesFor(market);

        System.out.println("Current Prices: ");
        for (Entity e:entities) {
            PriceComponent p = e.getComponent(PriceComponent.class);
            NameComponent n = e.getComponent(NameComponent.class);
            PriceHistoryComponent history = e.getComponent(PriceHistoryComponent.class);

            Iterator itr;

            System.out.print(n.name + " Resolution 1 \n");
            itr = history.getPriceHistory().iterator();
            while (itr.hasNext()) System.out.print((Integer) itr.next() + "\n");
            System.out.print(n.name + " Resolution 5\n");
            itr = history.getPriceHistorySkipFive().iterator();
            while (itr.hasNext()) System.out.print((Integer) itr.next() + "\n");

            System.out.print(n.name + " Resolution 15\n");
            itr = history.getPriceHistorySkipFifteen().iterator();
            while (itr.hasNext()) System.out.print((Integer) itr.next() + "\n");

        }

    }

    public PlayerEntity getCurrentPlayer() {
        Family players = Family.all(WalletComponent.class, NameComponent.class, InventoryComponent.class).get();
        return (PlayerEntity) entityEngine.getEntitiesFor(players).first();
    }

    public MarketItemEntity getMarketItemByName(String item) {
        Family marketItems = Family.all(MarketItemComponent.class, NameComponent.class).get();
        for (Entity e:entityEngine.getEntitiesFor(marketItems)) {
            if (e.getComponent(NameComponent.class).name.equalsIgnoreCase(item)) {
                return (MarketItemEntity) e;
            }
        }
        return null;

    }

    public List<MarketItemEntity> getMarketItems() {

        ArrayList marketItems = new ArrayList<MarketItemEntity>();
        Family marketItemFamily = Family.all(MarketItemComponent.class).get();
        for (Entity e:entityEngine.getEntitiesFor(marketItemFamily)) {
            if (e instanceof  MarketItemEntity) marketItems.add(e);
        }
        return marketItems;


    }


    public ImmutableArray<Entity> getMarketItemsRaw() {

        ArrayList marketItems = new ArrayList<MarketItemEntity>();
        Family marketItemFamily = Family.all(MarketItemComponent.class).get();
        return entityEngine.getEntitiesFor(marketItemFamily);


    }


    public int getIteration() {
            return system.getInterval();
    }

    public long getInventoryCount(MarketItemEntity item) {
        return getCurrentPlayer()
                .getComponent(InventoryComponent.class)
                .getInventoryQuantity(item.getComponent(NameComponent.class));
    }

    public int getGameSpeed() {
        return gameSpeed;
    }

    public void toggleGameSpeed() {
        gameSpeed *= 2;
        if (gameSpeed > 32) {
            gameSpeed = 1;
        }

    }

    public int getMaxIntervals() {
        return system.getMaxIntevals() - MARKET_WARMUP_ITERATIONS;
    }


    static class ComponentContainer {
        String clazz;
        Component value;

        public ComponentContainer(com.badlogic.ashley.core.Component c) {
            clazz = c.getClass().getName();
            value = c;
        }

        @Override
        public String toString() {
            return "ComponentContainer{" +
                    "clazz='" + clazz + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    static class EntityContainer {
        EntityContainer(long id, String clazz, List<ComponentContainer> components) {
            this.id = id;
            this.clazz = clazz;
            this.components = components;
        }

        long id;
        String clazz;
        List<ComponentContainer> components;

        @Override
        public String toString() {
            return "EntityContainer{" +
                    "id=" + id +
                    ", clazz='" + clazz + '\'' +
                    ", components=" + components +
                    '}';
        }
    }

    static class SavedGame {
        List<EntityContainer> entitiesContainers = new ArrayList<>();

        @Override
        public String toString() {
            return "SavedGame{" +
                    "entitiesContainers=" + entitiesContainers +
                    '}';
        }
    }
    public String saveToJson() {
        try {
            ImmutableArray<Entity> entities = entityEngine.getEntities();
            SavedGame savedGame = new SavedGame();
            for (Entity entity:entities) {
                List<ComponentContainer> componentContainers = new ArrayList<>();
                for (Component c:entity.getComponents()) {
                    componentContainers.add(new ComponentContainer(c));
                }
                EntityContainer entityContainer = new EntityContainer(entity.getId(), entity.getClass().getName(), componentContainers);
                savedGame.entitiesContainers.add(entityContainer);

            }

            return new GsonBuilder().create().toJson(savedGame);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed";
    }

    public void loadFromJson(String json) {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(ComponentContainer.class, new JsonDeserializer<ComponentContainer>() {
                @Override
                public ComponentContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        String clazz = json.getAsJsonObject().get("clazz").getAsString();

                        try {
                            Class c = Class.forName(clazz);
                            return new ComponentContainer((Component) context.deserialize(json.getAsJsonObject().get("value"), c));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        return null;

                }
            }).create();
            SavedGame sg;

            sg = gson.fromJson(json, SavedGame.class);
            System.out.println(sg.toString());
                       
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
