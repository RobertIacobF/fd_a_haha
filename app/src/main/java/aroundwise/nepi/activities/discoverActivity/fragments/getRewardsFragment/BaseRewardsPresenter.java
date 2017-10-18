package aroundwise.nepi.activities.discoverActivity.fragments.getRewardsFragment;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import aroundwise.nepi.network.model.Reward;

/**
 * Created by Adonis on 3/7/2017.
 */
public abstract class BaseRewardsPresenter extends MvpBasePresenter<RewardsFragmentView> {
    public abstract void getRewardsFromServer();

    public abstract void getRewardsFromServer(int offset, int limit);

    public abstract void refreshOffers(int offset, int limit);

    public abstract List<Reward> getRewards();

    public abstract ResponseMeta getRewardsMeta();
}
