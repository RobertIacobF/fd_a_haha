package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeMapFragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import aroundwise.nepi.network.model.Shop;

/**
 * Created by mihai on 16/09/16.
 */
public interface StoreMapView extends MvpView {
    public void setKmDistance(String km);
    public void displayShops(List<Shop> shops);
}
