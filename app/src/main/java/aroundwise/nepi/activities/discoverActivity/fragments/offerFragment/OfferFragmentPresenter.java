package aroundwise.nepi.activities.discoverActivity.fragments.offerFragment;

import android.util.Log;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.eventbus.LogOutEvent;
import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.network.api.OfferClient;
import aroundwise.nepi.network.api.UserClient;
import aroundwise.nepi.network.model.CoverOfferGroup;
import aroundwise.nepi.network.model.Mall;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.OfferType;
import aroundwise.nepi.network.model.SimpleOfferGroup;
import aroundwise.nepi.network.requests.UpdateMallRequest;
import aroundwise.nepi.network.responses.CoverOfferGroupResponse;
import aroundwise.nepi.network.responses.OfferResponse;
import aroundwise.nepi.network.responses.OfferTypeResponse;
import aroundwise.nepi.network.responses.SimpleOfferGroupResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import de.greenrobot.event.EventBus;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mihai on 29/08/16.
 */
public class OfferFragmentPresenter extends MvpBasePresenter<OfferFragmentView> {

    List<Offer> offers;
    List<OfferType> offerTypes;
    List<Offer> simpleFeaturedOffers;
    List<Offer> coverFeaturedOffers;
    List<CoverOfferGroup> coverOfferGroups;
    List<SimpleOfferGroup> simpleOfferGroups;
    ResponseMeta offersMeta;

    CompositeSubscription compositeSubscription;

    public OfferFragmentPresenter() {
        offers = new ArrayList<>();
        createCompositeSubscription();
    }

