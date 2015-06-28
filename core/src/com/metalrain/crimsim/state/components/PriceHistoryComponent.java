package com.metalrain.crimsim.state.components;

import com.badlogic.ashley.core.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Keep a price history, 20 last iterations
 * Created by Adam Hammer on 6/22/2015.
 */
public class PriceHistoryComponent extends Component {
    private static final int MAX_SIZE = 100;
    private final List<Integer> priceHistory = new CopyOnWriteArrayList<Integer>();
    private final List<Integer> priceHistorySkipFive = new CopyOnWriteArrayList();
    private final List<Integer> priceHistorySkipFifteen = new CopyOnWriteArrayList();
    private final List<Integer> priceHistorySkipOneHundred = new CopyOnWriteArrayList();

    public void registerPrice(PriceComponent pc, int iteration) {
        synchronized (priceHistory) {
            priceHistory.add(pc.price);
            if (priceHistory.size() > MAX_SIZE) priceHistory.remove(0);
        }

        synchronized (priceHistorySkipFive) {
            if (iteration % 5 == 0) {
                priceHistorySkipFive.add(pc.price);
                if (priceHistorySkipFive.size() > MAX_SIZE) priceHistorySkipFive.remove(0);
            }
        }

        synchronized (priceHistorySkipFifteen) {
            if (iteration % 15 == 0) {
                priceHistorySkipFifteen.add(pc.price);
                if (priceHistorySkipFifteen.size() > MAX_SIZE) priceHistorySkipFifteen.remove(0);
            }
        }

        synchronized (priceHistorySkipOneHundred) {
            if (iteration % 100 == 0) {
                priceHistorySkipOneHundred.add(pc.price);
            }
        }
    }

    public List<Integer> getPriceHistory() {
        return Collections.synchronizedList(priceHistory);
    }

    public List<Integer> getPriceHistorySkipFive() {
        return Collections.synchronizedList(priceHistorySkipFive);
    }

    public List<Integer> getPriceHistorySkipFifteen() {
        return Collections.synchronizedList(priceHistorySkipFifteen);
    }


    public List<Integer> getPriceHistorySkipOneHundred() {
        return Collections.synchronizedList(priceHistorySkipOneHundred);
    }
}
