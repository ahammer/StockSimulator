package com.metalrain.crimsim.state.entities;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.metalrain.crimsim.state.components.GameStateComponent;
import com.metalrain.crimsim.state.components.IterationComponent;
import com.metalrain.crimsim.state.components.TimerComponent;

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
