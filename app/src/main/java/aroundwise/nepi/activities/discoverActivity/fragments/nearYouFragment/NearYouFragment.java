package aroundwise.nepi.activities.discoverActivity.fragments.nearYouFragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aroundwise.aroundwisesdk.AroundWiseSDK;
import com.aroundwise.aroundwisesdk.model.http.beacon.ContextualBeacon;
import com.daprlabs.cardstack.SwipeDeck;
import com.daprlabs.cardstack.SwipeFrameLayout;

import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.adapters.SwipeAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.receivers.BluetoothEvent;
import aroundwise.nepi.receivers.BluetoothState;
import aroundwise.nepi.util.ConfettiCreator;
import aroundwise.nepi.util.Constants;
import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 01/09/16.
 */
public class NearYouFragment extends BaseMvpFragment<NearYouView, NearYouPresenter> {

    @BindView(R.id.scanning)
    RelativeLayout rl_scanning;

    @BindView(R.id.fl_open_bluetooth)
    RelativeLayout flOpenBluetooth;

    @BindView(R.id.swiperFrame)
    SwipeFrameLayout swipeFrameLayout;

    @BindView(R.id.swipe_deck)
    SwipeDeck swipeDeck;

    @BindView(R.id.iv_scanner)
    ImageView iv_scanner;

    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;

    List<ContextualBeacon> swipes;
    SwipeAdapter adapter;

    Animation animationUtils;

    public static NearYouFragment newInstance() {
        NearYouFragment fragment = new NearYouFragment();
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_near_you;
    }

    @Override
    protected void setUpViews() {
        if (!checkLocation())
            requestLocationPermission();
        else {

        }
    }

    @Override
    public NearYouPresenter createPresenter() {
        return new NearYouPresenter();
    }

    private void initialize() {
        if (checkBluetooth()) {
            animation();
        } else {
            showBluetoothScreen();
        }
    }

    private void showBluetoothScreen() {
        flOpenBluetooth.setVisibility(View.VISIBLE);
        rl_scanning.setVisibility(View.GONE);
        swipeFrameLayout.setVisibility(View.GONE);
    }

    private void hideBluetoothScreen() {
        flOpenBluetooth.setVisibility(View.GONE);
        rl_scanning.setVisibility(View.VISIBLE);
    }

    private void animation() {
        animationUtils = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_indefinitely);
        iv_scanner.startAnimation(animationUtils);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AroundWiseSDK.getBeaconList().isEmpty())
                    handler.postDelayed(this, 1000);
                else {
                    rl_scanning.setVisibility(View.GONE);
                    swipeFrameLayout.setVisibility(View.VISIBLE);
                    initializeSwipe();
                }
            }
        }, 1000);
    }

    private void initializeSwipe() {
        swipes = new ArrayList<>();
        swipes = AroundWiseSDK.getBeaconList();
        for (ContextualBeacon beacon : swipes) {
            MyApplication.instance.getPicasso().load(beacon.getBeacon().getContextual().getStore().getLogo()).fetch();
        }
        adapter = new SwipeAdapter(getActivity());
        adapter.setSwipes(swipes);
        swipeDeck.setAdapter(adapter);
        swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {

            }

            @Override
            public void cardSwipedRight(int position) {

            }

            @Override
            public void cardsDepleted() {
                adapter = new SwipeAdapter(getActivity());
                adapter.setSwipes(swipes);
                swipeDeck.setAdapter(adapter);
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });

    }

    private boolean checkBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            return false;
        }
        return true;
    }

    public void showBluetoothSettings(View btn) {
        Intent intentBluetooth = new Intent();
        intentBluetooth.setAction("android.settings.BLUETOOTH_SETTINGS");
        this.startActivity(intentBluetooth);
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
        if (animationUtils != null) {
            animationUtils.cancel();
            animationUtils.reset();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_LOCATION && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            //shouldShowRequestPermissionRationale("Pentru a scana zona de megazine avem nevoie de acordul d-voastra de a utiliza locatia curenta. Va multumim!");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Permisii necesare");
            builder.setMessage("Pentru a scana zona de magazine avem nevoie de acordul d-voastră de a utiliza locația curentă. Vă mulțumim!");
            builder.setPositiveButton("OK", null);
            builder.show();
        }
    }

    public void onEvent(BluetoothEvent event) {
        if (event.getState() == BluetoothState.ON) {
            hideBluetoothScreen();
            createConffetii();
            animation();
        } else
            showBluetoothScreen();
    }

    private boolean checkLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestLocationPermission() {
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.PERMISSION_LOCATION);
    }


    private void createConffetii() {
        ConfettiCreator confettiCreator = new ConfettiCreator();
        confettiCreator.confetti(getContext(), rlRoot, 3000, 50);
    }
}
