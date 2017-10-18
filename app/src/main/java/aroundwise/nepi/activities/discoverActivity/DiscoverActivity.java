package aroundwise.nepi.activities.discoverActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aroundwise.aroundwisesdk.AroundWiseSDK;
import com.aroundwise.aroundwisesdk.model.http.beacon.BeaconEvent;
import com.aroundwise.aroundwisesdk.model.http.beacon.BeaconScanerEvent;
import com.aroundwise.aroundwisesdk.model.http.beacon.GetBeaconResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.cameraActivity.CameraActivity;
import aroundwise.nepi.activities.cameraActivity.CameraActivity2;
import aroundwise.nepi.activities.discoverActivity.fragments.getRewardsFragment.GetRewardsFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.getPointsFragment.GetPointsFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.offerDetailsFragment.OfferDetailFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.offerCollectionFragment.OfferCollectionFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.StoresFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeDetailFragment.StoreDetailFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeMapFragment.StoreMapFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storesGetPointsFragments.StoreGetPointsFragment;
import aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storesGetPointsFragments.StorePurchasePointsFragment;
import aroundwise.nepi.activities.mainActivity.MainActivity;
import aroundwise.nepi.base.BaseMvpActivity;
import aroundwise.nepi.beacon.Callbacks;
import aroundwise.nepi.dialogs.WalkInDialog;
import aroundwise.nepi.eventbus.AnswerPollEvent;
import aroundwise.nepi.eventbus.BlurBackgroundEvent;
import aroundwise.nepi.eventbus.CameraEvent;
import aroundwise.nepi.eventbus.CollectionOfferEvent;
import aroundwise.nepi.eventbus.GetPointsEvent;
import aroundwise.nepi.eventbus.LogOutEvent;
import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.eventbus.Notificationevent;
import aroundwise.nepi.eventbus.OfferDetailEvent;
import aroundwise.nepi.eventbus.OnProfileChangeEvent;
import aroundwise.nepi.eventbus.PurchasePointsEvent;
import aroundwise.nepi.eventbus.ReviewEvent;
import aroundwise.nepi.eventbus.RewardDetailEvent;
import aroundwise.nepi.eventbus.StoreDetailsEvent;
import aroundwise.nepi.eventbus.StoreMapEvent;
import aroundwise.nepi.eventbus.StoresEvent;
import aroundwise.nepi.eventbus.WalkinPointsEvent;
import aroundwise.nepi.eventbus.WalletChangeEvent;
import aroundwise.nepi.gcm.GCMRegistationIntentService;
import aroundwise.nepi.network.api.BeaconClien;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.responses.BeaconScannerResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.receivers.BluetoothCallback;
import aroundwise.nepi.receivers.BluetoothReceiver;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.LocationUtil;
import aroundwise.nepi.util.Util;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import rx.Observer;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DiscoverActivity extends BaseMvpActivity<DiscoverActivityView, DiscoverActivityPresenter>
        implements DiscoverActivityView, BluetoothCallback {

    public static final int START_CAMERA_ACTIVITY = 1000;
    public static final int CAMERA_REQUEST = 100;

    //region BindView

    @BindView(R.id.container)
    ViewGroup container;

    @BindView(R.id.main_tab1)
    Button main_tab1;

    @BindView(R.id.main_tab2)
    Button main_tab2;

    @BindView(R.id.main_tab3)
    Button main_tab3;

    @BindView(R.id.main_tab4)
    Button main_tab4;

    @BindView(R.id.main_tab5)
    Button main_tab5;

    @BindView(R.id.main_tab1_line)
    View tab1_line;

    @BindView(R.id.main_tab2_line)
    View tab2_line;

    @BindView(R.id.main_tab3_line)
    View tab3_line;

    @BindView(R.id.main_tab4_line)
    View tab4_line;

    @BindView(R.id.main_tab5_line)
    View tab5_line;

    @BindView(R.id.layout_bubble)
    RelativeLayout layout_bubble;

    @BindView(R.id.tv_bubble)
    TextView tv_bubble;

    @BindView(R.id.bubble)
    ImageView ivBubble;

    @BindView(R.id.tv_loyalty)
    TextView tv_loyalty;

    @BindView(R.id.tv_loyalty_points)
    TextView tv_loyalty_points;

    @BindView(R.id.tv_profile)
    TextView tv_profile;

    @BindView(R.id.iv_profile)
    CircleImageView iv_profile;

    @BindView(R.id.background)
    RelativeLayout rlBackground;

    //endregion

    //region Variables
    private int currentTab = 0;
    private Context context;
    private AroundWiseSDK aroundWiseSDK;
    private BluetoothReceiver bluetoothReceiver;
    private CompositeSubscription compositeSubscription;
    private boolean isBlurred = false;
    private boolean goingForward = true;

    List<String> beaconsScanned;
    //endregion

    //region Activity methods

    private void displayPoints(Intent intent) {
        Bundle data = intent.getBundleExtra("bundle");
        String mallLogo = data.getString("mall_logo");
        String mallName = data.getString("mall_name");
        int points = Integer.valueOf(data.getString("points"));
        String storeLogo = data.getString("store_logo");
        String storeName = data.getString("store_name");

        WalkInDialog walkInDialog = WalkInDialog.newInstance(
                storeLogo,
                points,
                mallLogo);
        walkInDialog.show(getSupportFragmentManager(), null);
        if (presenter != null)
            presenter.getUserWallet();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.createCompositeSubscription();
        setContentView(R.layout.activity_discover);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initialize();
        if (getIntent().hasExtra("bundle")) {
            displayPoints(getIntent());
        }
        if (!checkLocationPermission())
            requestLocationPermission();
        checkUserLoggedIn();
        if (Session.getMalls() == null || Session.getMalls().size() == 0) {
            presenter.getMalls();
        } else {
            initializeTab();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.createCompositeSubscription();
        if (compositeSubscription == null)
            compositeSubscription = new CompositeSubscription();
        if (checkLocationPermission()) {
            LocationUtil.getLocationUtil(context).connect();
            initializeBeaconSdk();
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (AroundWiseSDK.getBeaconList() != null && AroundWiseSDK.getBeaconList().size() > 0)
            tv_bubble.setText(AroundWiseSDK.getBeaconList().size() + "");

        if (Session.getUser() != null && Session.getUser().getProfile() != null) {
            presenter.getUserWallet();
            setProfilePicture(Session.getUser().getProfile().getImage());
        } else
            presenter.getUserDetails();
        beaconsScanned = ComplexPreferences.getComplexPreferences(this, Constants.PREFFERENCES, MODE_PRIVATE).getObject(Constants.SCANNED_BEACONS + Session.getUser().getResourceUri(), List.class);
        if (beaconsScanned == null)
            beaconsScanned = new ArrayList<>();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationUtil.getLocationUtil(context).disconnect();
        EventBus.getDefault().unregister(this);
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, Constants.PREFFERENCES, MODE_PRIVATE);
        complexPreferences.putObject(Constants.SCANNED_BEACONS + Session.getUser().getResourceUri(), beaconsScanned);
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.removeSubscriptions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (aroundWiseSDK != null)
            aroundWiseSDK.unbindThisFromEventBus();
        unregisterReceiver(bluetoothReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationUtil.getLocationUtil(context).connect();
                    initializeBeaconSdk();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Permisii necesare");
                    builder.setMessage("Pentru a scana zona de magazine avem nevoie de acordul d-voastră de a utiliza locația curentă. Vă mulțumim!");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
                break;
            case CAMERA_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[grantResults.length - 1] == PackageManager.PERMISSION_GRANTED) {
                    onEvent(new CameraEvent());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Permisii necesare");
                    builder.setMessage("Pentru a scana bonul avem nevoie de acordul d-voastră de a utiliza camera foto. Vă mulțumim!");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    //endregion

    @NonNull
    @Override
    public DiscoverActivityPresenter createPresenter() {
        return new DiscoverActivityPresenter();
    }

    private void initializeBeaconSdk() {
        Callbacks c = new Callbacks();

        HashMap<String, String> clientData = new HashMap<>();
        clientData.put("key_placeholder", "value_placeholder");

        aroundWiseSDK = new AroundWiseSDK(this, this.getApplication(), c, false, "https://promenada.aroundwise.com", clientData);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Permission needed");
                builder.setMessage("We need to access your location in order to provide beacon related info");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(DiscoverActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.PERMISSION_LOCATION);
            }

        } else {
            aroundWiseSDK.bindThisToEventBus();
        }
    }

    private void initialize() {
        context = this;
        tv_bubble.setVisibility(View.GONE);
        ivBubble.setVisibility(View.GONE);
        tv_bubble.setText(0 + "");
        presenter.getFavoriteOffers();
        //presenter.getUserDetails();
        bluetoothReceiver = new BluetoothReceiver();
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothReceiver, intentFilter);
        if (Session.getUser().getProfile() != null
                && Session.getUser().getProfile().getImage() != null)
            setProfilePicture(Session.getUser().getProfile().getImage());

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, Constants.PREFFERENCES, MODE_PRIVATE);
        Boolean tokenSent = complexPreferences.getObject(Constants.TOKEN_SENT, Boolean.class);
        if (tokenSent == null || !tokenSent) {
            String token = ComplexPreferences.getComplexPreferences(this, Constants.PREFFERENCES, Context.MODE_PRIVATE).getObject("token", String.class);
            presenter.sendToken(token);
        }

        //presenter.claimPoints();

    }

    //region Select Deselect

    private void deselectTab(Button tab) {
        /* TODO: daca tot ce vrei sa faci e sa schimbi culoarea in functie de stare selectat,
         neselectat,
         poti face un selector in drawable pentru asta, similar cum ai face pentru backgroundul
         unui buton */
        int deselectedColor = ContextCompat.getColor(this, R.color.tab_non_selected_color);
        tab.setTextColor(deselectedColor);
        tab.setSelected(false);
        if (tab.getId() == R.id.main_tab4) {
            tv_loyalty.setTextColor(Color.parseColor("#66F2084B"));
            tv_loyalty_points.setTextColor(Color.parseColor("#66F2084B"));
        }
        if (tab.getId() == R.id.main_tab5) {
            tv_profile.setTextColor(deselectedColor);
            iv_profile.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
        }
    }

    private void deselectLines() {
        tab1_line.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        tab2_line.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        tab3_line.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        tab4_line.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        tab5_line.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

    }

    public void deselectTabs() {
        deselectTab(main_tab1);
        deselectTab(main_tab2);
        deselectTab(main_tab3);
        deselectTab(main_tab4);
        deselectTab(main_tab5);
    }

    public void selectTab(int index) {
        if (currentTab > index) {
            goingForward = true;
        } else {
            goingForward = false;
        }
        currentTab = index;
        deselectLines();
        deselectTabs();
        switch (index) {
            case 0:
                selectTab(main_tab1);
                tab1_line.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_selected_color));
                presenter.changeToOfferFragment();
                break;
            case 1:
                selectTab(main_tab2);
                tab2_line.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_selected_color));
                //TODO change back
                presenter.changeToNearYouFragment();
                //WalkInDialog dialog = new WalkInDialog();
                //dialog.show(getSupportFragmentManager(), "walkin");

                break;
            case 2:
                selectTab(main_tab3);
                tab3_line.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_selected_color));
                presenter.changeToStoreFragment();
                break;
            case 3:
                selectTab(main_tab4);
                tab4_line.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_selected_color));
                presenter.changeToLoylatyFragment();
                break;
            case 4:
                selectTab(main_tab5);
                tab5_line.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_selected_color));
                presenter.changeToProfileFragment();
                break;
            default:
                break;
        }
    }

    private void selectTab(Button tab) {
         /* TODO: In mod similar ca la deselect, poti sa faci asta din xml */
        int selectedColor = getResources().getColor(R.color.tab_selected_color);
        tab.setTextColor(selectedColor);
        tab.setSelected(true);
        if (tab.getId() == R.id.main_tab4) {
            tv_loyalty.setTextColor(Color.parseColor("#F2084B"));
            tv_loyalty_points.setTextColor(Color.parseColor("#F2084B"));
        }
        if (tab.getId() == R.id.main_tab5) {
            tv_profile.setTextColor(selectedColor);
            iv_profile.setColorFilter(null);
        }

    }

    private void initializeTab() {
        if (getIntent().hasExtra("contextual")) {
            GetBeaconResult beaconResult = getIntent().getParcelableExtra("contextual");
            Fragment fragment = OfferFragment.newInstance(new Offer(beaconResult.getContextual()));
            changeTab(fragment);
        } else {
            if (getIntent().hasExtra("gotoactivity") && getIntent().getBooleanExtra("gotoactivity", false)) {
                currentTab = 4;
                deselectLines();
                deselectTabs();
                selectTab(main_tab5);
                tab5_line.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_selected_color));
                presenter.changeToProfileFragment(1);
            } else
                selectTab(0);
        }
    }
    //endregion

    //region Fragment management methods

    @Override
    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right);
        //fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.container, fragment, null)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void changeTab(final Fragment fragment) {
        DiscoverActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm = getSupportFragmentManager();
                int count = fm.getBackStackEntryCount();
                for (int i = 0; i < count; ++i) {
                    fm.popBackStack();
                }

                FragmentTransaction ft = fm.beginTransaction();
                //.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right)
                //.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                if (goingForward) {
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else {
                    ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                }
                ft.replace(R.id.container, fragment, null);
                ft.commit();
            }
        });
    }

    @Override
    public void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            finish();
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    //endregion


    @Override
    public void hideProgressBar() {
        hideProgress();
    }

    @Override
    public void showProgressBar() {
        showProgress();
    }

    @Override
    public void resumeInitialization() {
        initializeTab();
    }

    @Override
    public void setProfilePicture(String uri) {
        MyApplication.instance.getPicasso().load(uri)
                .fit().centerCrop().into(iv_profile);
    }

    @Override
    public void setPoints() {
        tv_loyalty_points.setText(String.valueOf(Session.getUser().getProfile().getPoints()));
    }

    @Override
    public void errorOnUpdateToken() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiscoverActivity.this, Constants.PREFFERENCES, MODE_PRIVATE);
        complexPreferences.putObject(Constants.TOKEN_SENT, false);
        complexPreferences.save();
    }

    @Override
    public void successOnUpdateToken() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiscoverActivity.this, Constants.PREFFERENCES, MODE_PRIVATE);
        complexPreferences.putObject(Constants.TOKEN_SENT, true);
        complexPreferences.save();
    }

    @Override
    public void onBackPressed() {
        popFragment();
    }

    //region OnClick

    @OnClick(R.id.main_tab1)
    public void click0() {
        if (currentTab != 0)
            selectTab(0);
    }

    @OnClick(R.id.main_tab2)
    public void click1() {
        if (currentTab != 1)
            selectTab(1);
    }

    @OnClick(R.id.main_tab3)
    public void click2() {
        if (currentTab != 2)
            selectTab(2);
    }

    @OnClick(R.id.main_tab4)
    public void click3() {
        if (currentTab != 3)
            selectTab(3);
    }

    @OnClick(R.id.main_tab5)
    public void click4() {
        if (currentTab != 4)
            selectTab(4);
    }
    //endregion

    //region EventBus

    public void onEvent(BlurBackgroundEvent event) {
        if (event.isBlur() && !isBlurred) {
            Blurry.with(context).sampling(2).radius(25).onto(rlBackground);
            isBlurred = true;
        } else if (!event.isBlur() && isBlurred) {
            blurOut();
            isBlurred = false;
        }
    }

    private void blurOut() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Blurry.delete(rlBackground);
            }
        });
    }

    public void onEvent(CameraEvent event) {
        if (askForCamerPermssion()) {
            Intent intent = new Intent(DiscoverActivity.this, CameraActivity2.class);
            intent.putExtra(Constants.SHOW_TUTORIAL, true);
            startActivity(intent);
        }
    }

    public void onEvent(ReviewEvent event) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFFERENCES, Context.MODE_PRIVATE);
        int ratingCounter = sharedPreferences.getInt(Constants.RATING_COUNTER, 0);
        if (ratingCounter == 0) {
            AlertDialog.Builder reviewBuilder = new AlertDialog.Builder(context);
            final int finalRatingCounter = ratingCounter;
            reviewBuilder.setTitle(context.getString(R.string.review_title))
                    .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(Constants.RATING_COUNTER, -1);
                            editor.apply();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=aroundwise.nepi"));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(Constants.RATING_COUNTER, -1);
                            editor.apply();
                        }
                    })
                    .setNeutralButton("Nu incă", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int ratingModified = finalRatingCounter + 1;
                            if (ratingModified == 4)
                                ratingModified = 0;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(Constants.RATING_COUNTER, ratingModified);
                            editor.apply();
                        }
                    })
                    .create()
                    .show();
        } else if (ratingCounter != -1) {
            ratingCounter++;
            if (ratingCounter == 4)
                ratingCounter = 0;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Constants.RATING_COUNTER, ratingCounter);
            editor.apply();
        }
    }

    public void onEvent(AnswerPollEvent event) {
        WalkInDialog walkInDialog = WalkInDialog.newInstance(event.getPoints(), event.getLogo());
        walkInDialog.show(getSupportFragmentManager(), null);
    }


    public void onEvent(Notificationevent event) {
        WalkInDialog walkInDialog = WalkInDialog.newInstance(event.getStoreLogo(), event.getPoints(), event.getMallLogo());
        walkInDialog.show(getSupportFragmentManager(), null);
    }

    public void onEvent(OnProfileChangeEvent event) {
        presenter.getUserDetails();
    }

    public void onEvent(BeaconScanerEvent event) {
        Handler handlerUi = new Handler(Looper.getMainLooper());
        handlerUi.post(new Runnable() {
            @Override
            public void run() {
                if (AroundWiseSDK.getBeaconList().size() > 0) {
                    tv_bubble.setVisibility(View.VISIBLE);
                    ivBubble.setVisibility(View.VISIBLE);
                    tv_bubble.setText(AroundWiseSDK.getBeaconList().size() + "");
                } else {
                    tv_bubble.setVisibility(View.GONE);
                    ivBubble.setVisibility(View.GONE);
                }
            }
        });

        BeaconClien beaconClien = ServiceGenerator.getServiceGenerator().createService(BeaconClien.class);
        List<BeaconEvent> beaconEventList = AroundWiseSDK.getFullBeaconList();
        if (beaconEventList != null && beaconEventList.size() > 0)
            for (final BeaconEvent beaconEvent : beaconEventList) {
                compositeSubscription.add(beaconClien.sendBeaconInfo(Session.getUser().getId(),
                        Session.getUser().getSessionId(),
                        beaconEvent.getProximityUID(),
                        beaconEvent.getMajor(),
                        beaconEvent.getMinor())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<BeaconScannerResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(DiscoverActivity.class.getSimpleName(), "error");
                            }

                            @Override
                            public void onNext(BeaconScannerResponse basicResponse) {

                                if (basicResponse.getStore() != null
                                        && basicResponse.getStore().size() > 0
                                        && basicResponse.getStore().get(0).getPointsReceived() != 0
                                        && basicResponse.getStore().get(0) != null
                                        && basicResponse.getStore().get(0).getStores() != null
                                        ) {

                                    WalkInDialog walkInDialog = WalkInDialog.newInstance(
                                            basicResponse.getStore().get(0).getStores().getLogo(),
                                            basicResponse.getStore().get(0).getPointsReceived(),
                                            basicResponse.getStore().get(0).getStores().mall.getLogo());
                                    walkInDialog.show(getSupportFragmentManager(), null);
                                    presenter.getUserWallet();
                                }
                            }
                        }));
            }
    }

    public void onEvent(StoreMapEvent event) {
        if (event != null)
            changeFragment(StoreMapFragment.newInstance(event.getStore(), (ArrayList) event.getShopsList()));
    }

    public void onEvent(LogOutEvent event) {
        Toast.makeText(this, "Sesiunea d-voastra a expirat! Vă rugăm să vă logați din nou în aplicație!", Toast.LENGTH_LONG).show();
        Session.logOut(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void onEvent(OfferDetailEvent event) {
        if (event.getOffer() != null)
            changeFragment(OfferDetailFragment.newInstance(event.getOffer()));
    }

    public void onEvent(StoreDetailsEvent event) {
        Util.hideSoftKeyboard(this);
        if (event.getStore() != null)
            changeFragment(StoreDetailFragment.newInstance(event.getStore()));
    }

    public void onEvent(RewardDetailEvent event) {
        if (event.getReward() != null)
            changeFragment(GetRewardsFragment.newInstance(event.getReward()));
    }

    public void onEvent(GetPointsEvent event) {
        changeFragment(GetPointsFragment.newInstance());
    }

    public void onEvent(StoresEvent event) {
        changeFragment(StoresFragment.newInstance(event.isWalkIn()));
    }

    public void onEvent(CollectionOfferEvent event) {
        changeFragment(OfferCollectionFragment.newInstance(event.getId(), event.getTitle(), event.isSimple()));
    }

    public void onEvent(MallHasChangedEvent event) {
        if (presenter != null)
            presenter.getUserWallet();
    }

    public void onEvent(WalletChangeEvent event) {
        tv_loyalty_points.setText(String.valueOf(Session.getUser().getProfile().getPoints()));
    }

    public void onEvent(WalkinPointsEvent event) {
        changeFragment(StoreGetPointsFragment.newInstance(event.getStore()));
    }

    public void onEvent(PurchasePointsEvent event) {
        changeFragment(StorePurchasePointsFragment.newInstance(event.getStore()));
    }
    //endregion

    //region BluetoothCallback

    @Override
    public void bluetoothIsOn() {

    }

    @Override
    public void bluetoothIsOff() {

    }

    @Override
    public void turningOn() {

    }

    @Override
    public void tunringOff() {

    }

    //endregion


    public void clearBeaconList(View view) {
        aroundWiseSDK.clearBeaconList();
    }

    //TODO change perm req of write
    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, Constants.PERMISSION_LOCATION);
    }

    private void checkUserLoggedIn() {
        if (Session.readUserFromSharedPref(this) != null
                && Session.getUser().getId() == -1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        }

    }

    private boolean askForCamerPermssion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, CAMERA_REQUEST);
            return false;
        }
        return true;
    }
}
