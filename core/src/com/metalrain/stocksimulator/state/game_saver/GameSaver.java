package com.metalrain.stocksimulator.state.game_saver;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.metalrain.stocksimulator.state.GameState;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameSaver {
    private final GameState gameState;

    public GameSaver(GameState gameState) {
        this.gameState = gameState;
    }

    public String saveToJson() {
        gameState.stopThread();
        try {
            ImmutableArray<Entity> entities = gameState.getEntityEngine().getEntities();
            SavedGame savedGame = new SavedGame();
            for (Entity entity : entities) {
                List<ComponentContainer> componentContainers = new ArrayList<ComponentContainer>();
                for (Component c : entity.getComponents()) {
                    componentContainers.add(new ComponentContainer(c));
                }
                EntityContainer entityContainer = new EntityContainer(entity.getId(), entity.getClass().getName(), componentContainers);
                savedGame.entitiesContainers.add(entityContainer);

            }

            return new GsonBuilder().create().toJson(savedGame);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameState.startThread();
        return "Failed";
    }

    public void loadFromJson(String json) {
        gameState.stopThread();
        synchronized (gameState.getEntityEngine()) {
            try {
                //Custom Deserialized to Convert Component to it's expected Component subclass
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(ComponentContainer.class, new JsonDeserializer<ComponentContainer>() {
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

                SavedGame sg = gson.fromJson(json, SavedGame.class);

                gameState.getEntityEngine().removeAllEntities();
                for (EntityContainer ec : sg.entitiesContainers) {
                    Class c = Class.forName(ec.clazz);
                    //System.out.println("Adding entity: "+c.getName());
                    Entity e = (Entity) c.newInstance();
                    for (ComponentContainer component : ec.components) {
                        e.add(component.value);
                        //   System.out.println("Adding Component: "+component.value.getClass().getName());
                    }
                    gameState.getEntityEngine().addEntity(e);
                }
                System.out.println(sg.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            gameState.startThread();
        }
    }

    static class ComponentContainer {
        String clazz;
        Component value;

        public ComponentContainer(Component c) {
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
        List<EntityContainer> entitiesContainers = new ArrayList<EntityContainer>();

        @Override
        public String toString() {
            return "SavedGame{" +
                    "entitiesContainers=" + entitiesContainers +
                    '}';
        }
    }
}