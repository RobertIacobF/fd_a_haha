package aroundwise.nepi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aroundwise.aroundwisesdk.model.http.beacon.ContextualBeacon;
import com.squareup.picasso.Picasso;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.eventbus.OfferDetailEvent;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.util.views.SAutoBgButton;
import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 01/09/16.
 */
public class SwipeAdapter extends BaseAdapter {


    List<ContextualBeacon> swipes;
    Context context;

    public SwipeAdapter(Context context) {
        this.context = context;
    }

    public void setSwipes(List<ContextualBeacon> swipes) {
        this.swipes = swipes;
    }

    @Override
    public int getCount() {
        return swipes != null ? swipes.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return swipes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ContextualBeacon swipe = swipes.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_near_you, null);
        }
        ImageView iv = (ImageView) view.findViewById(R.id.iv_item);
        ImageView iv_logo = (ImageView) view.findViewById(R.id.iv_store_logo);
        TextView tv = (TextView) view.findViewById(R.id.tv_store_name);
        SAutoBgButton btnDetails = (SAutoBgButton) view.findViewById(R.id.btn_next);

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new OfferDetailEvent(new Offer(swipe.getBeacon().getContextual())));
            }
        });

        if ((swipe.getBeacon().getContextual().getImages() != null && swipe.getBeacon().getContextual().getImages().size() > 0)) {
            MyApplication.instance.getPicasso()
                    .load(swipe.getBeacon().getContextual().getImages().get(0).getImage())
                    .into(iv);
        }
        if (swipe.getBeacon().getStore() != null) {
            MyApplication.instance.getPicasso()
                    .load(swipe.getBeacon().getStore().getLogo())
                    .into(iv_logo);
            tv.setText(swipe.getBeacon().getStore().getName());
        }

        return view;
    }
}
