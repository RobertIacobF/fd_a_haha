package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.changeRewardFragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import aroundwise.nepi.network.model.Reward;

/**
 * Created by mihai on 29/09/16.
 */
public interface ChangeRewardFragmentView extends MvpView{

    public void hideProgressView();
    public void showProgressView();

    public void displayRewards(List<Reward> rewards);

    public void goBack();

    public void displayError(String message);

}
