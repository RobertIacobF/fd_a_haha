package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeMapFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.network.api.ShopClient;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.network.responses.ShopResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.LocationUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mihai on 16/09/16.
 */
public class StoreMapPresenter extends MvpBasePresenter<StoreMapView> {

    private List<Shop> shops;

    public StoreMapPresenter() {
        shops = new ArrayList<>();
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    public void kmToStore(Shop store, Context context) {
        Location location = new Location("");
        location.setLatitude(store.latitude);
        location.setLongitude(store.longitude);
        double distance = LocationUtil.getLocationUtil(context).getLastLocation().distanceTo(location);
        distance = distance / 1000;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        String km = df.format(distance) + " " + context.getString(R.string.km_away);
        if (isViewAttached())
            getView().setKmDistance(km);
    }


    public void receiveShops() {
        ShopClient shopClient = ServiceGenerator.getServiceGenerator().createService(ShopClient.class);
        shopClient.getShops(Session.getUser().getProfile().getMall().getId(),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached())
                            getView().displayShops(shops);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShopResponse shopResponse) {
                        if (shops != null) {
                            shops.addAll(shopResponse.getShops());
                        } else
                            shops = shopResponse.getShops();

                    }
                });
    }

    public void showDirections(Context context, Double startLat, Double startLng, Double destLat, Double destLng) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + startLat + "," + startLng + "&daddr=" + destLat + "," + destLng));
        context.startActivity(intent);
    }

    public boolean checkGps(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(context);
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
