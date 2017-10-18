package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeDetailFragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.adapters.StoreDetailsAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.dialogs.StoreInfoDialog;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;
import butterknife.OnClick;


public class StoreDetailFragment extends BaseMvpFragment<StoreDetailFragmentView, StoreDetailFragmentPresenter>
        implements StoreDetailFragmentView {

   /* @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;*/

    @BindView(R.id.actionbar)
    CustomActionBar actionBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    StoreDetailsAdapter storeDetailsAdapter;
    private Shop store;


    public static StoreDetailFragment newInstance(Shop shop) {
        StoreDetailFragment fragment = new StoreDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SHOP, shop);
        fragment.setArguments(args);
        return fragment;
    }

    public StoreDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public StoreDetailFragmentPresenter createPresenter() {
        return new StoreDetailFragmentPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = getArguments().getParcelable(Constants.SHOP);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_store_detail;
    }

    @Override
    protected void setUpViews() {
        initializeActionbar();
        initialize();
    }

    private void initializeActionbar() {
        actionBar.setTitleTypeFace(Typeface.createFromAsset(getContext().getAssets(), "fonts/ProximaNova-Semibold.otf"));
        actionBar.setActionBarTitle(store.getName().toUpperCase());
        actionBar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).popFragment();
            }
        });
    }

    private void initialize() {
        if (store.getImages() != null)
            presenter.getAllData(store);
        else
            presenter.getShopDetails(store.id);
    }


    @OnClick(R.id.btn_info)
    public void showStoreInfo() {
        StoreInfoDialog dialog = StoreInfoDialog.newInstance(store);
        dialog.show(getActivity().getSupportFragmentManager(), "tag");
    }


    @Override
    public void displayStoreRewards(List<Reward> rewards) {

    }

    @Override
    public void displayStoreOffers(List<Offer> offers) {


    }

    @Override
    public void setStore(Shop shop) {
        store = shop;
    }

    @Override
    public void displayData(List<Offer> offers, List<Reward> rewards) {
        storeDetailsAdapter = new StoreDetailsAdapter(getActivity(), store, rewards, offers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(storeDetailsAdapter);
    }

    @Override
    public void showProgressBar() {
        showProgress();
    }

    @Override
    public void hideProgressBar() {
        hideProgress();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