    public void removeSubscriptions() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    public void createCompositeSubscription() {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed())
            compositeSubscription = new CompositeSubscription();
    }

    public List<SimpleOfferGroup> getSimpleOfferGroups() {
        return simpleOfferGroups;
    }

    public void setSimpleOfferGroups(List<SimpleOfferGroup> simpleOfferGroups) {
        this.simpleOfferGroups = simpleOfferGroups;
    }

    public void getOffers() {
        requestOffers();
    }

    private void requestOffers() {
        final OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        compositeSubscription.add(offerClient.getOffers(Session.getUser().getProfile().getMall().getId(),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OfferResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayOffers(offers, true);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(OfferFragmentPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().hideProgressBar();
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(OfferResponse offerResponse) {
                        if (offers != null) {
                            offers.clear();
                            offers.addAll(offerResponse.getOffers());
                        } else
                            offers = offerResponse.getOffers();
                        OfferPresenter.setLikedOffers(offers);
                        offersMeta = offerResponse.getMeta();
                    }
                }));
    }

    public void requestOffers(int offset, int limit) {
        final OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        compositeSubscription.add(offerClient.getOffers(offset, limit, Session.getUser().getProfile().getMall().getId(),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OfferResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayOffers(offers, true);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideProgressBar();
                        }
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(OfferResponse offerResponse) {
                        if (offers != null) {
                            if (offers.size() > 0 &&
                                    offers.get(offers.size() - 1) == null)
                                offers.remove(offers.size() - 1);
                            offers.addAll(offerResponse.getOffers());
                        } else
                            offers = offerResponse.getOffers();
                        OfferPresenter.setLikedOffers(offers);
                        offersMeta = offerResponse.getMeta();
                    }
                }));
    }

    public void changeMall(final Mall mall) {
        if (isViewAttached())
            getView().showProgressBar();
        UserClient offerClient = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        compositeSubscription.add(offerClient.changeMall(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getId(), new UpdateMallRequest(mall.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Session.getUser().getProfile().setMall(mall);
                        EventBus.getDefault().post(new MallHasChangedEvent());
                        if (isViewAttached()) {
                            getView().refreshDataWhenMallIsChanged();
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(OfferFragmentPresenter.class.getSimpleName(), "onNext");
                    }
                }));
    }

    public void getOffersType() {
        if (offerTypes == null) {
            OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
            compositeSubscription.add(offerClient.getOfferType()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<OfferTypeResponse>() {
                        @Override
                        public void onCompleted() {
                            if (isViewAttached())
                                getView().showOfferTypes(offerTypes);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(OfferFragmentPresenter.class.getSimpleName(), e.getMessage());
                            if (e instanceof HttpException) {
                                if (((HttpException) e).code() == 401) {
                                    EventBus.getDefault().post(new LogOutEvent());
                                }
                            }
                        }

                        @Override
                        public void onNext(OfferTypeResponse offerTypeResponse) {
                            offerTypes = offerTypeResponse.getOfferType();
                        }
                    }));
        }
    }

    public void getOffersByType(OfferType offerType) {
        if (offerType != null) {
            if (isViewAttached())
                getView().showProgressBar();
            OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
            compositeSubscription.add(offerClient.getOffersByType(offerType.getName(),
                    Session.getUser().getProfile().getMall().getId(),
                    Constants.SESSIONID + Session.getUser().getSessionId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<OfferResponse>() {
                        @Override
                        public void onCompleted() {
                            if (isViewAttached()) {
                                getView().displayOffers(offers, false);
                                getView().hideProgressBar();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (isViewAttached()) {
                                getView().hideProgressBar();
                            }
                            if (e instanceof HttpException) {
                                if (((HttpException) e).code() == 401) {
                                    EventBus.getDefault().post(new LogOutEvent());
                                }
                            }
                        }

                        @Override
                        public void onNext(OfferResponse offerResponse) {

                            if (offers != null && offers.size() > 0) {
                                if (offers.get(offers.size() - 1) == null)
                                    offers.remove(offers.size() - 1);
                                offers.clear();
                                offers.addAll(offerResponse.getOffers());
                            } else
                                offers = offerResponse.getOffers();
                            offersMeta = offerResponse.getMeta();
                            OfferPresenter.setLikedOffers(offers);
                        }
                    }));
        } else
            requestOffers();
    }

    public void getOffersByType(OfferType offerType, final int offset, int limit) {
        if (offerType != null) {
            if (isViewAttached())
                getView().showProgressBar();
            OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
            compositeSubscription.add(offerClient.getOffersByType(offerType.getName(),
                    Session.getUser().getProfile().getMall().getId(), offset, limit,
                    Constants.SESSIONID + Session.getUser().getSessionId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<OfferResponse>() {
                        @Override
                        public void onCompleted() {
                            if (isViewAttached()) {
                                getView().displayOffers(offers, false);
                                getView().hideProgressBar();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(OfferFragmentPresenter.class.getSimpleName(), e.getMessage());
                            if (isViewAttached())
                                getView().hideProgressBar();
                            if (e instanceof HttpException) {
                                if (((HttpException) e).code() == 401) {
                                    EventBus.getDefault().post(new LogOutEvent());
                                }
                            }
                        }

                        @Override
                        public void onNext(OfferResponse offerResponse) {

                            if (offers != null) {
                                if (offers.get(offers.size() - 1) == null)
                                    offers.remove(offers.size() - 1);
                                offers.addAll(offerResponse.getOffers());
                            } else
                                offers = offerResponse.getOffers();
                            offersMeta = offerResponse.getMeta();
                            OfferPresenter.setLikedOffers(offers);
                        }
                    }));
        } else
            requestOffers();
    }

    public void refreshList(OfferType offerType, final int offset, int limit) {
        if (offerType != null) {
            if (isViewAttached())
                getView().showProgressBar();
            OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
            compositeSubscription.add(offerClient.getOffersByType(offerType.getName(),
                    Session.getUser().getProfile().getMall().getId(), offset, limit,
                    Constants.SESSIONID + Session.getUser().getSessionId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<OfferResponse>() {
                        @Override
                        public void onCompleted() {
                            if (isViewAttached()) {
                                getView().displayOffers(offers, false);
                                getView().hideProgressBar();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(OfferFragmentPresenter.class.getSimpleName(), e.getMessage());
                            if (isViewAttached())
                                getView().hideProgressBar();
                            if (e instanceof HttpException) {
                                if (((HttpException) e).code() == 401) {
                                    EventBus.getDefault().post(new LogOutEvent());
                                }
                            }
                        }

                        @Override
                        public void onNext(OfferResponse offerResponse) {

                            offers = offerResponse.getOffers();
                            offersMeta = offerResponse.getMeta();
                            OfferPresenter.setLikedOffers(offers);
                        }
                    }));
        } else
            requestOffers();
    }

    public List<OfferType> getOfferTypes() {
        return offerTypes;
    }

    public List<Offer> getOffersList() {
        return offers;
    }

    public ResponseMeta getOffersMeta() {
        return offersMeta;
    }

    public void showProgressInView() {
        if (isViewAttached())
            getView().showProgressBar();
    }

    public void hideProgress() {
        if (isViewAttached())
            getView().hideProgressBar();
    }

    public Observable<List<CoverOfferGroup>> getCoverOfferGroupListIds() {
        OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        return offerClient.getCoverOffersList(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getMall().getId())
                .map(new Func1<CoverOfferGroupResponse, List<CoverOfferGroup>>() {
                    @Override
                    public List<CoverOfferGroup> call(CoverOfferGroupResponse coverOfferGroupResponse) {
                        return coverOfferGroupResponse.getCoverOfferGroups();
                    }
                });
    }

    public void getAllData() {
        if (isViewAttached())
            getView().showProgressBar();
        OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        Observable getData = offerClient.getSimpleOfferGroup(Constants.SESSIONID + Session.getUser().getSessionId())
                .flatMap(new Func1<SimpleOfferGroupResponse, Observable<?>>() {
                    @Override
                    public Observable<?> call(SimpleOfferGroupResponse simpleOfferGroupResponse) {
                        simpleOfferGroups = simpleOfferGroupResponse.getCoverOfferGroups();
                        return Observable.zip(getSimpleOfferGroupOffers(simpleOfferGroupResponse.getCoverOfferGroups().get(simpleOfferGroupResponse.getCoverOfferGroups().size() - 1).getId()),
                                getCoverOfferGroupListIds(),
                                requestOffersObservable(), new Func3<List<Offer>, List<CoverOfferGroup>, List<Offer>, Void>() {
                                    @Override
                                    public Void call(List<Offer> offer, List<CoverOfferGroup> coverOffer, List<Offer> offers2) {
                                        simpleFeaturedOffers = offer;
                                        coverOfferGroups = coverOffer;
                                        offers = offers2;
                                        return null;
                                    }
                                }


                        );
                    }
                });
        compositeSubscription.add(getData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayData(offers, coverOfferGroups, simpleFeaturedOffers);
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideProgressBar();
                        }
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                }));
    }

    public Observable<List<Offer>> getSimpleOfferGroupOffers(int id) {
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

    private Observable<List<Offer>> requestOffersObservable() {
        final OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        return offerClient.getOffers(Session.getUser().getProfile().getMall().getId(),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .map(new Func1<OfferResponse, List<Offer>>() {
                    @Override
                    public List<Offer> call(OfferResponse offerResponse) {
                        offersMeta = offerResponse.getMeta();
                        return offerResponse.getOffers();
                    }
                });
    }

}
