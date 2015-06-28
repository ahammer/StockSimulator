package com.metalrain.stocksimulator.state.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Adam Hammer on 6/22/2015.
 */
public class RegistrationComponent extends Component {
    public final String email;
    public final String password;

    public RegistrationComponent(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
