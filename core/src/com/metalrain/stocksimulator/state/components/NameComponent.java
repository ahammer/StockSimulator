package com.metalrain.stocksimulator.state.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class NameComponent extends Component {
    public String name;

    public NameComponent() {}

    public NameComponent(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameComponent that = (NameComponent) o;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NameComponent{" +
                "name='" + name + '\'' +
                '}';
    }
}
