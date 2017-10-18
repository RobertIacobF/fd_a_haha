package aroundwise.nepi.activities.discoverActivity.fragments.getRewardsFragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import aroundwise.nepi.network.model.Reward;


/**
 * Created by mihai on 13/09/16.
 */
public interface RewardsFragmentView extends MvpView {

    public void displayRewards(List<Reward> rewards);
}
