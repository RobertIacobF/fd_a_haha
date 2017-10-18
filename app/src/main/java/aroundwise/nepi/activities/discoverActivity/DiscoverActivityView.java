package aroundwise.nepi.activities.discoverActivity;

import android.support.v4.app.Fragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by mihai on 29/08/16.
 */
public interface DiscoverActivityView extends MvpView {
    public void changeFragment(Fragment fragment);

    public void popFragment();

    public void changeTab(Fragment fragment);

    public void setProfilePicture(String uri);

    public void setPoints();

    public void hideProgressBar();
    public void showProgressBar();
    public void errorOnUpdateToken();
    public void successOnUpdateToken();

    public void resumeInitialization();

}
