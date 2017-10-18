package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by mihai on 30/08/16.
 */
public interface LoyaltyFragmentView extends MvpView {
    public void showProgressView();
    public void hideProgressView();
    public void mallsSwitched();
}
