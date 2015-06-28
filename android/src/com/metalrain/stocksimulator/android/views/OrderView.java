package com.metalrain.stocksimulator.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.metalrain.stocksimulator.android.StockSimulator;
import com.metalrain.stocksimulator.android.R;
import com.metalrain.stocksimulator.state.components.InventoryComponent;
import com.metalrain.stocksimulator.state.components.MarketVolatilityComponent;
import com.metalrain.stocksimulator.state.components.NameComponent;
import com.metalrain.stocksimulator.state.components.PriceComponent;
import com.metalrain.stocksimulator.state.components.WalletComponent;
import com.metalrain.stocksimulator.state.entities.MarketItemEntity;
import com.metalrain.stocksimulator.state.entities.PlayerEntity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Adam Hammer on 15-06-25.
 */
public class OrderView extends FrameLayout {
    @InjectView(R.id.market_item_name)
    TextView marketItemName;
    @InjectView(R.id.quantity)
    EditText quantity;
    @InjectView(R.id.button_increase_quantity)
    Button buttonIncreaseQuantity;
    @InjectView(R.id.button_decrease_quantity)
    Button buttonDecreaseQuantity;
    @InjectView(R.id.buy_button)
    Button buyButton;
    @InjectView(R.id.sell_button)
    Button sellButton;
    @InjectView(R.id.button_sell_all)
    Button buttonSellAll;
    @InjectView(R.id.button_buy_max)
    Button buttonBuyMax;

    private MarketItemEntity marketItemEntity;

    public OrderView(Context context) {
        super(context);
        inflate();
    }

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate();
    }

    private void inflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.market_order_view, this);
        ButterKnife.inject(this);
        buttonIncreaseQuantity.setOnClickListener(new ChangeQuantityOnClick(1));
        buttonDecreaseQuantity.setOnClickListener(new ChangeQuantityOnClick(-1));
        buyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerEntity playerEntity = StockSimulator.getGameState().getCurrentPlayer();
                int total = marketItemEntity.getComponent(PriceComponent.class).price * getQuantityValue();
                WalletComponent wallet = playerEntity.getComponent(WalletComponent.class);
                if (total > wallet.balance) {
                    System.out.println("Not enough balance to do this. " + (total - wallet.balance) + " Short\n");
                    return;
                } else {
                    wallet.balance -= total;
                    InventoryComponent inventoryComponent = playerEntity.getComponent(InventoryComponent.class);
                    Integer orig_quantity = inventoryComponent.inventory.get(marketItemEntity.getComponent(NameComponent.class));
                    if (orig_quantity == null) orig_quantity = 0;
                    inventoryComponent.inventory.put(marketItemEntity.getComponent(NameComponent.class), orig_quantity + getQuantityValue());
                }
                invalidate();
            }
        });

        sellButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                PlayerEntity playerEntity = StockSimulator.getGameState().getCurrentPlayer();
                InventoryComponent inventoryComponent = playerEntity.getComponent(InventoryComponent.class);
                int total = marketItemEntity.getComponent(PriceComponent.class).price * getQuantityValue();
                WalletComponent wallet = playerEntity.getComponent(WalletComponent.class);
                Integer inventoryCount = inventoryComponent.inventory.get(marketItemEntity.getComponent(NameComponent.class));
                if (inventoryCount == null) inventoryCount = 0;
                if (inventoryCount < getQuantityValue()) {
                    System.out.println("Insufficient Quantity to make sale");
                    return;
                } else {
                    inventoryComponent.inventory.put(marketItemEntity.getComponent(NameComponent.class), inventoryCount - getQuantityValue());
                    wallet.balance += total;
                }
                invalidate();
            }
        });

        buttonSellAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerEntity playerEntity = StockSimulator.getGameState().getCurrentPlayer();
                InventoryComponent inventoryComponent = playerEntity.getComponent(InventoryComponent.class);
                Integer inventoryCount = inventoryComponent.inventory.get(marketItemEntity.getComponent(NameComponent.class));
                int total = marketItemEntity.getComponent(PriceComponent.class).price * inventoryCount;
                WalletComponent wallet = playerEntity.getComponent(WalletComponent.class);
                inventoryComponent.inventory.put(marketItemEntity.getComponent(NameComponent.class), inventoryCount - inventoryCount);
                wallet.balance += total;
                invalidate();
            }
        });

        buttonBuyMax.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerEntity playerEntity = StockSimulator.getGameState().getCurrentPlayer();
                WalletComponent wallet = playerEntity.getComponent(WalletComponent.class);
                InventoryComponent inventoryComponent = playerEntity.getComponent(InventoryComponent.class);
                PriceComponent pc = marketItemEntity.getComponent(PriceComponent.class);
                NameComponent marketItemNameComponent = marketItemEntity.getComponent(NameComponent.class);

                int max_avail = wallet.balance / pc.price;
                int total = pc.price * max_avail;
                wallet.balance -= total;

                Integer orig_quantity = inventoryComponent.inventory.get(marketItemNameComponent);
                if (orig_quantity == null) orig_quantity = 0;

                inventoryComponent.inventory.put(marketItemNameComponent, orig_quantity + max_avail);
                invalidate();
            }
        });
    }

    public void setMarketItem(MarketItemEntity marketItemEntity) {
        this.marketItemEntity = marketItemEntity;
        drawMarketItem();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        drawMarketItem();
    }

    private void drawMarketItem() {
        if (this.marketItemEntity != null) {
            NameComponent nc = marketItemEntity.getComponent(NameComponent.class);
            PriceComponent pc = marketItemEntity.getComponent(PriceComponent.class);
            MarketVolatilityComponent mvc = marketItemEntity.getComponent(MarketVolatilityComponent.class);
            marketItemName.setText(nc.name);

            int total = (getQuantityValue() * pc.price);
            buyButton.setText("Buy " + getQuantityValue() + " for $" + String.valueOf(total / 100f));
            sellButton.setText("Sell " + getQuantityValue() + " for $" + String.valueOf(total / 100f));

            if (getQuantityValue() == 0) {
                buyButton.setEnabled(false);
                sellButton.setEnabled(false);
            } else {
                buyButton.setEnabled(true);
                sellButton.setEnabled(true);
            }

            PlayerEntity playerEntity = StockSimulator.getGameState().getCurrentPlayer();
            InventoryComponent inventory = playerEntity.getComponent(InventoryComponent.class);
            if (inventory.getInventoryQuantity(nc) < getQuantityValue())
                sellButton.setEnabled(false);
            if (total > playerEntity.getComponent(WalletComponent.class).balance)
                buyButton.setEnabled(false);
            buttonSellAll.setEnabled(inventory.getInventoryQuantity(nc) > 0);
            buttonBuyMax.setEnabled(pc.price < playerEntity.getComponent(WalletComponent.class).balance);
        }
    }

    public MarketItemEntity getMarketItem() {
        return marketItemEntity;
    }

    private class ChangeQuantityOnClick implements OnClickListener {
        private final int size;

        public ChangeQuantityOnClick(int i) {
            this.size = i;
        }

        @Override
        public void onClick(View view) {
            int newValue = getQuantityValue() + size;
            if (newValue < 0) return;
            quantity.setText(String.valueOf(newValue));
            drawMarketItem();
        }
    }

    private Integer getQuantityValue() {
        return Integer.valueOf(quantity.getText().toString());
    }
}
