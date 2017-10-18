package aroundwise.nepi.activities.discoverActivity.fragments.profileFragment;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferPresenter;
import aroundwise.nepi.eventbus.LogOutEvent;
import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.network.api.ActivityClient;
import aroundwise.nepi.network.api.OfferClient;
import aroundwise.nepi.network.api.RewardsClient;
import aroundwise.nepi.network.api.UserClient;
import aroundwise.nepi.network.model.Mall;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.model.UserActivity;
import aroundwise.nepi.network.model.UserReward;
import aroundwise.nepi.network.requests.UpdateMallRequest;
import aroundwise.nepi.network.responses.ActivityResponse;
import aroundwise.nepi.network.responses.FavoriteOffersResponse;
import aroundwise.nepi.network.responses.UserRewardResponse;
import aroundwise.nepi.network.responses.WalletResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import de.greenrobot.event.EventBus;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mihai on 01/09/16.
 */
public class ProfileFragmentPresenter extends MvpBasePresenter<ProfileFragmentView> {

    private List<Offer> offers;
    private List<UserReward> userRewards;
    private List<Reward> rewards;
    private List<UserActivity> activities;

    private CompositeSubscription compositeSubscription;

    public ProfileFragmentPresenter() {
        this.offers = new ArrayList<>();
        this.userRewards = new ArrayList<>();
        rewards = new ArrayList<>();
    }

    public CompositeSubscription getCompositeSubscription() {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        return compositeSubscription;
    }

    public void requestActivities() {
        if (isViewAttached())
            getView().showProgressDialog();
        final ActivityClient activityClient = ServiceGenerator.getServiceGenerator().createService(ActivityClient.class);
        getCompositeSubscription().add(activityClient.getUserActivity(0, 10, Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActivityResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayActivities(activities);
                            getView().hideProgressDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ProfileFragmentPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().hideProgressDialog();
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(ActivityResponse activityResponse) {
                        activities = activityResponse.getUserActivities();
                    }
                }));

    }

    public void requestFavoriteOffers() {
        if (isViewAttached())
            getView().showProgressDialog();
        final OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        int mallId = 8;
        if (Session.getUser().getProfile() != null
                && Session.getUser().getProfile().getMall() != null) {
            mallId = Session.getUser().getProfile().getMall().getId();
        } else if (Session.getMalls() != null && Session.getMalls().size() > 0) {
            mallId = Session.getMalls().get(0).getId();
        }
        getCompositeSubscription().add(offerClient.getUserFavoriteOffers(Constants.SESSIONID + Session.getUser().getSessionId(),
                mallId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FavoriteOffersResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayFavoriteOffers(offers);
                            getView().hideProgressDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ProfileFragmentPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().hideProgressDialog();
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(FavoriteOffersResponse favoriteOffersResponse) {
                        offers.clear();
                        for (int i = 0; i < favoriteOffersResponse.getOffers().size(); i++) {
                            Session.getFavoriteOfferIds().put(
                                    favoriteOffersResponse.getOffers().get(i).getOffer().getId(),
                                    favoriteOffersResponse.getOffers().get(i).getId());
                            offers.add(favoriteOffersResponse.getOffers().get(i).getOffer());
                        }
                        OfferPresenter.setLikedOffers(offers);
                    }
                }));
    }

    public void requestMyRewards() {
        if (isViewAttached())
            getView().showProgressDialog();
        RewardsClient rewardsClient = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        getCompositeSubscription().add(rewardsClient.getUserRewards(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getMall().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserRewardResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayMyRewards(rewards);
                            getView().hideProgressDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ProfileFragmentPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().hideProgressDialog();
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(UserRewardResponse userRewardResponse) {
                        rewards.clear();
                        for (UserReward reward : userRewardResponse.getRewards()) {
                            reward.getReward().code = reward.getCode();
                            reward.getReward().statusReward = reward.getStatus();
                            rewards.add(reward.getReward());
                        }
                    }
                }));
    }

    public void changeMall(final Mall mall) {
        if (isViewAttached())
            getView().showProgressDialog();
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
                        if (isViewAttached())
                            getView().hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ProfileFragmentPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().hideProgressDialog();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(ProfileFragmentPresenter.class.getSimpleName(), "onNext");
                    }
                }));
    }

    public void getUserWallet() {
        if (isViewAttached())
            getView().showProgressDialog();
        UserClient userClient = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        getCompositeSubscription().add(userClient.wallet(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getMall().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WalletResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().setPoints();
                            getView().hideProgressDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ProfileFragmentPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().hideProgressDialog();
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(WalletResponse responseBody) {
                        Session.getUser().getProfile().setPoints(responseBody.wallets.get(0).getTotalPoints());
                    }
                }));
    }

    public void closeSubscription() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

}

