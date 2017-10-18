package aroundwise.nepi.activities.discoverActivity.fragments.getRewardsFragment;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.eventbus.WalletChangeEvent;
import aroundwise.nepi.network.api.RewardsClient;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.model.UserReward;
import aroundwise.nepi.network.requests.ClaimRewardRequest;
import aroundwise.nepi.network.requests.DeleteUserReward;
import aroundwise.nepi.network.requests.SetCurrentRewardRequest;
import aroundwise.nepi.network.responses.SetCurrentRewardResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import de.greenrobot.event.EventBus;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mihai on 15/09/16.
 */
public class GetRewardsFragmentPresenter extends MvpBasePresenter<GetRewardsFragmentView> {

    public void claimReward(final Reward reward) {
        if (isViewAttached())
            getView().showProgressBar();
        if (!Session.getUserRewards().containsKey(reward.id)) {
            String uri = reward.resource_uri;
            if (uri.contains("rewardsub"))
                uri = uri.replace("rewardsub", "rewards");
            RewardsClient rewardsClient = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
            rewardsClient.claimReward(new ClaimRewardRequest(uri, Session.getUser().getResourceUri()),
                    Constants.SESSIONID + Session.getUser().getSessionId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UserReward>() {
                        @Override
                        public void onCompleted() {
                            if (isViewAttached())
                                getView().hideProgressBar();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (isViewAttached())
                                getView().hideProgressBar();
                        }

                        @Override
                        public void onNext(UserReward userReward) {
                            Log.d(GetRewardsFragmentPresenter.class.getSimpleName(), "next");
                            if (isViewAttached()) {
                                getView().updateRewardCode(userReward.getCode(), userReward.getStatus());
                            }
                            EventBus.getDefault().post(new MallHasChangedEvent());
                            //Session.getUserRewards().put(reward.id, userReward.getId());
                        }
                    });
        }
    }

    public void removeReward(Reward reward) {
        if (Session.getUserRewards().containsKey(reward.id)) {
            String uri = reward.resource_uri;
            if (uri.contains("rewardsub"))
                uri = uri.replace("rewardsub", "rewards");
            RewardsClient rewardsClient = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
            rewardsClient.removeUserReward(new DeleteUserReward(), Constants.SESSIONID + Session.getUser().getSessionId())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(GetRewardsFragmentPresenter.class.getSimpleName(), e.getMessage());
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            Log.d(GetRewardsFragmentPresenter.class.getSimpleName(), "On NExt");
                        }
                    });
        }
    }

    public void updateCurrentReward(Reward reward) {
        Session.getUser().getProfile().setCurrent_reward(reward);
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        client.setCurrentReward(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getId(), new SetCurrentRewardRequest(reward.resource_uri))
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<SetCurrentRewardResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(GetRewardsFragmentPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(SetCurrentRewardResponse setCurrentRewardResponse) {
                        //  Logger.d(setCurrentRewardResponse.getMessage());
                    }
                });
    }
}
