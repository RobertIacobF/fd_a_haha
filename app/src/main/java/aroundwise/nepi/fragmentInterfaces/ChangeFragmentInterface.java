package aroundwise.nepi.fragmentInterfaces;

import android.os.Bundle;

import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.base.BaseMvpFragment;

/**
 * Created by mihai on 25/08/16.
 */
public interface ChangeFragmentInterface {

    public void changeFragment(BaseFragment fragment);
    public void changeFragment(BaseMvpFragment fragment);
    public Bundle getData();
}
