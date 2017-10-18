package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeMapFragment.MapUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Set;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.network.model.Location;
import aroundwise.nepi.network.model.Shop;

/**
 * Created by mihai on 22/09/16.
 */
public class ShopRenderer extends DefaultClusterRenderer<Shop> {


    public final TextView tvMarkerTitle;
    public final View unselectedView;
    public final View selectedView;
    public final TextView tvAddress;
    public final ImageView ivLogo;
    private Shop currentlyStore = null;

    public void setCurrentlyStore(Shop currentlyStore) {
        this.currentlyStore = currentlyStore;
    }

    private Context context;


    public ShopRenderer(Activity activity, GoogleMap map, ClusterManager<Shop> clusterManager) {
        super(activity, map, clusterManager);

        context = activity;
        unselectedView = activity.getLayoutInflater().inflate(R.layout.view_maps_marker_closed, null);
        selectedView = activity.getLayoutInflater().inflate(R.layout.view_maps_marker, null);

        tvMarkerTitle = (TextView) unselectedView.findViewById(R.id.marker_text);
        tvAddress = (TextView) selectedView.findViewById(R.id.tv_address);
        ivLogo = (ImageView) selectedView.findViewById(R.id.iv_store);

    }

    public Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    public void setMarkerClosed(Shop shop) {
        if (shop == null) {
            return;
        }
        tvMarkerTitle.setText(shop.getName());
        if (getMarker(shop) != null)
            getMarker(shop).setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(unselectedView)));
    }

    public void setMarkerOpen(final Shop shop, final boolean secondTimeSettingPic) {
        if (shop == null) {
            return;
        }

        Location location = null;

        //TODO changes ehre
        for (Location locationIt : shop.getLcoations()) {
            if (locationIt.getLatitude() == shop.getLcoations().get(0).getLatitude()
                    && locationIt.getLongitude() == shop.getLcoations().get(0).getLongitude()) {
                location = locationIt;
                break;
            }
        }

        tvAddress.setText(location == null ? "" : location.getAddress());
        MyApplication.instance.getPicasso().load(shop.getLogo()).into(ivLogo, new Callback() {
            @Override
            public void onSuccess() {
                if (!secondTimeSettingPic)
                    setMarkerOpen(shop, true);
            }

            @Override
            public void onError() {

            }
        });
        if (getMarker(shop) != null)
            getMarker(shop).setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(selectedView)));
    }

    @Override
    protected void onClusterRendered(Cluster<Shop> cluster, Marker marker) {
        super.onClusterRendered(cluster, marker);
    }

    @Override
    protected void onBeforeClusterItemRendered(Shop store, MarkerOptions markerOptions) {
        // Draw a single person.
        if (currentlyStore != null && currentlyStore.getId() == store.getId()) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(selectedView)));
            setMarkerOpen(store, false);
        } else {
            tvMarkerTitle.setText(store.getName());
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(unselectedView)));
        }
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Shop> cluster, MarkerOptions markerOptions) {


        tvMarkerTitle.setText(cluster.getSize() + " stores");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(unselectedView)));
    }

    @Override
    public void onClustersChanged(Set<? extends Cluster<Shop>> clusters) {
        super.onClustersChanged(clusters);
        /*if (currentlyStore != null) {
            setMarkerOpen(currentlyStore, false);
        }*/
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}
