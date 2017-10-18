package aroundwise.nepi.activities.discoverActivity.fragments.getRewardsFragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.adapters.OnBoardingRewardAdapter;
import aroundwise.nepi.adapters.OnLoadMoreListener;
import aroundwise.nepi.adapters.RewardPagerAdapter;
import aroundwise.nepi.adapters.RewardsAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.network.model.Image;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.testClasses.ModelRewardAdapter;
import aroundwise.nepi.util.views.CirclePageIndicator;
import aroundwise.nepi.util.views.CustomActionBar;
import aroundwise.nepi.util.views.CustomViewPager;
import butterknife.BindView;


public class RewardsFragment extends BaseMvpFragment<RewardsFragmentView, BaseRewardsPresenter>
        implements RewardsFragmentView {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.actionbar)
    CustomActionBar actionbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    RewardsAdapter adapterRV;
    LinearLayoutManager layoutManagerRewards;

    int mode;

    public static RewardsFragment newInstance(int mode) {
        RewardsFragment fragment = new RewardsFragment();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        fragment.setArguments(args);
        return fragment;
    }

    public RewardsFragment() {
        // Required empty public constructor
    }

    @Override
    public BaseRewardsPresenter createPresenter() {
        if (mode == 1)
            return new RewardsFragmentPresenter();
        else
            return new RewardsFragmentBeneficiiPresenter();
    }

    @Override
    protected int getLayout() {
        if (getArguments() != null && getArguments().containsKey("mode"))
            mode = getArguments().getInt("mode");
        return R.layout.fragment_rewards;
    }

    @Override
    protected void setUpViews() {
        showProgress();
        switch (mode) {
            case 0:
                actionbar.setActionBarTitle("BENEFICII");
                break;
            case 1:
                actionbar.setActionBarTitle("CADOURI");
                break;
        }
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                refreshRewards();
            }
        });
        actionbar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).popFragment();
            }
        });
    }

    @Override
    public void displayRewards(List<Reward> rewards) {
        swipeLayout.setRefreshing(false);
   /*     initializePagerAdapter(getBannerRewards(rewards));
        initializeViewPager();*/
        if (adapterRV == null) {
            adapterRV = new RewardsAdapter(getActivity(), rewards, getBannerRewards(rewards));
            adapterRV.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    int offset = presenter.getRewards().size();
                    if (offset < presenter.getRewardsMeta().totalCount) {
                        presenter.getRewards().add(null);
                        presenter.getRewardsFromServer(offset, offset + 10);
                        adapterRV.notifyDataSetChanged();
                    }
                }
            });
            initializeRecyclerView();
        } else {
            adapterRV.setRewards(rewards);
            adapterRV.setBannerRewards(getBannerRewards(rewards));
            adapterRV.notifyDataSetChanged();
        }
        hideProgress();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapterRV != null
                && adapterRV.getItemCount() > 0) {
            initializeRecyclerView();
            if (adapterRV.getItemSelected() != -1)
                layoutManagerRewards.scrollToPosition(adapterRV.getItemSelected());
            hideProgress();
        } else
            presenter.getRewardsFromServer(0, 10);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void refreshRewards() {
        if (adapterRV != null)
            presenter.refreshOffers(0, adapterRV.getItemCount() - 1);
        else
            presenter.getRewardsFromServer();
    }

    private void initializeRecyclerView() {
        layoutManagerRewards = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManagerRewards);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterRV);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (presenter.getRewards().size() > 0) {
                    int lastVisible = layoutManagerRewards.findLastVisibleItemPosition();
                    int offset = presenter.getRewards().size();
                    if (lastVisible == presenter.getRewards().size() - 1
                            && offset < presenter.getRewardsMeta().totalCount
                            && presenter.getRewardsMeta().next != null
                            && presenter.getRewards().get(lastVisible) != null) {
                        if (adapterRV.onLoadMoreListener != null)
                            adapterRV.onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    private List<Reward> getBannerRewards(List<Reward> rewards) {
        List<Reward> bannerRewards = new ArrayList<>();
        for (Reward reward : rewards) {
            if (reward != null &&
                    reward.rewardImages != null && reward.rewardImages.size() > 0) {
                for (Image image : reward.rewardImages) {
                    if (image.isBanner()) {
                        bannerRewards.add(reward);
                        break;
                    }
                }
            }
        }
        return bannerRewards;
    }


}
