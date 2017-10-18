package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.getPointsFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.eventbus.StoreMapEvent;
import aroundwise.nepi.eventbus.StoresEvent;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class WalkInFragment extends BaseFragment {


    @BindView(R.id.actionbar)
    CustomActionBar actionBar;


    public static WalkInFragment newInstance() {
        WalkInFragment fragment = new WalkInFragment();
        return fragment;
    }

    public WalkInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_walk_in;
    }

    @Override
    protected void setUpViews() {
        actionBar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).popFragment();
            }
        });
    }

    @OnClick(R.id.btn_see_stores)
    public void seeStores() {
        EventBus.getDefault().post(new StoresEvent(true));
    }

}
