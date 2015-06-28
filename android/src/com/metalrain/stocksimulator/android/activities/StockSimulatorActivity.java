package com.metalrain.stocksimulator.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;


import com.metalrain.stocksimulator.android.R;
import com.metalrain.stocksimulator.android.StockSimulator;

import com.metalrain.stocksimulator.android.views.MarketItemView;
import com.metalrain.stocksimulator.android.views.OrderView;
import com.metalrain.stocksimulator.android.views.PlayerView;
import com.metalrain.stocksimulator.state.entities.MarketItemEntity;
import com.metalrain.stocksimulator.state.systems.MarketUpdatedMessage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Adam Hammer on 15-06-24.
 */
public class StockSimulatorActivity extends Activity {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    @InjectView(R.id.market_list)
    ListView marketListView;
    @InjectView(R.id.player_view)
    PlayerView playerView;
    @InjectView(R.id.order_view)
    OrderView orderView;

    private volatile boolean updateUI = true;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crimsim_activity);
        ButterKnife.inject(this);
        marketListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == SCROLL_STATE_IDLE) updateUI = true;
                else updateUI = false;
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });


        final List<MarketItemEntity> marketList = StockSimulator.getGameState().getMarketItems();
        orderView.setMarketItem(marketList.get(0));

        marketListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return marketList.size();
            }

            @Override
            public Object getItem(int i) {
                return marketList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
               if (view == null) {
                   view = new MarketItemView(viewGroup.getContext());
               }
                ((MarketItemView) view).setMarketItemEntity((MarketItemEntity) getItem(i));

                return view;
            }
        });
        marketListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MarketItemEntity e = (MarketItemEntity) adapterView.getAdapter().getItem(i);
                if (view instanceof  MarketItemView) {
                    if (orderView.getMarketItem() == e) {
                        ((MarketItemView) view).marketHistoryView.toggleView();
                    }
                }

                orderView.setMarketItem(e);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        StockSimulator.getGameState().startThread();
        subscription = StockSimulator.getGameState().bus.toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(final Object o) {
                if (o instanceof MarketUpdatedMessage) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (updateUI)
                                ((BaseAdapter) marketListView.getAdapter()).notifyDataSetChanged();
                            playerView.invalidate();
                            orderView.invalidate();
                        }
                    });
                }

            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        StockSimulator.getGameState().stopThread();
        subscription.unsubscribe();

    }
}
