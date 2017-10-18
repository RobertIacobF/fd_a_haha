package aroundwise.nepi.activities.discoverActivity.fragments.getRewardsFragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by mihai on 15/09/16.
 */
public interface GetRewardsFragmentView extends MvpView {
    public void updateRewardCode(String code, String status);
    public void hideProgressBar();
    public void showProgressBar();
}
