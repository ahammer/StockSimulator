package com.metalrain.stocksimulator.android.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.metalrain.stocksimulator.android.StockSimulator;
import com.metalrain.stocksimulator.state.components.StockImpactEventComponent;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by octopoco on 7/26/2015.
 */
public class RandomEventView extends TextView {
    Context context;

    private Subscription subscription;

    public RandomEventView(Context context){
        super(context);
        this.context=context;
        setText("test");
    }
    public RandomEventView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        this.context=context;
        setText("test");
    }
    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();

        subscription=StockSimulator.getGameState().bus.toObserverable().subscribe(new Action1<Object>(){

            @Override
            public void call(final Object o){
                if(o instanceof StockImpactEventComponent) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            setText(o.toString());
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        subscription.unsubscribe();
    }



}
