package aroundwise.nepi.activities.discoverActivity.fragments.profileFragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.model.UserActivity;

/**
 * Created by mihai on 01/09/16.
 */
public interface ProfileFragmentView extends MvpView {

    public void hideProgressDialog();
    public void showProgressDialog();
    public void displayFavoriteOffers(List<Offer> favoriteOffers);
    public void displayMyRewards(List<Reward> userRewards);
    public void displayActivities(List<UserActivity> activities);
    public void setPoints();


}
