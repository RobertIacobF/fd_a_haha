package aroundwise.nepi.activities.discoverActivity;

import android.util.Log;

import com.aroundwise.aroundwisesdk.model.http.auth.User;
import com.aroundwise.aroundwisesdk.model.http.beacon.GetPointsRequest;
import com.aroundwise.aroundwisesdk.model.http.beacon.GetPointsResult;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;


import aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.LoyaltyFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.nearYouFragment.NearYouFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferPresenter;
import aroundwise.nepi.activities.discoverActivity.fragments.profileFragment.ProfileFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.StoresFragment;
import aroundwise.nepi.eventbus.LogOutEvent;
import aroundwise.nepi.eventbus.WalletChangeEvent;
import aroundwise.nepi.network.api.ActivityClient;
import aroundwise.nepi.network.api.LoginClient;
import aroundwise.nepi.network.api.RegisterClient;
import aroundwise.nepi.network.api.UserClient;
import aroundwise.nepi.network.requests.TokenRequest;
import aroundwise.nepi.network.responses.BasicResponse;
import aroundwise.nepi.network.responses.MallResponse;
import aroundwise.nepi.network.responses.UpdateProfileResponse;
import aroundwise.nepi.network.responses.UserResponse;
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
 * Created by mihai on 29/08/16.
 */
public class DiscoverActivityPresenter extends MvpBasePresenter<DiscoverActivityView> {


    CompositeSubscription compositeSubscription;

    public DiscoverActivityPresenter() {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed())
            compositeSubscription = new CompositeSubscription();
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

    public void changeToOfferFragment() {
        OfferFragment offerFragment = OfferFragment.newInstance();
        if (isViewAttached())
            getView().changeTab(offerFragment);
    }

    public void changeToLoylatyFragment() {
        LoyaltyFragment fragment = LoyaltyFragment.newInstance();
        if (isViewAttached())
            getView().changeTab(fragment);
    }

    public void changeToStoreFragment() {
        StoresFragment fragment = StoresFragment.newInstance(false);
        if (isViewAttached())
            getView().changeTab(fragment);
    }

    public void changeToProfileFragment() {
        ProfileFragment fragment = ProfileFragment.newInstance();
        if (isViewAttached())
            getView().changeTab(fragment);
    }

    public void changeToProfileFragment(int mode) {
        ProfileFragment fragment = ProfileFragment.newInstance(mode);
        if (isViewAttached())
            getView().changeTab(fragment);
    }


    public void changeToNearYouFragment() {
        NearYouFragment fragment = NearYouFragment.newInstance();
        if (isViewAttached())
            getView().changeTab(fragment);
    }

    public void getUserDetails() {
        if (isViewAttached())
            getView().showProgressBar();
        LoginClient loginClient = ServiceGenerator.getServiceGenerator().createService(LoginClient.class);
        compositeSubscription.add(loginClient.userDetails(Session.getUser().getId(), Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().setProfilePicture(Session.getUser().getProfile().getImage());
                            getView().hideProgressBar();
                            getUserWallet();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(DiscoverActivityPresenter.class.getSimpleName(), e.getMessage());
                        getView().hideProgressBar();
                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        Session.getUser().setUserResponse(userResponse);
                    }
                }));
    }

    public void getMalls() {
        if (isViewAttached())
            getView().showProgressBar();
        RegisterClient client = ServiceGenerator.getServiceGenerator().createService(RegisterClient.class);
        compositeSubscription.add(client.getMalls(10, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().resumeInitialization();
                            getView().hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMalls();
                    }

                    @Override
                    public void onNext(MallResponse mallResponse) {
                        Session.setMalls(mallResponse.getMalls());
                    }
                }));
    }

    public void getUserWallet() {
        UserClient userClient = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        compositeSubscription.add(userClient.wallet(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile() != null ? Session.getUser().getProfile().getMall().getId() : 8)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WalletResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().setPoints();
                            EventBus.getDefault().post(new WalletChangeEvent());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(DiscoverActivityPresenter.class.getSimpleName(), e.getMessage());
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

    public void getFavoriteOffers() {
        OfferPresenter.getUserFavoriteOffers();
    }

    public void sendToken(String token) {
        UserClient userClient = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        compositeSubscription.add(userClient.resendToken(Session.getUser().getUserProfileId(), new TokenRequest(token),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UpdateProfileResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached())
                            getView().errorOnUpdateToken();
                    }

                    @Override
                    public void onNext(UpdateProfileResponse basicResponse) {
                        if (isViewAttached())
                            getView().successOnUpdateToken();
                    }
                }));
    }


}
