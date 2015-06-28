package com.metalrain.stocksimulator.state.entities;

import com.badlogic.ashley.core.Entity;
import com.metalrain.stocksimulator.state.components.GameStateComponent;
import com.metalrain.stocksimulator.state.components.IterationComponent;
import com.metalrain.stocksimulator.state.components.TimerComponent;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class GameStateEntity extends Entity {
    public GameStateEntity() {
        add(new TimerComponent());
        add(new IterationComponent());
        add(new GameStateComponent());
    }
}
