package aroundwise.nepi.eventbus;

import aroundwise.nepi.network.model.Shop;

/**
 * Created by irina on 1/12/2017.
 */
public class PurchasePointsEvent {
    Shop store;

    public PurchasePointsEvent(Shop store) {
        this.store = store;
    }

    public Shop getStore() {
        return store;
    }
}
