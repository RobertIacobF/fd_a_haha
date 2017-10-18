package aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.offerCollectionFragment;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import aroundwise.nepi.network.api.OfferClient;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.SimpleOfferGroup;
import aroundwise.nepi.network.responses.OfferResponse;
import aroundwise.nepi.network.responses.SimpleOfferGroupResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class OfferCollectionPresenter extends MvpBasePresenter<OfferCollectionView> {

    List<Offer> offers;
    List<SimpleOfferGroup> simpleOfferGroups;

    public void getCoverOfferGroupOffers(int id) {
        if (isViewAttached())
            getView().showProgressBar();
        OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        offerClient.getCoverOffers(id, Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getMall().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OfferResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayOffers(offers);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(OfferCollectionPresenter.class.getSimpleName(), e.getMessage());
                        getView().hideProgressBar();
                    }

                    @Override
                    public void onNext(OfferResponse basicResponse) {
                        offers = basicResponse.getOffers();
                    }
                });
    }

    public void getSimpleOffers() {
        if (isViewAttached())
            getView().showProgressBar();
        OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        Observable<List<Offer>> getData = offerClient.getSimpleOfferGroup(Constants.SESSIONID + Session.getUser().getSessionId())
                .flatMap(new Func1<SimpleOfferGroupResponse, Observable<List<Offer>>>() {
                    @Override
                    public Observable<List<Offer>> call(SimpleOfferGroupResponse simpleOfferGroupResponse) {
                        simpleOfferGroups = simpleOfferGroupResponse.getCoverOfferGroups();
                        return getSimpleOfferGroupOffers(simpleOfferGroupResponse.getCoverOfferGroups().get(0).getId());
                    }
                });

        getData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Offer>>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayOffers(offers);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(OfferCollectionPresenter.class.getSimpleName(), e.getMessage());
                        getView().hideProgressBar();
                    }

                    @Override
                    public void onNext(List<Offer> offersList) {
                        offers = offersList;
                    }
                });
    }

    private Observable<List<Offer>> getSimpleOfferGroupOffers(int id) {
        final OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        return offerClient.getSimpleGroup(id, Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getMall().getId())
                .map(new Func1<OfferResponse, List<Offer>>() {
                    @Override
                    public List<Offer> call(OfferResponse offerResponse) {
                        return offerResponse.getOffers();
                    }
                });
    }

}
