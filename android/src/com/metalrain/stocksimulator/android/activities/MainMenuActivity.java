package com.metalrain.stocksimulator.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.metalrain.stocksimulator.android.R;
import com.metalrain.stocksimulator.android.StockSimulator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Adam on 6/27/2015.
 */
public class MainMenuActivity extends Activity {
    @InjectView(R.id.Label)
    TextView Label;
    @InjectView(R.id.new_game_label)
    TextView newGameLabel;
    @InjectView(R.id.player_name)
    EditText playerName;
    @InjectView(R.id.random_seed)
    EditText randomSeed;
    @InjectView(R.id.game_length)
    Spinner gameLength;
    @InjectView(R.id.start_game)
    Button startGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        ButterKnife.inject(this);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), StockSimulatorActivity.class);
                startActivity(i);
            }
        });
    }
}
