package com.metalrain.stocksimulator.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.badlogic.ashley.core.Entity;
import com.metalrain.stocksimulator.android.StockSimulator;
import com.metalrain.stocksimulator.state.components.InventoryComponent;
import com.metalrain.stocksimulator.state.components.NameComponent;
import com.metalrain.stocksimulator.state.components.PriceComponent;
import com.metalrain.stocksimulator.state.components.PriceHistoryComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Draws the history graph
 * Created by Adam Hammer on 15-06-24.
 */
public class EntityRendererView extends View {
    private PriceHistoryComponent marketHistory;
    private NameComponent name;
    private PriceComponent price;
    private boolean dontRegisterMax = false;
    public static volatile int GLOBAL_MAX_Y=0;


    enum mode {one, five, fifteen, onehundred;
        public String value() {
            switch (this) {
                case one: return "R:1";
                case five: return "R:5";
                case fifteen: return "R:15";
                case onehundred: return "ALL";
            }
            return "";
        };
    }

    public mode currentMode = mode.five;
    public EntityRendererView(Context context) {
        super(context);

    }

    public EntityRendererView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void toggleView() {
        switch (currentMode) {
            case one: currentMode = mode.five; break;
            case five: currentMode = mode.fifteen; break;
            case fifteen: currentMode = mode.onehundred; break;
            case onehundred: currentMode = mode.one; break;
        }
        invalidate();
    }



    Paint p = new Paint();




    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        p.setColor(Color.DKGRAY);
        canvas.drawRect(0,0,getWidth(),getHeight(),p);


        if (marketHistory != null) {
            List<Integer> list;
            switch (currentMode) {
                case one: list = marketHistory.getPriceHistory(); break;
                case five: list = marketHistory.getPriceHistorySkipFive(); break;
                case fifteen: list = marketHistory.getPriceHistorySkipFifteen(); break;
                case onehundred: list = marketHistory.getPriceHistorySkipOneHundred(); break;
                default:
                    list = marketHistory.getPriceHistory();
            }
            boolean up = (list.get(list.size()-1).doubleValue() - list.get(0).doubleValue()) > 0;



            if (list.size() == 0) return;
            int min_x = 0;
            int max_x = list.size();

            int min_y = list.get(0);
            int max_y = list.get(0);
            for (Integer i : list) {
                if (i < min_y) min_y = i;
                if (i > max_y) max_y = i;
            }

            if (!dontRegisterMax) {
                if (GLOBAL_MAX_Y < max_y) GLOBAL_MAX_Y = max_y;
            }
            //Setup the view bounds
            if (currentMode == mode.onehundred) {
                max_y = GLOBAL_MAX_Y;
                min_y = 0;
            } else {
                max_y += 1000;
                min_y -= 1000;
                if (min_y < 0) min_y = 0;
            }


            drawGraph(canvas, list, max_x, min_y, max_y);
            drawTextOverlay(canvas, min_y, max_y,up);

        }

    }

    public void setDontRegisterMax(boolean dontRegisterMax) {
        this.dontRegisterMax = dontRegisterMax;
    }

    private void drawTextOverlay(Canvas canvas, int min_y, int max_y, boolean up) {
        p.setColor(Color.argb(200,200,255,200));
        p.setTextSize(30f);
        canvas.drawText(String.valueOf(max_y / 100f), getWidth() - 100, 35, p);
        canvas.drawText(String.valueOf(min_y / 100f), getWidth() - 100, getHeight() - 10, p);
        canvas.drawText(currentMode.value(), getWidth() - 200, 35, p);

        p.setColor(Color.argb(200,200,200,255));
        float height_multiplier = 0.35f;

        p.setTextSize(getHeight()*height_multiplier);
        float name_length = p.measureText(name.name);
        canvas.drawText(name.name, 10,getHeight()-10,p);
        if (price != null) {

            if (up)
                p.setColor(Color.argb(128, 0, 255, 0));
            else {
                p.setColor(Color.argb(128, 255, 0, 0));
            }
            canvas.drawText(String.valueOf(price.price / 100f), 50 + name_length, getHeight() - 10, p);
        }



        long count = StockSimulator.getGameState().getCurrentPlayer().getComponent(InventoryComponent.class).getInventoryQuantity(name);
        if (count!=0) {
            p.setColor(Color.argb(255,50,25,25));
            canvas.drawText("Quantity: "+String.valueOf(count), 10, getHeight() * height_multiplier, p);
        }
    }

    private void drawGraph(Canvas canvas, List<Integer> list, float max_x, int min_y, int max_y) {
        p.setColor(Color.GRAY);
        p.setStrokeWidth(5.0f);
        Float lx = null, ly = null;
        List<Integer> local = new ArrayList(list);//Shallow copy
        for (int i = 0; i < list.size(); i++) {

            float px = i / max_x;
            float py = 1-((list.get(i) - min_y) / (float) (max_y - min_y));
            float tx = px * getWidth();
            float ty = py * getHeight();
            if (lx != null) {
                canvas.drawLine(tx, ty, lx, ly, p);
            }
            lx = tx;
            ly = ty;
        }
    }

    public void setEntity(Entity itemEntity) {
        this.marketHistory = itemEntity.getComponent(PriceHistoryComponent.class);
        this.name = itemEntity.getComponent(NameComponent.class);
        this.price = itemEntity.getComponent(PriceComponent.class);
        invalidate();
    }



    public PriceHistoryComponent getMarketHistory() {
        return marketHistory;
    }

}
