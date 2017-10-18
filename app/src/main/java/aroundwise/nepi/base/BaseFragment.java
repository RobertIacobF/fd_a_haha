package aroundwise.nepi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.fragmentInterfaces.ChangeFragmentInterface;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mihai on 25/08/16.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    protected ChangeFragmentInterface changeFragmentInterface;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // MyApplication.instance.refWatcher.watch(this);
    }

    public void setChangeFragmentInterface(ChangeFragmentInterface changeFragmentInterface) {
        this.changeFragmentInterface = changeFragmentInterface;
    }

    protected abstract int getLayout();

    protected abstract void setUpViews();

    protected ChangeFragmentInterface getChangeFragmentInterface() {
        return changeFragmentInterface;
    }

    @Nullable
    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

}