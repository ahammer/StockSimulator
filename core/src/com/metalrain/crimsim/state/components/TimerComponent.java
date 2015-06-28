package com.metalrain.crimsim.state.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class TimerComponent  extends Component {
    long timer_in_millis = 0l;
    boolean paused = false;
}
