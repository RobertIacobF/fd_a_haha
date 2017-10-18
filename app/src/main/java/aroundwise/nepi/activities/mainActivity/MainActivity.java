package aroundwise.nepi.activities.mainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.mainActivity.fragments.mainFragment.MainFragment;
import aroundwise.nepi.adapters.ViewPagerAdapterOnlyText;
import aroundwise.nepi.base.BaseActivity;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.fragmentInterfaces.ChangeFragmentInterface;
import aroundwise.nepi.gcm.GCMRegistationIntentService;
import aroundwise.nepi.network.api.RegisterClient;
import aroundwise.nepi.network.responses.MallResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import aroundwise.nepi.util.views.CirclePageIndicator;
import aroundwise.nepi.util.views.CustomViewPager;
import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity implements ChangeFragmentInterface {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.viewpager)
    CustomViewPager viewpager;

    @BindView(R.id.viewpager_indicator)
    CirclePageIndicator view_pager_indicator;

    @BindView(R.id.logo)
    public ImageView ivLogo;

    ViewPagerAdapterOnlyText adapter;
    List<Integer> images;
    List<String> texts;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent() != null && getIntent().hasExtra("bundle"))
            data = getIntent().getBundleExtra("bundle");
        initialize();
        setFirstFragment();
        if (checkPlayServices()) {
            registerGCMReciver();
            registerReceiver();
            Intent intent = new Intent(this, GCMRegistationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isReceiverRegistered = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        hideProgress();
    }

    private void setFirstFragment() {
        MainFragment fragment = MainFragment.newInstance(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initialize() {
        showProgress();
        initializeViewPager();
        addBackStackListener();
        //TODO bug with no internet cand intri in aplicatie
        getMalls();
    }

    private void initializeViewPager() {
        images = new ArrayList<>();
        images.add(R.drawable.background_image_login);
        images.add(R.drawable.splash_screen);
        images.add(R.drawable.splash_screen_2);
        MyApplication.instance.getPicasso().load(getImage(0)).fetch();
        texts = new ArrayList<>();
        texts.add(getString(R.string.main_pager_text_1));
        texts.add(getString(R.string.main_pager_text_2));
        texts.add(getString(R.string.main_pager_text_3));

        adapter = new ViewPagerAdapterOnlyText(this, texts);
        viewpager.setAdapter(adapter);
        view_pager_indicator.setViewPager(viewpager);
    }

    private void switchFragment(BaseFragment fragment) {
        if (fragment != null) {
            fragment.setChangeFragmentInterface(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        }
    }

    private void switchFragment(BaseMvpFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        }
    }

    private void addBackStackListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    int backsTackCount = manager.getBackStackEntryCount();
                    Log.d(MainActivity.class.getSimpleName(), backsTackCount + "");
                    if (backsTackCount == 1) {
                        view_pager_indicator.setVisibility(View.VISIBLE);
                        viewpager.setPagingEnabled(true);
                    } else {
                        view_pager_indicator.setVisibility(View.INVISIBLE);
                        viewpager.setPagingEnabled(false);
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager manager = getSupportFragmentManager();

        if (manager != null) {
            int backsTackCount = manager.getBackStackEntryCount();
            Log.d(MainActivity.class.getSimpleName(), backsTackCount + "");
            if (backsTackCount == 0) {
                this.finish();
            }
        }
    }

    @Override
    public void changeFragment(BaseFragment fragment) {
        switchFragment(fragment);
    }

    @Override
    public void changeFragment(BaseMvpFragment fragment) {
        switchFragment(fragment);
    }

    @Override
    @Nullable
    public Bundle getData() {
        return data;
    }

    public Integer getImage(int position) {
        return images.get(0);
    }

    public CustomViewPager getViewpager() {
        return viewpager;
    }

    private void getMalls() {
        RegisterClient client = ServiceGenerator.getServiceGenerator().createService(RegisterClient.class);
        client.getMalls(10, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallResponse>() {
                    @Override
                    public void onCompleted() {
                        loadSession();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                    }

                    @Override
                    public void onNext(MallResponse mallResponse) {
                        Session.setMalls(mallResponse.getMalls());
                    }
                });
    }

    private void loadSession() {
        if (Session.readUserFromSharedPref(this) != null
                && !Session.getUser().getFirst_name().equals(Constants.NEW_USER)) {
            hideProgress();
            Intent intent = new Intent(this, DiscoverActivity.class);
            if (data != null)
                intent.putExtra("bundle", data);
            Util.reinitializeShowTutorialOfCamera(this);
            startActivity(intent);
            this.finish();
        } else
            hideProgress();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(DiscoverActivity.class.getSimpleName(), "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerGCMReciver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GCMRegistationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");
                } else if (intent.getAction().equals(GCMRegistationIntentService.REGISTRATION_ERROR)) {
                    Log.e(DiscoverActivity.class.getSimpleName(), "GCM registration error!");
                } else {
                    Log.e(DiscoverActivity.class.getSimpleName(), "Error occurred");
                }
            }
        };

    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistationIntentService.REGISTRATION_SUCCESS));
            isReceiverRegistered = true;
        }
    }

}
