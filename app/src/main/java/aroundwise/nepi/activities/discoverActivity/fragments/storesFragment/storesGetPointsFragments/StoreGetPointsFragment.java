package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storesGetPointsFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.tutorialActivity.TutorialActivity;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.eventbus.GetPointsEvent;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class StoreGetPointsFragment extends BaseFragment {

    @BindView(R.id.actionbar)
    CustomActionBar actionBar;

    @BindView(R.id.tvWalkInPoints)
    TextView tvWalkInPoints;

    @BindView(R.id.iv_store_logo)
    ImageView ivStoreLogo;

    Shop store;

    public static StoreGetPointsFragment newInstance(Shop store) {
        StoreGetPointsFragment fragment = new StoreGetPointsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SHOP, store);
        fragment.setArguments(args);
        return fragment;
    }


    public StoreGetPointsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = getArguments().getParcelable(Constants.SHOP);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_store_get_points;
    }

    @Override
    protected void setUpViews() {
        actionBar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).popFragment();
            }
        });
        tvWalkInPoints.setText(String.valueOf(store.getWalkin_points()));
        MyApplication.instance.getPicasso().load(store.getLogo()).into(ivStoreLogo);
    }

    @OnClick(R.id.btn_next)
    public void seeTutorial() {
        //EventBus.getDefault().post(new GetPointsEvent());
        Intent intent = new Intent(getActivity(), TutorialActivity.class);
        getActivity().startActivity(intent);
    }

}
