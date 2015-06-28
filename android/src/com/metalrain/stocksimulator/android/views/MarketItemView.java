package com.metalrain.stocksimulator.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.metalrain.stocksimulator.android.R;
import com.metalrain.stocksimulator.state.entities.MarketItemEntity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Adam Hammer on 15-06-24.
 */
public class MarketItemView extends FrameLayout {
    private MarketItemEntity item;

    @InjectView(R.id.price_chart)
    public
    EntityRendererView marketHistoryView;

    public MarketItemView(Context context) {
        super(context);
        inflate();
    }

    public MarketItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate();
    }

    private void inflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item_market_item, this);
        ButterKnife.inject(this);
    }

    public void setMarketItemEntity(MarketItemEntity item) {
        this.item = item;
        bindItem();

    }

    private void bindItem() {
        if (item != null) {
            marketHistoryView.setEntity(item);
        }


    }
}
