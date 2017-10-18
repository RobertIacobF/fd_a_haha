package aroundwise.nepi.eventbus;

import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.network.model.Shop;

/**
 * Created by mihai on 26/09/16.
 */
public class StoreMapEvent {
    private Shop store;
    private List<Shop> shopsList;

    public StoreMapEvent(Shop store, List<Shop> shoplist) {
        this.store = store;
        this.shopsList = shoplist;
    }

    public Shop getStore() {
        return store;
    }

    public List<Shop> getShopsList() {
        return shopsList;
    }
}
