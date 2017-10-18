package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.changeRewardFragment;

import android.util.Log;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;


import java.util.List;

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
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mihai on 29/09/16.
 */
public class ChangeRewardFragmentPresenter extends MvpBasePresenter<ChangeRewardFragmentView> {

    private List<Reward> rewards;
    private ResponseMeta rewardsMeta;

    CompositeSubscription compositeSubscription;

    public ChangeRewardFragmentPresenter() {
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

    public void getRewardsFromServer() {
        if (isViewAttached()) {
            getView().showProgressView();
        }
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        compositeSubscription.add(client.getRewards(Constants.SESSIONID + Session.getUser().getSessionId(), 0, 10, Session.getUser().getProfile().getMall().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RewardResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayRewards(rewards);
                            getView().hideProgressView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ChangeRewardFragmentPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(RewardResponse rewardResponse) {
                        if (rewardResponse.getRewards() != null) {
                            rewards = rewardResponse.getRewards();
                            rewardsMeta = rewardResponse.getMeta();
                        }
                    }
                }));
    }

    public void getRewardsFromServer(int offset, int limit) {
        if (isViewAttached()) {
            getView().showProgressView();
        }
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        compositeSubscription.add(client.getRewards(offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RewardResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().displayRewards(rewards);
                            getView().hideProgressView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ChangeRewardFragmentPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(RewardResponse rewardResponse) {
                        if (rewardResponse.getRewards() != null) {
                            if (rewards != null) {
                                if (rewards.size() > 0 && rewards.get(rewards.size() - 1) == null)
                                    rewards.remove(rewards.size() - 1);
                            }
                            rewards.addAll(rewardResponse.getRewards());
                            rewardsMeta = rewardResponse.getMeta();
                        }
                    }
                }));
    }

    public void updateCurrentReward(final Reward reward) {
        if (isViewAttached()) {
            getView().showProgressView();
        }
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        compositeSubscription.add(client.setCurrentReward(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getId(), new SetCurrentRewardRequest(reward.resource_uri))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SetCurrentRewardResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            Session.getUser().getProfile().setCurrent_reward(reward);
                            getView().hideProgressView();
                            getView().goBack();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ChangeRewardFragmentPresenter.class.getSimpleName(), e.getMessage());
                        if(isViewAttached()) {
                            getView().hideProgressView();
                            getView().displayError("A apărut o eroare. Vă rugam să verificați conexiunea la internet și să încercați din nou.");
                        }
                    }

                    @Override
                    public void onNext(SetCurrentRewardResponse setCurrentRewardResponse) {
                        //  Logger.d(setCurrentRewardResponse.getMessage());
                    }
                }));
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public ResponseMeta getRewardsMeta() {
        return rewardsMeta;
    }
}
