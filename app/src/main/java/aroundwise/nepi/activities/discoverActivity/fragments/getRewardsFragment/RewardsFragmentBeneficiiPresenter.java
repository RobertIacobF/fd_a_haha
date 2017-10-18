package aroundwise.nepi.activities.discoverActivity.fragments.getRewardsFragment;

import android.util.Log;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import aroundwise.nepi.network.api.RewardsClient;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.responses.RewardResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mihai on 13/09/16.
 */
public class RewardsFragmentBeneficiiPresenter extends BaseRewardsPresenter {

    List<Reward> rewards;
    ResponseMeta rewardsMeta;

    public RewardsFragmentBeneficiiPresenter() {
    }

    public void getRewardsFromServer() {
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        client.getRewardsWithoutPoints(Constants.SESSIONID + Session.getUser().getSessionId(),
                0, 10, Session.getUser().getProfile().getMall().getId(), 0)
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
                        Log.d(RewardsFragmentPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(RewardResponse rewardResponse) {
                        if (rewardResponse.getRewards() != null) {
                            rewards = rewardResponse.getRewards();
                            rewardsMeta = rewardResponse.getMeta();
                        }
                    }
                });
    }

    public void getRewardsFromServer(int offset, int limit) {
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        client.getRewardsWithoutPoints(Constants.SESSIONID + Session.getUser().getSessionId(),
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
                        Log.d(RewardsFragmentPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(RewardResponse rewardResponse) {
                        if (rewardResponse.getRewards() != null) {
                            if (rewards != null) {
                                if (rewards.size() > 0 && rewards.get(rewards.size() - 1) == null)
                                    rewards.remove(rewards.size() - 1);
                                rewards.addAll(rewardResponse.getRewards());
                            } else {
                                rewards = rewardResponse.getRewards();
                            }
                            rewardsMeta = rewardResponse.getMeta();
                        }
                    }
                });
    }

    public void refreshOffers(int offset, int limit) {
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        client.getRewardsWithoutPoints(Constants.SESSIONID + Session.getUser().getSessionId(),
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
                        Log.d(RewardsFragmentPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(RewardResponse rewardResponse) {
                        if (rewardResponse.getRewards() != null) {
                            if (rewards != null) {
                                rewards = rewardResponse.getRewards();
                            }
                            rewardsMeta = rewardResponse.getMeta();
                        }
                    }
                });
    }


    public List<Reward> getRewards() {
        return rewards;
    }

    public ResponseMeta getRewardsMeta() {
        return rewardsMeta;
    }
}
