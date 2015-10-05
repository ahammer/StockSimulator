package com.metalrain.stocksimulator.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    @InjectView(R.id.source_code)
    Button sourceCode;

    enum eGameLength {
        SHORT(1000), MEDIUM(5000), LONG(10000), XLONG(50000);
        public final int length;

        eGameLength(int length) {
            this.length = length;
        }

        @Override
        public String toString() {
            return name() + "\t(" + length + " Iterations)";
        }
    }

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
    @InjectView(R.id.resume_game)
    Button resumeGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        ButterKnife.inject(this);

        sourceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://github.com/ahammer/StockSimulator"));
                startActivity(i);
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(v);
            }
        });
        
        resumeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeGame(v);
            }
        });

        gameLength.setAdapter(new GameLengthAdapter());
    }

    private void resumeGame(View v) {
        Intent i = new Intent(v.getContext(), StockSimulatorActivity.class);
        startActivity(i);
    }

    private void startGame(View v) {
        int seed = (int) (Math.random() * 1000000);
        int iterations = ((eGameLength) gameLength.getSelectedItem()).length;

        try {
            String input_seed = randomSeed.getText().toString();
            if (!TextUtils.isEmpty(input_seed)) {
                seed = Integer.parseInt(input_seed);
            }
        } catch (Exception e) {
        }

        StockSimulator.initializeGameState(seed, iterations);
        Intent i = new Intent(v.getContext(), StockSimulatorActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeGame.setVisibility((StockSimulator.hasGameStarted()) ? View.VISIBLE : View.GONE);
    }

    private static class GameLengthAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return eGameLength.values().length;
        }

        @Override
        public Object getItem(int position) {
            return eGameLength.values()[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v = (TextView) convertView;
            if (v == null) v = new TextView(parent.getContext());
            v.setText(eGameLength.values()[position].toString());
            return v;
        }
    }
}
