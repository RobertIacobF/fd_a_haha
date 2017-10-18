package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.changeRewardFragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.adapters.OnBoardingRewardAdapter;
import aroundwise.nepi.adapters.OnLoadMoreListener;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mihai on 29/09/16.
 */
public class ChangeRewardFragment extends BaseMvpFragment<ChangeRewardFragmentView, ChangeRewardFragmentPresenter>
        implements ChangeRewardFragmentView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.actionbar)
    CustomActionBar actionBar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private OnBoardingRewardAdapter adapter;
    private LinearLayoutManager manager;

    public static ChangeRewardFragment newInstance() {
        ChangeRewardFragment fragment = new ChangeRewardFragment();
        return fragment;
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_change_reward;
    }

    @Override
    protected void setUpViews() {
        presenter.getRewardsFromServer();
        actionBar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        actionBar.setOnOkbtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getSelectedReward() != null)
                    presenter.updateCurrentReward(adapter.getSelectedReward());
            }
        });
        actionBar.setTitleTextSize(12);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                presenter.getRewardsFromServer();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.createCompositeSubscription();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeSubscriptions();
    }

    @Override
    public ChangeRewardFragmentPresenter createPresenter() {
        return new ChangeRewardFragmentPresenter();
    }

    @Override
    public void hideProgressView() {
        hideProgress();
    }

    @Override
    public void showProgressView() {
        showProgress();
    }

    @Override
    public void displayRewards(List<Reward> rewards) {
        swipeRefreshLayout.setRefreshing(false);
        setChoosenReward(rewards);
        if (adapter == null) {
            adapter = new OnBoardingRewardAdapter(getActivity(), true, false);
            adapter.setRewards(rewards);
            adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    int offset = presenter.getRewards().size();
                    if (offset < presenter.getRewardsMeta().totalCount) {
                        presenter.getRewards().add(null);
                        adapter.notifyDataSetChanged();
                        presenter.getRewardsFromServer(offset, offset + 10);
                    }
                }
            });
            initializeRecyclerView();
        } else {
            adapter.setRewards(rewards);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void goBack() {
        Session.saveUserToSharedPref(getActivity());
        ((DiscoverActivity) getActivity()).popFragment();
    }

    @Override
    public void displayError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void initializeRecyclerView() {
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (presenter.getRewards().size() > 0) {
                    int lastVisible = manager.findLastVisibleItemPosition();
                    int offset = presenter.getRewards().size();
                    if (lastVisible == presenter.getRewards().size() - 1
                            && offset < presenter.getRewardsMeta().totalCount
                            && presenter.getRewardsMeta().next != null
                            && presenter.getRewards().get(lastVisible) != null) {
                        if (adapter.onLoadMoreListener != null)
                            adapter.onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    private void setChoosenReward(List<Reward> rewards) {
        if (Session.getUser().getProfile().getCurrent_reward() != null)
            for (Reward reward : rewards) {
                if (reward != null && reward.id == Session.getUser().getProfile().getCurrent_reward().id) {
                    reward.setSelected(true);
                    break;
                }
            }
    }

}
