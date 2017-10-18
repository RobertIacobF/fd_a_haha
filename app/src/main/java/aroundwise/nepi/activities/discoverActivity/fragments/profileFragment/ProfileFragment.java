package aroundwise.nepi.activities.discoverActivity.fragments.profileFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.discoverActivity.fragments.profileFragment.fragments.ProfileSettingsFragment;
import aroundwise.nepi.adapters.ActivityAdapter;
import aroundwise.nepi.adapters.BadgesAdapter;
import aroundwise.nepi.adapters.EmptyAdapter;
import aroundwise.nepi.adapters.MyRewardAdapter;
import aroundwise.nepi.adapters.OfferAdapter;
import aroundwise.nepi.adapters.OnBoardingRewardAdapter;
import aroundwise.nepi.adapters.ProfileOfferAdapter;
import aroundwise.nepi.adapters.SpinnerAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.eventbus.BlurBackgroundEvent;
import aroundwise.nepi.eventbus.GetPointsEvent;
import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.eventbus.OffersListEmptyEvent;
import aroundwise.nepi.eventbus.WalletChangeEvent;
import aroundwise.nepi.network.model.Badge;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.model.UserActivity;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.CustomSpinner;
import aroundwise.nepi.util.views.SAutoBgButton;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends BaseMvpFragment<ProfileFragmentView, ProfileFragmentPresenter>
        implements ProfileFragmentView {

    //region BindView


    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.tv_points)
    TextView tv_points;

    @BindView(R.id.tv_first_name)
    TextView tv_first_name;

    @BindView(R.id.tv_last_name)
    TextView tv_last_name;

    @BindView(R.id.btn_get_more_points)
    SAutoBgButton btn_get_points;

    @BindView(R.id.btn_tab1)
    Button btn_tab1;

    @BindView(R.id.btn_tab2)
    Button btn_tab2;

    @BindView(R.id.btn_tab3)
    Button btn_tab3;

    @BindView(R.id.btn_tab4)
    Button btn_tab4;

    @BindView(R.id.tv_tab1)
    TextView tv_tab1;

    @BindView(R.id.tv_tab2)
    TextView tv_tab2;

    @BindView(R.id.tv_tab3)
    TextView tv_tab3;

    @BindView(R.id.tv_tab4)
    TextView tv_tab4;

    @BindView(R.id.tab1_line)
    View tab_line1;

    @BindView(R.id.tab2_line)
    View tab_line2;

    @BindView(R.id.tab3_line)
    View tab_line3;

    @BindView(R.id.tab4_line)
    View tab_line4;

    @BindView(R.id.badgeRecyclerView)
    RecyclerView badgeRecyclerView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.iv_profile)
    ImageView iv_profile;

    @BindView(R.id.spinner)
    CustomSpinner spinner;

    @BindView(R.id.ivCorporateCode)
    CircleImageView ivCorporateCode;
    //endregion

    private GridLayoutManager gridLayoutManager;
    private ProfileOfferAdapter offerAdapter;
    private MyRewardAdapter rewardAdapter;
    private ActivityAdapter activityAdapter;
    private BadgesAdapter badgesAdapter;
    private SpinnerAdapter spinnerAdapter;

    private int currentTab = 0;
    private int recyclerViewHeight = 0;
    private boolean firstTimeUserSpinner = true;

    private List<Badge> badgesList;


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public static ProfileFragment newInstance(int mode) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_profile_2;
    }

    @Override
    protected void setUpViews() {
        if (getArguments() != null
                && getArguments().containsKey("mode")) {
            currentTab = 1;
        }
        EventBus.getDefault().unregister(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        recyclerViewHeight = recyclerView.getHeight();
        tv_first_name.setText(Session.getUser().getFirst_name());
        tv_last_name.setText(Session.getUser().getLast_name());
        if (Session.getUser().getProfile() != null)
            tv_points.setText(Session.getUser().getProfile().getPoints() + " PUNCTE");
        initializeBadgeAdapter();
        initializeSpinner();
        if (Session.getUser().getProfile().isCorporateCodeValidated()) {
            MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getCorporateLogo()).into(ivCorporateCode);
        }
        if (Session.getUser().getProfile().getMall().getName().equalsIgnoreCase("promenada")) {
            ivCorporateCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ProfileFragmentPresenter createPresenter() {
        return new ProfileFragmentPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void displayFavoriteOffers(List<Offer> offers) {
        if (offers.size() != 0) {
            offerAdapter = new ProfileOfferAdapter(getActivity(), offers);
            offerAdapter.setInFavoriteScreen(true);
            initializeRecyclerViewForFavoriteOffers();
        } else {
            EmptyAdapter emptyAdapter = new EmptyAdapter(getActivity(), "Nu ai oferte favorite");
            initializeEmptyRecyclerView(emptyAdapter);
        }
    }

    @Override
    public void displayActivities(List<UserActivity> activities) {
        activityAdapter = new ActivityAdapter(getActivity(), activities);
        initializeRecyclerViewForActivities();
    }

    @Override
    public void displayMyRewards(List<Reward> userRewards) {
        if (userRewards != null && userRewards.size() != 0) {
            rewardAdapter = new MyRewardAdapter(getActivity(), userRewards);
            initializeRecyclerViewForRewards();
        } else {
            EmptyAdapter emptyAdapter = new EmptyAdapter(getActivity(), "");
            initializeEmptyRecyclerView(emptyAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.closeSubscription();
        }
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.PROFILE_TAB, currentTab);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Session.getUser() == null || Session.getUser().getProfile() == null)
            Session.readUserFromSharedPref(getActivity());
        if (Session.getUser().getProfile() != null)
            MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getImage()).fit().centerCrop()
                    .into(iv_profile);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFFERENCES, Context.MODE_PRIVATE);
        currentTab = sharedPreferences.getInt(Constants.PROFILE_TAB, 0);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        selectTab(currentTab);
    }

    public void onEvent(WalletChangeEvent event) {
        tv_points.setText(String.valueOf(Session.getUser().getProfile().getPoints()));
    }

    private void initializeBadgeAdapter() {
        badgesList = new ArrayList<>();
        badgesList.add(new Badge(R.drawable.badge_hunter, "Hunter"));
        badgesList.add(new Badge(R.drawable.badge_super_visiter, "Super visiter"));

        badgesAdapter = new BadgesAdapter(getContext(), badgesList);
        badgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        badgeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        badgeRecyclerView.setAdapter(badgesAdapter);
    }

    private void initializeSpinner() {
        spinnerAdapter = new SpinnerAdapter(getActivity(), Session.getMalls(), getContext().getString(R.string.my_profile_caps));
        spinnerAdapter.setMallNameTextColorResource(R.color.white50alpha);
        spinnerAdapter.setMyprofile(true);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (firstTimeUserSpinner)
                    firstTimeUserSpinner = false;
                else {
                    if (i != 0) {
                        presenter.changeMall(Session.getMalls().get(i));
                        Session.switchMalls(i);
                    }
                    spinnerAdapter.notifyDataSetChanged();
                    spinner.setSelection(0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                EventBus.getDefault().post(new BlurBackgroundEvent(true));
                spinnerAdapter.setMallNameTextColorResource(0);
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSpinnerClosed() {
                EventBus.getDefault().post(new BlurBackgroundEvent(false));
                spinnerAdapter.setMallNameTextColorResource(1);
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }


    private void deselectTab(TextView tv, View line) {
        int deselectedColor = ContextCompat.getColor(getActivity(), R.color.tab_non_selected_color);
        tv.setTextColor(deselectedColor);
        line.setBackgroundColor(Color.TRANSPARENT);
    }

    private void selectTab(TextView tv, View line) {
        int selectedColor = ContextCompat.getColor(getActivity(), R.color.tab_selected_color);
        tv.setTextColor(selectedColor);
        line.setBackgroundColor(selectedColor);
    }

    private void selectTab(int index) {
        deselectTab(tv_tab1, tab_line1);
        deselectTab(tv_tab2, tab_line2);
        deselectTab(tv_tab3, tab_line3);
        deselectTab(tv_tab4, tab_line4);

        switch (index) {
            case 0:
                selectTab(tv_tab1, tab_line1);
                prepareOffers();
                currentTab = 0;
                break;
            case 1:
                selectTab(tv_tab2, tab_line2);
                prepareActivities();
                currentTab = 1;
                break;
            case 2:
                selectTab(tv_tab3, tab_line3);
                currentTab = 2;
                prepareRewards();
                break;
            case 3:
                selectTab(tv_tab4, tab_line4);
                initializeEmptyRecyclerView(new EmptyAdapter(getContext(), "Nu ai cupoane"));
                currentTab = 3;
                break;
            default:
                break;
        }
    }

    private void prepareActivities() {
        presenter.requestActivities();
    }

    private void prepareRewards() {
        presenter.requestMyRewards();
    }

    private void prepareOffers() {
        presenter.requestFavoriteOffers();
    }

    private void initializeRecyclerViewForFavoriteOffers() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0) {
                    return 4;
                } else {
                    GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
                    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if (position % 3 == 0) {
                                return 4;
                            } else {
                            }
                            return 2;
                        }
                    });
                }
                return 2;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setBackgroundColor(Color.TRANSPARENT);
        recyclerView.setAdapter(offerAdapter);
        if (offerAdapter.getPositionSelected() != -1)
            gridLayoutManager.scrollToPosition(offerAdapter.getPositionSelected());

    }

    private void initializeRecyclerViewForRewards() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rewardAdapter);
    }

    private void initializeRecyclerViewForActivities() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(activityAdapter);
    }


    public void onEvent(OffersListEmptyEvent event) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        appBarLayout.scrollTo(0, 0);
        if (behavior != null) {
            behavior.setTopAndBottomOffset(0);
            behavior.onNestedPreScroll(getCoordinatorLayout(), appBarLayout, null, 0, 1, new int[2]);
        }
        EmptyAdapter emptyAdapter = new EmptyAdapter(getActivity(), event.getMessage());
        initializeEmptyRecyclerView(emptyAdapter);
    }

    public void onEvent(MallHasChangedEvent event) {
        presenter.getUserWallet();
        offerAdapter = null;
        rewardAdapter = null;

        switch (currentTab) {
            case 0:
                prepareOffers();
                break;
            case 2:
                prepareRewards();
                break;
        }
        if (Session.getUser().getProfile().getMall().getName().equalsIgnoreCase("promenada")) {
            ivCorporateCode.setVisibility(View.VISIBLE);
        } else {
            ivCorporateCode.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeEmptyRecyclerView(EmptyAdapter emptyAdapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(emptyAdapter);
    }

    @OnClick(R.id.btn_get_more_points)
    public void getPoints() {
        EventBus.getDefault().post(new GetPointsEvent());
    }

    @OnClick(R.id.btn_tab1)
    public void clickTab1() {
        selectTab(0);
    }

    @OnClick(R.id.btn_tab2)
    public void clickTab2() {
        selectTab(1);
    }

    @OnClick(R.id.btn_tab3)
    public void clickTab3() {
        selectTab(2);
    }

    @OnClick(R.id.btn_tab4)
    public void clickTab4() {
        selectTab(3);
    }

    @OnClick(R.id.btn_settings)
    public void settings() {
        ((DiscoverActivity) getActivity()).changeFragment(ProfileSettingsFragment.newInstance());
    }

    @Override
    public void setPoints() {
        if (Session.getUser().getProfile() != null)
            tv_points.setText(Session.getUser().getProfile().getPoints() + " PUNCTE");
    }

    @Override
    public void hideProgressDialog() {
        hideProgress();
    }

    @Override
    public void showProgressDialog() {
        showProgress();
    }
}
