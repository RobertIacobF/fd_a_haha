package aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.offerCollectionFragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import aroundwise.nepi.network.model.Offer;

/**
 * Created by irina on 12/21/2016.
 */
public interface OfferCollectionView extends MvpView{

    public void displayOffers(List<Offer> offers);
    public void showProgressBar();
    public void hideProgressBar();

}
