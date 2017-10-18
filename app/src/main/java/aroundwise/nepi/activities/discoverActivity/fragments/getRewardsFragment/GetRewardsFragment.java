package aroundwise.nepi.activities.discoverActivity.fragments.getRewardsFragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.adapters.NetworkViewPagerAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.eventbus.GetPointsEvent;
import aroundwise.nepi.eventbus.StoreDetailsEvent;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.CirclePageIndicator;
import aroundwise.nepi.util.views.CustomViewPager;
import aroundwise.nepi.util.views.SAutoBgButton;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class GetRewardsFragment extends BaseMvpFragment<GetRewardsFragmentView, GetRewardsFragmentPresenter>
        implements GetRewardsFragmentView {

    @BindView(R.id.actionbar_btn_back)
    Button btn_header_back;

    @BindView(R.id.btn_back)
    SAutoBgButton btn_back;

    @BindView(R.id.viewpager)
    CustomViewPager viewpager;

    @BindView(R.id.viewpager_indicator)
    CirclePageIndicator view_pager_indicator;

    @BindView(R.id.tv_reward_title)
    TextView tv_reward_title;

    @BindView(R.id.tv_reward_description)
    TextView tv_reward_description;

    @BindView(R.id.btn_get_points)
    SAutoBgButton btn_get_points;

    @BindView(R.id.rl_reward_header)
    RelativeLayout rl_reward_header;

    @BindView(R.id.tv_code)
    TextView tv_code;

    @BindView(R.id.tv_points)
    TextView tvPoints;

    @BindView(R.id.btn_like)
    Button btnLike;

    @BindView(R.id.offer_header)
    RelativeLayout rlStoreName;

    @BindView(R.id.tv_offer_owner)
    TextView tvStore;

    @BindView(R.id.iv_offer_logo)
    ImageView ivStoreLogo;

    @BindView(R.id.iv_offer_mall_logo)
    ImageView ivMallLogo;

    @BindView(R.id.tvBuc)
    TextView tvNrOfBuc;

    @BindView(R.id.tv_expire)
    TextView tvExpireDate;

    @BindView(R.id.glow)
    ImageView ivGlow;

    @BindView(R.id.rl_btn_glow)
    RelativeLayout rlBtnGlow;

    @BindView(R.id.frame_badge)
    FrameLayout frameBadge;

    Reward reward;
    NetworkViewPagerAdapter adapter;

    public static GetRewardsFragment newInstance(Reward rewardPagerAdapter) {
        GetRewardsFragment fragment = new GetRewardsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.REWARD, rewardPagerAdapter);
        fragment.setArguments(args);
        return fragment;
    }


    public GetRewardsFragment() {
        // Required empty public constructor
    }

    @Override
    public GetRewardsFragmentPresenter createPresenter() {
        return new GetRewardsFragmentPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reward = getArguments().getParcelable(Constants.REWARD);
    }

    private void initializeViewPager() {
        adapter = new NetworkViewPagerAdapter(reward.rewardImages, getActivity());
        viewpager.setAdapter(adapter);
        view_pager_indicator.setViewPager(viewpager);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_get_rewards;
    }

    @Override
    protected void setUpViews() {
        tv_reward_title.setText(reward.title);
        tv_reward_description.setText(reward.description);
        tvPoints.setText(String.valueOf(reward.pointCost) + " PUNCTE");
        initializeViewPager();
        if (reward.pointCost == 0) {
            frameBadge.setVisibility(View.INVISIBLE);
            btn_get_points.setText("Folosește cod de bare");
            btn_get_points.setAllCaps(false);
            rlBtnGlow.setVisibility(View.VISIBLE);
            btn_get_points.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reward.rewardImages.size() > 1)
                        viewpager.setCurrentItem(1, true);
                }
            });
            ivGlow.setVisibility(View.INVISIBLE);
        }
        if (Session.getUser().getProfile().getPoints() >= reward.pointCost && reward.pointCost != 0) {
            btn_get_points.setActivated(true);
            btn_get_points.setText("CERE CADOU");
            ivGlow.setVisibility(View.VISIBLE);
        }
        if (Session.getUser().getProfile().getCurrent_reward() != null
                && Session.getUser().getProfile().getCurrent_reward().id == reward.id) {
            btnLike.setActivated(true);
            reward.setSelected(true);
        }
        rlStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new StoreDetailsEvent(reward.store));
            }
        });
        MyApplication.instance.getPicasso().load(reward.store.getLogo()).into(ivStoreLogo);
        tvStore.setText(reward.store.getName());
        MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getMall().getLogo()).into(ivMallLogo);
        if (reward.code != null && reward.statusReward.equalsIgnoreCase("pending")) {
            showCodeHeader(reward.code);
            btn_get_points.setVisibility(View.INVISIBLE);
            rlBtnGlow.setVisibility(View.INVISIBLE);
        }
        tvNrOfBuc.setText(String.valueOf(reward.stockRemaining));
        if (reward.stockRemaining <= 0 && reward.pointCost != 0) {
            btn_get_points.setVisibility(View.INVISIBLE);
            rlBtnGlow.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.btn_back)
    public void goback() {
        ((DiscoverActivity) getActivity()).popFragment();
    }

    @OnClick(R.id.actionbar_btn_back)
    public void goback2() {
        ((DiscoverActivity) getActivity()).popFragment();
    }

    @OnClick(R.id.btn_get_points)
    public void getPoints() {
        if (btn_get_points.isActivated())
            presenter.claimReward(reward);
        else
            EventBus.getDefault().post(new GetPointsEvent());
    }

    @OnClick(R.id.btn_like)
    public void likeReward() {
        if (!btnLike.isActivated()) {
            btnLike.setActivated(true);
            presenter.updateCurrentReward(reward);
        }
    }

    @Override
    public void updateRewardCode(String code, String status) {
        showCodeHeader(code);
        reward.code = code;
        reward.statusReward = status;
        btn_get_points.setVisibility(View.INVISIBLE);
        rlBtnGlow.setVisibility(View.INVISIBLE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 3);
        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String dateString = sdf.format(date);
        String text = "Arată codul până la " + dateString + " la orice infodesk din Promenada/Mega Mall pentru a rdica cadoul.";
        tvExpireDate.setText(text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Felicitări")
                .setMessage("Poți găsi cadoul în pagina de profil.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }

    @Override
    public void hideProgressBar() {
        hideProgress();
    }

    @Override
    public void showProgressBar() {
        showProgress();
    }

    private void showCodeHeader(String code) {
        btn_back.setVisibility(View.GONE);
        rl_reward_header.setVisibility(View.VISIBLE);
        tv_code.setText(code);
    }

    private void hideCodeHeader() {
        btn_back.setVisibility(View.VISIBLE);
        rl_reward_header.setVisibility(View.GONE);
    }


}
