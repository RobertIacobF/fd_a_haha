package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.cameraActivity.CameraActivity;
import aroundwise.nepi.activities.cameraActivity.CameraActivity2;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.discoverActivity.fragments.getRewardsFragment.RewardsFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.changeRewardFragment.ChangeRewardFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.getPointsFragment.GetPointsFragment;
import aroundwise.nepi.adapters.SpinnerAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.eventbus.BlurBackgroundEvent;
import aroundwise.nepi.eventbus.RewardDetailEvent;
import aroundwise.nepi.eventbus.WalletChangeEvent;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.CustomActionBar;
import aroundwise.nepi.util.views.CustomSpinner;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;


public class LoyaltyFragment extends BaseMvpFragment<LoyaltyFragmentView, LoyaltyFragmentPresenter>
        implements LoyaltyFragmentView {

    @BindView(R.id.actionbar)
    CustomActionBar actionbar;

    @BindView(R.id.loyalty_iv_reward)
    CircleImageView civLoyalty;

    @BindView(R.id.tv_points)
    TextView tvPoints;

    @BindView(R.id.tv_need_points)
    TextView tvNeedPoints;

    @BindView(R.id.iv_profile)
    ImageView ivProfile;

    @BindView(R.id.loyalty_reward_progress)
    DonutProgress rewardProgress;

    private SpinnerAdapter spinnerAdapter;

    Target addGradientTarget;
    final int REQUEST = 100;
    private boolean firstTimeUserSpinner = true;

    public static LoyaltyFragment newInstance() {
        LoyaltyFragment fragment = new LoyaltyFragment();
        return fragment;
    }

    public LoyaltyFragment() {
        // Required empty public constructor
    }

    @Override
    public LoyaltyFragmentPresenter createPresenter() {
        return new LoyaltyFragmentPresenter();
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_loyalty;
    }

    @Override
    protected void setUpViews() {
        addGradientTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap updatedBitmap = presenter.addGradient(bitmap);
                ivProfile.setImageBitmap(updatedBitmap);
                // Blurry.with(getActivity()).sampling(2).radius(25).from(bitmap).into(ivProfile);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.e(LoyaltyFragment.class.getSimpleName(), "Eroare bitmap");
                ivProfile.setBackgroundColor(Color.parseColor("#66f80a7a"));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        initializeSpinner();
    }

    private void initializeSpinner() {
        spinnerAdapter = new SpinnerAdapter(getActivity(), Session.getMalls(), getContext().getString(R.string.loyalty_caps));
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
                    }
                    spinnerAdapter.notifyDataSetChanged();
                    actionbar.getSpinner().setSelection(0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        actionbar.getSpinner().setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
            }

            @Override
            public void onSpinnerClosed() {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        initialize();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.btn_getMorePoints)
    public void getMorePoints() {
        ((DiscoverActivity) getActivity()).changeFragment(GetPointsFragment.newInstance());
    }

    @OnClick(R.id.btn_getRewards)
    public void getRewards() {
        ((DiscoverActivity) getActivity()).changeFragment(RewardsFragment.newInstance(1));
    }

    @OnClick(R.id.btn_scan_receipt)
    public void scanReceipt() {
        if (askForPermissions()) {
            Intent intent = new Intent(getActivity(), CameraActivity2.class);
            intent.putExtra(Constants.SHOW_TUTORIAL, true);
            startActivity(intent);
        }
    }

    @OnClick(R.id.loyalty_iv_reward)
    public void goToCurrentReward() {
        ((DiscoverActivity) getActivity()).changeFragment(ChangeRewardFragment.newInstance());
    }

    @OnClick(R.id.tv_change_reward)
    public void changeReward() {
        ((DiscoverActivity) getActivity()).changeFragment(ChangeRewardFragment.newInstance());
    }

    @OnClick(R.id.btn_beneficii)
    public void beneficii() {
        ((DiscoverActivity) getActivity()).changeFragment(RewardsFragment.newInstance(0));
    }

    private boolean askForPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST);
            return false;
        }
        return true;
    }


    private void initialize() {
        if (Session.getUser().getProfile().getImage() != null
                && !Session.getUser().getProfile().getImage().isEmpty())
            MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getImage()).placeholder(R.drawable.placeholder)
                    .into(addGradientTarget);
        setRewardImage();
        tvPoints.setText(String.valueOf(Session.getUser().getProfile().getPoints()));

    }

    private void setRewardImage() {
        if (Session.getUser().getProfile() != null && Session.getUser().getProfile().getCurrent_reward() != null
                && Session.getUser().getProfile().getCurrent_reward().rewardImages != null
                && Session.getUser().getProfile().getCurrent_reward().store.mall.getId() == Session.getUser().getProfile().getMall().getId()) {
            rewardProgress.setVisibility(View.VISIBLE);
            MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getCurrent_reward().rewardImages.get(0).getImage()).fit().into(civLoyalty);
            long pointsNeeded = Session.getUser().getProfile().getCurrent_reward().pointCost - Session.getUser().getProfile().getPoints();
            int pointsRewardCosts = Session.getUser().getProfile().getCurrent_reward().pointCost;
            int pointsUserHas = Session.getUser().getProfile().getPoints();
            tvNeedPoints.setText("You need " + pointsNeeded + " points");
            rewardProgress.setMax(pointsRewardCosts);
            if (pointsNeeded <= 0)
                rewardProgress.setProgress(rewardProgress.getMax());
            else
                rewardProgress.setProgress(pointsUserHas);
        } else {
            MyApplication.instance.getPicasso().load(R.drawable.no_reward).fit().into(civLoyalty);
            rewardProgress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[grantResults.length - 1] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(getActivity(), CameraActivity2.class);
            intent.putExtra(Constants.SHOW_TUTORIAL, true);
            startActivity(intent);
        }
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
    public void mallsSwitched() {
        setRewardImage();
    }

    public void onEvent(WalletChangeEvent event) {
        tvPoints.setText(String.valueOf(Session.getUser().getProfile().getPoints()));
    }
}
