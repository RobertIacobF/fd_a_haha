package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeMapFragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;


import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeMapFragment.MapUtil.ShopRenderer;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.LocationUtil;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;


public class StoreMapFragment extends BaseMvpFragment<StoreMapView, StoreMapPresenter>
        implements OnMapReadyCallback, StoreMapView, ClusterManager.OnClusterItemClickListener<Shop> {

    @BindView(R.id.actionbar)
    CustomActionBar actionBar;

    @BindView(R.id.iv_store_background)
    ImageView ivStoreBackground;

    @BindView(R.id.iv_store_logo)
    ImageView ivStoreLogo;

    @BindView(R.id.tv_store_name)
    TextView tvStoreName;

    @BindView(R.id.tv_store_km)
    TextView tvStoreKm;

    @BindView(R.id.tv_stores_points_left)
    TextView tvPointsWalkIn;

    @BindView(R.id.tv_stores_points_center)
    TextView tvPointsBuy;

    @BindView(R.id.iv_store_mall_logo)
    ImageView ivStoreMallLogo;

    @BindView(R.id.item_store)
    RelativeLayout rlStoreItem;

    private GoogleMap googleMap;
    private Shop currentlySelectedShop;
    private ShopRenderer shopRenderer;
    private ClusterManager<Shop> clusterManager;
    private List<Shop> shopList;


    public static StoreMapFragment newInstance(@Nullable Shop store, @Nullable ArrayList<Shop> shopList) {
        StoreMapFragment fragment = new StoreMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SHOP, store);
        args.putParcelableArrayList(Constants.SHOP_LIST, shopList);
        fragment.setArguments(args);
        return fragment;
    }

    public StoreMapFragment() {
        // Required empty public constructor

    }

    @Override
    public StoreMapPresenter createPresenter() {
        return new StoreMapPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentlySelectedShop = getArguments().getParcelable(Constants.SHOP);
        shopList = getArguments().getParcelableArrayList(Constants.SHOP_LIST);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_store_map;
    }

    @Override
    protected void setUpViews() {
        presenter.setShops(shopList);
        rlStoreItem.setVisibility(View.GONE);
        if (checkPermissions(getActivity())) {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.maps_fragment);
            mapFragment.getMapAsync(this);
        }
        actionBar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).popFragment();
            }
        });
        setUpStore(currentlySelectedShop);
    }

    @Override
    public void displayShops(List<Shop> shops) {
        if (shops != null) {
            setUpCluster(shops);
            shopRenderer.setMarkerOpen(currentlySelectedShop, false);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        if (this.googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            if (currentlySelectedShop != null && currentlySelectedShop.getLcoations().size() != 0) {
                animateUpStore();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentlySelectedShop.getLcoations().get(0).getLatitude(),
                        currentlySelectedShop.getLcoations().get(0).getLongitude()), 15f));
            } else {
                android.location.Location myLocation = LocationUtil.getLocationUtil(getActivity()).getLastLocation();
                if (myLocation != null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),
                            myLocation.getLongitude()), 15f));
            }

            if (currentlySelectedShop == null) {
                displayShops(presenter.getShops());
            } else {
                setUpClusterWithSingleItem();
                shopRenderer.setMarkerOpen(currentlySelectedShop, false);
            }

        }

    }

    private boolean checkPermissions(Context context) {
        String coarse = Manifest.permission.ACCESS_COARSE_LOCATION;
        String fine = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(context, coarse) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, coarse) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{coarse, fine}, Constants.PERMISSION_LOCATION);
            return false;
        }
        return true;
    }

    private void animateUpStore() {
        if (rlStoreItem.getVisibility() != View.VISIBLE) {
            Animation bootomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.translation_bottom_up);
            rlStoreItem.startAnimation(bootomUp);
            rlStoreItem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.maps_fragment);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void setKmDistance(String km) {
        tvStoreKm.setText(km);
    }

    private void setUpCluster(List<Shop> shops) {
        clusterManager = new ClusterManager<Shop>(getActivity(), googleMap);
        shopRenderer = new ShopRenderer(getActivity(), googleMap, clusterManager);
        clusterManager.setRenderer(shopRenderer);
        clusterManager.setOnClusterItemClickListener(this);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        List<Shop> tempShops = filterStores(shops);
        clusterManager.addItems(tempShops);
        if (currentlySelectedShop != null)
            for (Shop shop : shops) {
                if (shop.getId().equals(currentlySelectedShop.getId())) {
                    shopRenderer.setMarkerOpen(shop, false);
                    currentlySelectedShop = shop;
                    shopRenderer.setCurrentlyStore(currentlySelectedShop);
                    break;
                }
            }
        clusterManager.cluster();
    }

    private void setUpClusterWithSingleItem() {
        clusterManager = new ClusterManager<Shop>(getActivity(), googleMap);
        shopRenderer = new ShopRenderer(getActivity(), googleMap, clusterManager);
        clusterManager.setRenderer(shopRenderer);
        clusterManager.setOnClusterItemClickListener(this);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        clusterManager.addItem(currentlySelectedShop);
        clusterManager.cluster();
        shopRenderer.setCurrentlyStore(currentlySelectedShop);
    }


    private List<Shop> filterStores(List<Shop> shops) {
        List<Shop> temp = new ArrayList<>();
        for (Shop shop : shops) {
            if (shop.getLcoations() != null && shop.getLcoations().size() > 0)
                temp.add(shop);
        }
        return temp;
    }

    private void setUpStore(Shop store) {
        if (store != null) {
            if (store.getImages() != null && store.getImages().size() > 0)
                MyApplication.instance.getPicasso().load(store.getImages().get(0).getImage()).into(ivStoreBackground);
            MyApplication.instance.getPicasso().load(store.getLogo()).into(ivStoreLogo);
            MyApplication.instance.getPicasso().load(Session.getMalls().get(0).getLogo()).into(ivStoreMallLogo);
            tvStoreName.setText(store.getName());
            tvPointsBuy.setText(String.valueOf(store.getPointsPerBuy()));
            tvPointsWalkIn.setText(String.valueOf(store.getWalkin_points()));
            if (store.latitude != null && store.longitude != null) {
                presenter.kmToStore(store, getContext());
            }
        }
    }

    @Override
    public boolean onClusterItemClick(Shop shop) {
        if (currentlySelectedShop != null
                && currentlySelectedShop.getLcoations().size() > 0
                && currentlySelectedShop.getId().longValue() == shop.getId().longValue()) {
            if (presenter.checkGps(getActivity())) {
                android.location.Location lastLocation = LocationUtil.getLocationUtil(getActivity()).getLastLocation();
                if (lastLocation != null)
                    presenter.showDirections(getActivity(),
                            lastLocation.getLatitude(), lastLocation.getLongitude(),
                            currentlySelectedShop.getLcoations().get(0).getLatitude(),
                            currentlySelectedShop.getLcoations().get(0).getLongitude());
            }
        } else {
            shopRenderer.setMarkerClosed(currentlySelectedShop);
            currentlySelectedShop = shop;
            shopRenderer.setCurrentlyStore(currentlySelectedShop);
            setUpStore(shop);
            animateUpStore();
            shopRenderer.setMarkerOpen(shop, false);
        }


        return false;
    }

}
