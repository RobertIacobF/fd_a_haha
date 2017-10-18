package aroundwise.nepi.eventbus;


import aroundwise.nepi.network.model.Shop;

/**
 * Created by mihai on 13/09/16.
 */
public class StoreDetailsEvent {
    private Shop store;

    public StoreDetailsEvent(Shop store) {
        this.store = store;
    }

    public Shop getStore() {
        return store;
    }
}
