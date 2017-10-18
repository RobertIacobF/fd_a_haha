package aroundwise.nepi.activities.onBoardActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.mainActivity.MainActivity;
import aroundwise.nepi.adapters.OnBoardingRewardAdapter;
import aroundwise.nepi.adapters.OnLoadMoreListener;
import aroundwise.nepi.base.BaseMvpActivity;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import butterknife.BindView;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OnBoardActivity extends BaseMvpActivity<OnBoardActivityView, OnBoardActivityPresenter>
        implements OnBoardActivityView {

    private List<Integer> images, icons;
    private List<String> first, second;
    private int currentImage = 0;
    private boolean learnMorePressed = false;
    private boolean isRecyclerViewShown = false;
    private OnBoardingRewardAdapter adapter;

    @BindView(R.id.iv_discover)
    ImageView iv_discover;

    @BindView(R.id.tv_learn_more)
    TextView tv_learn_more;

    @BindView(R.id.layout_recyclerView)
    RelativeLayout recyclerLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.iv_overlay)
    ImageView iv_overlay;

    @BindView(R.id.iv_icon)
    ImageView ivIcon;

    @BindView(R.id.tv_first)
    TextView tvFirst;

    @BindView(R.id.tv_second)
    TextView tvSecond;

    ProgressDialog progressDialog;
    LinearLayoutManager manager;

    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        //isFirstLaunch();
        initialize();
        MyApplication.instance.getPicasso().load(images.get(currentImage)).into(iv_discover);
        MyApplication.instance.getPicasso().load(icons.get(currentImage)).into(ivIcon);
        tvFirst.setText(first.get(currentImage));
        tvSecond.setText(second.get(currentImage));
        if (Session.getUser() == null || Session.getUser().getProfile() == null)
            Session.readUserFromSharedPref(this);
        presenter.getRewardsFromServer();

    }

    private void isFirstLaunch() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFFERENCES, Context.MODE_PRIVATE);
        firstTime = sharedPreferences.getBoolean(Constants.FIRST_LAUNCH, true);
        if (firstTime) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.FIRST_LAUNCH, false);
            editor.apply();
        } else {
            nextStep();
        }
    }

    @NonNull
    @Override
    public OnBoardActivityPresenter createPresenter() {
        return new OnBoardActivityPresenter();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initialize() {
        images = new ArrayList<>();
        images.add(R.drawable.discover_1);
        images.add(R.drawable.discover_2);
        images.add(R.drawable.discover_2_1);
        images.add(R.drawable.discover_2_2);
        images.add(R.drawable.discover_2_3);
        images.add(R.drawable.discover_3);
        images.add(R.drawable.onboard_new);
        images.add(R.drawable.discover_4);

        icons = new ArrayList<>();
        icons.add(R.drawable.icon_onboard_1);
        icons.add(R.drawable.icon_onboard_2_3);
        icons.add(R.drawable.icon_onboard_2_1);
        icons.add(R.drawable.icon_onboard_2_2);
        icons.add(R.drawable.icon_onboard_2_3);
        icons.add(R.drawable.icon_onboard_3);
        icons.add(R.drawable.icon_loyalty_purchase);
        icons.add(R.drawable.icon_onboard_4);
        icons.add(R.drawable.icon_onboard_5);

        first = new ArrayList<>();
        first.add(getString(R.string.onboard_first_1));
        first.add(getString(R.string.onboard_first_2));
        first.add(getString(R.string.onboard_first_2_1));
        first.add(getString(R.string.onboard_first_2_2));
        first.add(getString(R.string.onboard_first_2_3));
        first.add(getString(R.string.onboard_first_3));
        first.add(getString(R.string.onboard_first_3_1));
        first.add(getString(R.string.onboard_first_4));
        first.add(getString(R.string.onboard_first_2_3));

        second = new ArrayList<>();
        second.add(getString(R.string.onboard_second_1));
        second.add(getString(R.string.onboard_second_2));
        second.add(getString(R.string.onboard_second_2_1));
        second.add(getString(R.string.onboard_second_2_2));
        second.add(getString(R.string.onboard_second_2_3));
        second.add(getString(R.string.onboard_second_3));
        second.add(getString(R.string.onboard_second_3_1));
        second.add(getString(R.string.onboard_second_4));
        second.add(getString(R.string.onboard_second_2_3));


    }

    private void initializeRecyclerView() {
        manager = new LinearLayoutManager(this);
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


    @OnClick(R.id.btn_next)
    public void next() {
        if (isRecyclerViewShown && adapter != null && adapter.isItemChoosed()) {
            showProgress();
            Session.setOnboardReward(presenter.getRewards().get(adapter.getItemSelected()));
            presenter.updateCurrentReward(Session.getOnboardReward(), this);
        } else if (isRecyclerViewShown)
            Util.createSnackbar(getCoordinatorLayout(), "Alegeți cadoul acum. Îl puteți schimba mai încolo!");
        if (currentImage < images.size() - 1) {
            currentImage++;
            MyApplication.instance.getPicasso().load(images.get(currentImage)).into(iv_discover);
            MyApplication.instance.getPicasso().load(icons.get(currentImage)).into(ivIcon);
            tvFirst.setText(first.get(currentImage));
            tvSecond.setText(second.get(currentImage));
        } else {
            isRecyclerViewShown = true;
            recyclerLayout.setVisibility(View.VISIBLE);
            iv_discover.setVisibility(View.INVISIBLE);
            MyApplication.instance.getPicasso().load(icons.get(icons.size() - 1)).into(ivIcon);
            tvFirst.setText(first.get(first.size() - 1));
            tvSecond.setText(second.get(second.size() - 1));
        }

        if (currentImage == 1) {
            currentImage = 4;
            learnMorePressed = false;
            tv_learn_more.setVisibility(View.VISIBLE);
        } else {
            tv_learn_more.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.tv_learn_more)
    public void showLearnMore() {
        learnMorePressed = true;
        currentImage = 2;
        MyApplication.instance.getPicasso().load(images.get(currentImage)).into(iv_discover);
        tv_learn_more.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (!previous())
            super.onBackPressed();
    }

    private boolean previous() {
        if (isRecyclerViewShown) {
            recyclerLayout.setVisibility(View.INVISIBLE);
            iv_discover.setVisibility(View.VISIBLE);
            isRecyclerViewShown = false;
            MyApplication.instance.getPicasso().load(images.get(currentImage)).into(iv_discover);
            MyApplication.instance.getPicasso().load(icons.get(currentImage)).into(ivIcon);
            tvFirst.setText(first.get(currentImage));
            tvSecond.setText(second.get(currentImage));
            return true;
        }
        if (currentImage > 0) {
            if (!learnMorePressed) {
                if (currentImage == 5)
                    currentImage = 1;
                else if (currentImage == 4)
                    currentImage = 0;
                else
                    currentImage--;
            } else
                currentImage--;
            MyApplication.instance.getPicasso().load(images.get(currentImage)).into(iv_discover);
            MyApplication.instance.getPicasso().load(icons.get(currentImage)).into(ivIcon);
            tvFirst.setText(first.get(currentImage));
            tvSecond.setText(second.get(currentImage));
            tv_learn_more.setVisibility(View.INVISIBLE);
            if (currentImage == 1) {
                learnMorePressed = false;
                tv_learn_more.setVisibility(View.VISIBLE);
            }
            return true;
        } else
            return false;
    }


    @Override
    public void displayRewards(List<Reward> rewards) {
        if (adapter == null) {
            adapter = new OnBoardingRewardAdapter(this, true, false);
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
    public void nextStep() {
        hideProgress();
        intentStarter.startActivity(this, DiscoverActivity.class);
        finish();
    }

    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        progressDialog.show();
        progressDialog.setContentView(R.layout.simple_progress_dialog);
    }

    public void hideProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
