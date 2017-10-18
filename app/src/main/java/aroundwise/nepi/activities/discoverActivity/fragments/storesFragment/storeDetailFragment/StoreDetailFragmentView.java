package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeDetailFragment;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.model.Shop;

/**
 * Created by mihai on 31/08/16.
 */
public interface StoreDetailFragmentView extends MvpView {

    public void displayStoreRewards(List<Reward> rewards);

    public void displayStoreOffers(@Nullable List<Offer> offers);

    public void setStore(Shop shop);

    public void displayData(List<Offer> offers, List<Reward> rewards);

    public void showProgressBar();

    public void hideProgressBar();

}
