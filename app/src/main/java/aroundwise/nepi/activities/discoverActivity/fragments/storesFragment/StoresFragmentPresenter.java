package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment;

import android.util.Log;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.network.api.ShopClient;
import aroundwise.nepi.network.api.UserClient;
import aroundwise.nepi.network.model.Mall;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.network.requests.UpdateMallRequest;
import aroundwise.nepi.network.responses.ShopResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import de.greenrobot.event.EventBus;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mihai on 31/08/16.
 */
public class StoresFragmentPresenter extends MvpBasePresenter<StoresFragmentView> {

    private List<Shop> shops;
    private ResponseMeta shopsMeta;
    private boolean walkIn;

    private CompositeSubscription compositeSubscription;

    public void setWalkIn(boolean walkIn) {
        this.walkIn = walkIn;
    }

    public StoresFragmentPresenter() {
        shops = new ArrayList<>();
        compositeSubscription = new CompositeSubscription();
    }

    public void removeSubscriptions() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    public CompositeSubscription getCompositeSubscription() {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        return compositeSubscription;
    }

    public void createCompositeSubscription() {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed())
            compositeSubscription = new CompositeSubscription();
    }

    public ResponseMeta getShopsMeta() {
        return shopsMeta;
    }

    public void requestForShops() {
        if (isViewAttached())
            getView().showProgressBar();
        if (walkIn)
            receiveWalkInShops();
        else
            receiveShops();
    }

    public void requestForShops(int offset, int limit) {
        if (walkIn)
            receiveWalkInShops(offset, limit);
        else
            receiveShops(offset, limit);
    }

    public void receiveShops() {
        ShopClient shopClient = ServiceGenerator.getServiceGenerator().createService(ShopClient.class);
        getCompositeSubscription().add(shopClient.getShops(Session.getUser().getProfile().getMall().getId(),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayShops(shops, true);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShopResponse shopResponse) {
                        shops.clear();
                        shops.addAll(shopResponse.getShops());
                        shopsMeta = shopResponse.getMeta();
                    }
                }));
    }

    public void receiveWalkInShops() {
        ShopClient shopClient = ServiceGenerator.getServiceGenerator().createService(ShopClient.class);
        getCompositeSubscription().add(shopClient.getShopsOnlyWithWalkin(0, Session.getMalls().get(0).getId(),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayShops(shops, true);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShopResponse shopResponse) {
                        shops.clear();
                        shops.addAll(shopResponse.getShops());
                        shopsMeta = shopResponse.getMeta();
                    }
                }));
    }

    public void receiveShops(int offset, int limit) {
        ShopClient shopClient = ServiceGenerator.getServiceGenerator().createService(ShopClient.class);
        getCompositeSubscription().add(shopClient.getShops(Session.getUser().getProfile().getMall().getId(),
                offset, limit, Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayShops(shops, false);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShopResponse shopResponse) {
                        if (shops != null) {
                            if (shops.size() > 0 && shops.get(shops.size() - 1) == null)
                                shops.remove(shops.size() - 1);
                            shops.addAll(shopResponse.getShops());
                        } else
                            shops = shopResponse.getShops();
                        shopsMeta = shopResponse.getMeta();
                    }
                }));
    }

    public void receiveWalkInShops(int offset, int limit) {
        ShopClient shopClient = ServiceGenerator.getServiceGenerator().createService(ShopClient.class);
        getCompositeSubscription().add(shopClient.getShopsOnlyWithWalkin(0, offset, limit, Session.getUser().getProfile().getMall().getId(),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayShops(shops, false);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShopResponse shopResponse) {
                        if (shops != null) {
                            if (shops.size() > 0 && shops.get(shops.size() - 1) == null)
                                shops.remove(shops.size() - 1);
                            shops.addAll(shopResponse.getShops());
                        } else
                            shops = shopResponse.getShops();
                        shopsMeta = shopResponse.getMeta();
                    }
                }));
    }

    public void changeMall(final Mall mall) {
        UserClient offerClient = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        getCompositeSubscription().add(offerClient.changeMall(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getId(), new UpdateMallRequest(mall.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Session.getUser().getProfile().setMall(mall);
                        EventBus.getDefault().post(new MallHasChangedEvent());
                        //if (isViewAttached())
                        //getView().refreshDataWhenMallIsChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(StoresFragmentPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(StoresFragmentPresenter.class.getSimpleName(), "onNext");
                    }
                }));
    }

    public void searchStores(int mallId, String searchQuery) {
        try {
            if (isViewAttached())
                getView().showProgressBar();
            String urlEncodedQuery = URLEncoder.encode(searchQuery, "UTF-8");
            ShopClient shopClient = ServiceGenerator.getServiceGenerator().createService(ShopClient.class);
            getCompositeSubscription().add(shopClient.searchShops(mallId, searchQuery, Constants.SESSIONID + Session.getUser().getSessionId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ShopResponse>() {
                        @Override
                        public void onCompleted() {
                            if (isViewAttached()) {
                                getView().displayShops(shops, true);
                                getView().hideProgressBar();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (isViewAttached())
                                getView().hideProgressBar();
                        }

                        @Override
                        public void onNext(ShopResponse shopResponse) {
                            shops.clear();
                            shops.addAll(shopResponse.getShops());
                            shopsMeta = shopResponse.getMeta();
                        }
                    }));

        } catch (UnsupportedEncodingException e) {
            if (isViewAttached())
                getView().hideProgressBar();
        }
    }

    public List<Shop> getShops() {
        return shops;
    }
}
