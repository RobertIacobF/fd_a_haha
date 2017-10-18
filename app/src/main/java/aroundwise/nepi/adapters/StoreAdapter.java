package aroundwise.nepi.adapters;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.eventbus.StoreDetailsEvent;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.LocationUtil;
import aroundwise.nepi.util.Util;
import aroundwise.nepi.util.views.SAutoBgButton;
import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 31/08/16.
 */
public class StoreAdapter extends SupportAnnotatedAdapter implements StoreAdapterBinder {


    @ViewType(
            layout = R.layout.item_store,
            views =
                    {
                            @ViewField(id = R.id.iv_store_logo, type = ImageView.class, name = "iv_store_logo"),
                            @ViewField(id = R.id.iv_store_background, type = ImageView.class, name = "iv_store_background"),
                            @ViewField(id = R.id.iv_store_mall_logo, type = ImageView.class, name = "iv_mall_logo"),
                            @ViewField(id = R.id.tv_stores_points_left, type = TextView.class, name = "tv_points_left"),
                            @ViewField(id = R.id.tv_store_name, type = TextView.class, name = "tv_store_name"),
                            @ViewField(id = R.id.left_puncte_label, type = TextView.class, name = "tvLeftPuncteLabel"),
                            @ViewField(id = R.id.right_puncte_label, type = TextView.class, name = "tvRightPuncteLabel"),
                            @ViewField(id = R.id.tv_store_km, type = TextView.class, name = "tv_store_km"),
                            @ViewField(id = R.id.tv_stores_points_center, type = TextView.class, name = "tv_points_center"),
                            @ViewField(id = R.id.btn_store, type = SAutoBgButton.class, name = "btn_store")
                    }
    )
    public static final int store_vh = 0;

    @ViewType(
            layout = R.layout.item_loading_row,
            views =
                    {
                            @ViewField(id = R.id.progressbar, type = ProgressBar.class, name = "progressbar"),
                    }
    )
    public static final int loadingRow = 1;

    private Context context;
    private List<Shop> stores;
    private OnLoadMoreListener onLoadMoreListener;
    private int selectedStore = -1;

    public int getSelectedStore() {
        return selectedStore;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public StoreAdapter(Context context, List<Shop> stores) {
        super(context);
        this.context = context;
        this.stores = stores;
    }

    @Override
    public int getItemViewType(int position) {
        if (stores.get(position) == null)
            return loadingRow;
        return store_vh;
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    @Override
    public void bindViewHolder(StoreAdapterHolders.Store_vhViewHolder vh, final int position) {
        final Shop store = stores.get(position);

        if (store.getImages() != null && store.getImages().size() > 0)
            MyApplication.instance.getPicasso().load(store.getImages().get(0).getImage()).fit().centerCrop().placeholder(R.drawable.placeholder).into(vh.iv_store_background);
        else
            MyApplication.instance.getPicasso().load(R.drawable.placeholder).into(vh.iv_store_background);
        MyApplication.instance.getPicasso().load(store.getLogo()).into(vh.iv_store_logo);
        MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getMall().getLogo()).into(vh.iv_mall_logo);

        vh.tv_points_center.setText(store.getPointsPerBuy() + "%");
        if (store.getPointsPerBuy() == 1)
            vh.tvRightPuncteLabel.setText("PUNCT");
        vh.tv_points_left.setText(store.getWalkin_points() + "");
        if (store.getWalkin_points() == 1)
            vh.tvLeftPuncteLabel.setText("PUNCT");
        vh.tv_store_name.setText(store.getName());
        String km = "";
        if (store.getLcoations().size() > 0) {
            Location location = new Location("");
            location.setLatitude(store.getLcoations().get(0).getLatitude());
            location.setLongitude(store.getLcoations().get(0).getLongitude());
            Location lastLoc = LocationUtil.getLocationUtil(context).getLastLocation();
            if (lastLoc != null) {
                double distance = lastLoc.distanceTo(location);
                distance = distance / 1000;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(1);
                km = df.format(distance) + " " + context.getString(R.string.km_away);
            }
        }
        vh.tv_store_km.setText(km);
        vh.btn_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new StoreDetailsEvent(store));
                selectedStore = position;
            }
        });


    }

    @Override
    public void bindViewHolder(StoreAdapterHolders.LoadingRowViewHolder vh, int position) {

    }
}
