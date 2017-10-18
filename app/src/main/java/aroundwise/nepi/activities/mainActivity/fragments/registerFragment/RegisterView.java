package aroundwise.nepi.activities.mainActivity.fragments.registerFragment;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by mihai on 07/09/16.
 */
public interface RegisterView extends MvpView {

    public void setProfileImage(Uri image);
    public int getImageViewWidth();
    public int getImageViewHeight();
    public void serverError(String message);
    public void showProgressBar();
    public void hideProgressBar();
    public void registerAfterMallsDownloaded();
    public void showAlertError(String error);
}
