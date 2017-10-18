package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment;



import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import aroundwise.nepi.network.model.Shop;


/**
 * Created by mihai on 31/08/16.
 */
public interface StoresFragmentView extends MvpView {

    public void displayShops(List<Shop> shops, boolean scroolToTop);
    public void showProgressBar();
    public void hideProgressBar();
}
