package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.getPointsFragment;


import android.view.View;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;
import butterknife.OnClick;


public class GetPointsFragment extends BaseFragment {

    @BindView(R.id.actionbar)
    CustomActionBar actionBar;

    public static GetPointsFragment newInstance() {
        GetPointsFragment fragment = new GetPointsFragment();
        return fragment;
    }

    public GetPointsFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_get_points;
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

    @OnClick(R.id.btn_walk_in)
    public void walkIn() {
        ((DiscoverActivity) getActivity()).changeFragment(WalkInFragment.newInstance());
    }

    @OnClick(R.id.btn_purchase)
    public void purchase() {
        ((DiscoverActivity) getActivity()).changeFragment(PurchaseFragment.newInstance());
    }

    @OnClick(R.id.btn_site)
    public void site() {
        ((DiscoverActivity) getActivity()).changeFragment(WebsiteFragment.newInstance());
    }

}
