package aroundwise.nepi.activities.discoverActivity.fragments.profileFragment.fragments;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by mihai on 22/09/16.
 */
public interface ProfileSettingsView extends MvpView{
    public void displaySnackbarMessage(String message);
    public void hideProgressView();
    public void finishActivity();
    public void showProgressBar();

}
