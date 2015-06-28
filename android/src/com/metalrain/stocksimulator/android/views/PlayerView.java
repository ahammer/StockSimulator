package com.metalrain.stocksimulator.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.metalrain.stocksimulator.android.StockSimulator;
import com.metalrain.stocksimulator.android.R;
import com.metalrain.stocksimulator.state.GameState;
import com.metalrain.stocksimulator.state.components.WalletComponent;
import com.metalrain.stocksimulator.state.entities.PlayerEntity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Adam Hammer on 15-06-25.
 */
public class PlayerView extends FrameLayout {
    @InjectView(R.id.iteration)
    TextView iteration;
    @InjectView(R.id.speed)
    TextView speed;
    @InjectView(R.id.player_graph)
    EntityRendererView playerGraph;
    @InjectView(R.id.cash)
    TextView cash;

    public PlayerView(Context context) {
        super(context);
        inflate();
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate();
    }

    private void inflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.player_view, this);
        ButterKnife.inject(this);
        bindViews();
    }

    private void bindViews() {
        try {
            PlayerEntity p = StockSimulator.getGameState().getCurrentPlayer();
            playerGraph.setEntity(p);
            invalidate();
            speed.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    StockSimulator.getGameState().toggleGameSpeed();

                }
            });
            playerGraph.setDontRegisterMax(true);
            playerGraph.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    playerGraph.toggleView();
                }
            });
        } catch (Exception e) {
            //for layout editor
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        PlayerEntity p = StockSimulator.getGameState().getCurrentPlayer();
        if (p != null) {
            float wallet_amount = p.getComponent(WalletComponent.class).balance / 100f;

            cash.setText("$" + wallet_amount);
            iteration.setText((StockSimulator.getGameState().getIteration() - GameState.MARKET_WARMUP_ITERATIONS) + "/" + StockSimulator.getGameState().getMaxIntervals());
            speed.setText(StockSimulator.getGameState().getGameSpeed() + "x");

            playerGraph.invalidate();
        }

    }
}
