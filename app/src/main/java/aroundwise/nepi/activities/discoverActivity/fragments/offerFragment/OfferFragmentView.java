package aroundwise.nepi.activities.discoverActivity.fragments.offerFragment;


import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import aroundwise.nepi.network.model.CoverOfferGroup;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.OfferType;


/**
 * Created by mihai on 29/08/16.
 */
public interface OfferFragmentView extends MvpView {

    public void showOfferTypes(List<OfferType> offerTypeList);

    public void displayOffers(List<Offer> offers, boolean showSliders);

    public void displayOffersReceivedFromTypeChanged(List<Offer> offers);

    public void refreshOfferAdapter();

    public void showProgressBar();

    public void hideProgressBar();

    public void refreshDataWhenMallIsChanged();

    public void displayFeatureOffers(List<Offer> offers);

    public void displayData(List<Offer> offers,
                            List<CoverOfferGroup> coverOffers, List<Offer> simpleFeaturedOffers);
}
