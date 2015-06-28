package com.metalrain.stocksimulator.android.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.metalrain.stocksimulator.android.views.MarketItemView;
import com.metalrain.stocksimulator.state.entities.MarketItemEntity;

import java.util.List;

/**
* Created by Adam on 6/27/2015.
*/
public class MarketItemsListAdapter extends BaseAdapter {
    private final List<MarketItemEntity> marketList;

    public MarketItemsListAdapter(List<MarketItemEntity> marketList) {
        this.marketList = marketList;
    }

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
}
