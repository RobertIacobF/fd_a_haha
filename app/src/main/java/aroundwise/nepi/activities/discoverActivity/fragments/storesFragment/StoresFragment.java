package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.adapters.OnLoadMoreListener;
import aroundwise.nepi.adapters.SpinnerAdapter;
import aroundwise.nepi.adapters.StoreAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.eventbus.BlurBackgroundEvent;
import aroundwise.nepi.eventbus.StoreMapEvent;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import aroundwise.nepi.util.views.CustomActionBar;
import aroundwise.nepi.util.views.CustomSpinner;
import butterknife.BindView;
import de.greenrobot.event.EventBus;


public class StoresFragment extends BaseMvpFragment<StoresFragmentView, StoresFragmentPresenter>
        implements StoresFragmentView {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.actionbar)
    CustomActionBar actionbar;


    private SpinnerAdapter spinnerAdapter;
    private StoreAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean firstTimeUserSpinner = true;
    boolean walkinStores;

    public static StoresFragment newInstance(boolean walkIn) {
        StoresFragment fragment = new StoresFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.WALK_IN, walkIn);
        fragment.setArguments(args);
        return fragment;
    }

    public StoresFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walkinStores = getArguments().getBoolean(Constants.WALK_IN);

    }

    @Override
    public StoresFragmentPresenter createPresenter() {
        return new StoresFragmentPresenter();
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_stores;
    }

    @Override
    protected void setUpViews() {
        initializeSpinner();
        presenter.setWalkIn(walkinStores);
        showProgress();
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                refreshStores();
            }
        });
        actionbar.setBtnLocationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new StoreMapEvent(null, presenter.getShops()));
            }
        });
        actionbar.getSpinner().setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                EventBus.getDefault().post(new BlurBackgroundEvent(true));
            }

            @Override
            public void onSpinnerClosed() {
                EventBus.getDefault().post(new BlurBackgroundEvent(false));
            }
        });
        actionbar.getEtSearch().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Util.hideSoftKeyboard(getActivity());
                    presenter.searchStores(Session.getUser().getProfile().getMall().getId(), v.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void displayShops(List<Shop> shops, boolean scroolToTop) {
        swipeLayout.setRefreshing(false);
        if (adapter == null) {
            adapter = new StoreAdapter(getActivity(), shops);
            initializeRecyclerView();
        } else {
            adapter.notifyDataSetChanged();
        }
        if (scroolToTop)
            recyclerView.smoothScrollToPosition(0);
    }

    private void initializeSpinner() {
        spinnerAdapter = new SpinnerAdapter(getActivity(), Session.getMalls(), getContext().getString(R.string.magazin_caps));
        spinnerAdapter.setStore(true);
        actionbar.getSpinner().setAdapter(spinnerAdapter);
        actionbar.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (firstTimeUserSpinner)
                    firstTimeUserSpinner = false;
                else {
                    if (i != 0) {
                        presenter.changeMall(Session.getMalls().get(i));
                        Session.switchMalls(i);
                        presenter.requestForShops();

                    }
                    actionbar.getSpinner().setSelection(0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.createCompositeSubscription();
        if (presenter.getShops().isEmpty())
            presenter.requestForShops();
        else {
            hideProgress();
            initializeRecyclerView();
            if (adapter != null && adapter.getSelectedStore() != -1)
                recyclerView.scrollToPosition(adapter.getSelectedStore());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeSubscriptions();
    }

    @Override
    public void hideProgressBar() {
        hideProgress();
    }

    @Override
    public void showProgressBar() {
        showProgress();
    }

    private void refreshStores() {
        presenter.requestForShops();
    }

    private void initializeRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                int offset = presenter.getShops().size();
                presenter.getShops().add(null);
                adapter.notifyDataSetChanged();
                presenter.requestForShops(offset, offset + 2);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                final List<Shop> shops = presenter.getShops();
                try {
                    if (lastVisible == shops.size() - 1
                            && lastVisible < shops.size()
                            && shops.get(lastVisible) != null
                            && presenter.getShopsMeta() != null
                            && presenter.getShopsMeta().next != null) {
                        if (adapter.getOnLoadMoreListener() != null)
                            adapter.getOnLoadMoreListener().onLoadMore();
                    }
                } catch (ArrayIndexOutOfBoundsException exception) {

                }
            }
        });
    }

}
