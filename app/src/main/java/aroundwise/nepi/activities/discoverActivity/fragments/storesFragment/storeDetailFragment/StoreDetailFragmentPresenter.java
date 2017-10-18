package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeDetailFragment;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;


import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.network.api.OfferClient;
import aroundwise.nepi.network.api.RewardsClient;
import aroundwise.nepi.network.api.ShopClient;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.network.responses.OfferResponse;
import aroundwise.nepi.network.responses.RewardResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by mihai on 31/08/16.
 */
public class StoreDetailFragmentPresenter extends MvpBasePresenter<StoreDetailFragmentView> {


    private List<Reward> storeRewards;
    private List<Offer> storeOffers;
    private Shop store;

    public void requestStoreRewards(final Shop store) {
        RewardsClient rewardsClient = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        rewardsClient.getStoreRewards(store.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RewardResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached())
                            getView().displayStoreRewards(storeRewards);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(StoreDetailFragmentPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(RewardResponse rewardResponse) {
                        storeRewards = rewardResponse.getRewards();
                    }
                });
    }

    public Observable<List<Reward>> requestStoreRewardsObs(final Shop store) {
        RewardsClient rewardsClient = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        return rewardsClient.getStoreRewards(store.getId())
                .map(new Func1<RewardResponse, List<Reward>>() {
                    @Override
                    public List<Reward> call(RewardResponse rewardResponse) {
                        return rewardResponse.getRewards();
                    }
                });

    }

    public Observable<List<Offer>> requestStoreOffersObs(Shop store) {
        OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        return offerClient.getStoreOffer(store.getId(),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .map(new Func1<OfferResponse, List<Offer>>() {
                    @Override
                    public List<Offer> call(OfferResponse offerResponse) {
                        return offerResponse.getOffers();
                    }
                });
    }

    public Observable<Shop> getShopDetailsObs(long shopId) {
        ShopClient shopClient = ServiceGenerator.getServiceGenerator().createService(ShopClient.class);
        return shopClient.getShopDetails(shopId, Constants.SESSIONID + Session.getUser().getSessionId())
                .map(new Func1<Shop, Shop>() {
                    @Override
                    public Shop call(Shop shop) {
                        return shop;
                    }
                });
    }

    public void getAllData(Shop store) {
        if (isViewAttached())
            getView().showProgressBar();
        Observable obs = Observable.zip(requestStoreOffersObs(store),
                requestStoreRewardsObs(store), new Func2<List<Offer>, List<Reward>, Void>() {
                    @Override
                    public Void call(List<Offer> offers, List<Reward> rewards) {
                        storeOffers = offers;
                        storeRewards = rewards;
                        return null;
                    }
                });

        obs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayData(storeOffers, storeRewards);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(StoreDetailFragment.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().hideProgressBar();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    public void requestStoreOffers(Shop store) {
        OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        offerClient.getStoreOffer(store.getId(),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OfferResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached())
                            getView().displayStoreOffers(storeOffers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(StoreDetailFragment.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().displayStoreOffers(null);
                    }

                    @Override
                    public void onNext(OfferResponse offerResponse) {
                        storeOffers = offerResponse.getOffers();
                    }
                });
    }

    public void getShopDetails(long shopId) {
        if (isViewAttached())
            getView().showProgressBar();
        ShopClient shopClient = ServiceGenerator.getServiceGenerator().createService(ShopClient.class);
        shopClient.getShopDetails(shopId, Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Shop>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().setStore(store);
                        }
                        getAllData(store);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(StoreDetailFragment.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().hideProgressBar();
                    }

                    @Override
                    public void onNext(Shop shop) {
                        store = shop;
                    }
                });
    }

    public List<Reward> getStoreRewards() {
        return storeRewards;
    }
}
