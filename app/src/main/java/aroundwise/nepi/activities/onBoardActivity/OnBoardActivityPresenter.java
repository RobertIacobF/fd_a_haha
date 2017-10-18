package aroundwise.nepi.activities.onBoardActivity;

import android.content.Context;
import android.util.Log;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;


import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.network.api.RewardsClient;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.requests.SetCurrentRewardRequest;
import aroundwise.nepi.network.responses.RewardResponse;
import aroundwise.nepi.network.responses.SetCurrentRewardResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mihai on 13/09/16.
 */
public class OnBoardActivityPresenter extends MvpBasePresenter<OnBoardActivityView> {

    List<Reward> rewards;
    ResponseMeta rewardsMeta;

    public List<Reward> getRewards() {
        return rewards;
    }

    public ResponseMeta getRewardsMeta() {
        return rewardsMeta;
    }

    public void getRewardsFromServer() {
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        client.getRewards(Constants.SESSIONID + Session.getUser().getSessionId(), 0, 10, Session.getUser().getProfile().getMall().getId(), 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RewardResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached())
                            getView().displayRewards(rewards);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(OnBoardActivityPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(RewardResponse rewardResponse) {
                        if (rewardResponse.getRewards() != null) {
                            rewards = rewardResponse.getRewards();
                            rewardsMeta = rewardResponse.getMeta();
                            for (int i = 0; i < rewards.size() - 1; i++)
                                if (rewards.get(i).rewardImages.size() > 0)
                                    MyApplication.instance.getPicasso().load(rewards.get(i).rewardImages.get(0).getImage()).fetch();
                        }
                    }
                });
    }

    public void getRewardsFromServer(int offset, int limit) {
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        client.getRewards(Constants.SESSIONID + Session.getUser().getSessionId(),
                offset, limit, Session.getUser().getProfile().getMall().getId(), 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RewardResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached())
                            getView().displayRewards(rewards);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(OnBoardActivityPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(RewardResponse rewardResponse) {
                        if (rewardResponse.getRewards() != null) {
                            if (rewards.size() > 0 && rewards.get(rewards.size() - 1) == null)
                                rewards.remove(rewards.size() - 1);
                            rewards.addAll(rewardResponse.getRewards());
                            rewardsMeta = rewardResponse.getMeta();
                        }
                    }
                });
    }

    public void updateCurrentReward(final Reward reward, final Context context) {
        String resourceUri = "";
        if (reward != null)
            resourceUri = reward.resource_uri;
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        client.setCurrentReward(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getId(), new SetCurrentRewardRequest(resourceUri))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SetCurrentRewardResponse>() {
                    @Override
                    public void onCompleted() {
                        Session.getUser().getProfile().setCurrent_reward(reward);
                        Session.saveUserToSharedPref(context);
                        if (isViewAttached())
                            getView().nextStep();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(OnBoardActivityPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(SetCurrentRewardResponse setCurrentRewardResponse) {
                        //  Logger.d(setCurrentRewardResponse.getMessage());
                    }
                });
    }


}
