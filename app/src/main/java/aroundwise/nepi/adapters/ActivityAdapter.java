package aroundwise.nepi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.network.model.UserActivity;

/**
 * Created by mihai on 23/09/16.
 */
public class ActivityAdapter extends SupportAnnotatedAdapter implements ActivityAdapterBinder {

    private final Context context;


    private static final String TYPE_WALK_IN = "walkin";
    private static final String TYPE_WALK_OUT = "walkout";
    private static final String TYPE_CLAIMED_REWARD = "claimedreward";

    @ViewType(
            layout = R.layout.item_activity,
            views =
                    {
                            @ViewField(id = R.id.iv_logo, type = ImageView.class, name = "ivLogo"),
                            @ViewField(id = R.id.tv_title, type = TextView.class, name = "tvTitle"),
                            @ViewField(id = R.id.tv_date, type = TextView.class, name = "tvDate"),
                            @ViewField(id = R.id.tv_reason, type = TextView.class, name = "tvReason")
                    }
    )


    public final int activty = 1;


    private List<UserActivity> activities;


    public ActivityAdapter(Context context, List<UserActivity> activities) {
        super(context);
        this.context = context;
        this.activities = activities;
    }

    @Override
    public int getItemViewType(int position) {
        return activty;

    }

    @Override
    public int getItemCount() {
        return activities == null ? 0 : activities.size();
    }

    @Override
    public void bindViewHolder(ActivityAdapterHolders.ActivtyViewHolder vh, int position) {
        vh.tvTitle.setText("" + activities.get(position).getActivityMessage());
        vh.tvReason.setText(activities.get(position).getReasonMessage());
        if (activities.get(position).getCreatedAt() != null && activities.get(position).getCreatedAt().contains("T")) {
            vh.tvDate.setText("" + activities.get(position).getCreatedAt().split("T")[0] + ", "
                    + activities.get(position).getCreatedAt().split("T")[1]);
        } else {
            vh.tvDate.setText("");
        }

        /*if (activities.get(position).getActivityType().equals(TYPE_WALK_IN)) {
            vh.tvDescription.setText("" + activities.get(position).getPointsClaimed());
            vh.tvFrom.setText("for walk in");
            vh.tvDescription.setTextColor(Color.parseColor("#f7cd2d"));
            vh.tvFrom.setTextColor(Color.parseColor("#4a4a4a"));
        }

        if (activities.get(position).getActivityType().equals(TYPE_WALK_OUT)) {

            vh.tvDescription.setText("" + activities.get(position).getPointsClaimed());
            vh.tvFrom.setText("for walk out");
            vh.tvDescription.setTextColor(Color.parseColor("#f7cd2d"));
            vh.tvFrom.setTextColor(Color.parseColor("#4a4a4a"));

        }

        if (activities.get(position).getActivityType().equals(TYPE_CLAIMED_REWARD)) {
            vh.tvDescription.setText("" + activities.get(position).getPointsClaimed());
            vh.tvDescription.setTextColor(Color.parseColor("#F8524A"));
            vh.tvFrom.setText("for claiming reward");
            vh.tvFrom.setTextColor(Color.parseColor("#4a4a4a"));
        }
*/

        if (activities.get(position).getShops().getLogo() != null) {
            MyApplication.instance.getPicasso().load(activities.get(position).getShops().getLogo().equals("") ? "null" : activities.get(position).getShops().getLogo())
                    .error(R.mipmap.ic_get_points_details_shop).placeholder(R.drawable.placeholder).into(vh.ivLogo);
        } else {
            vh.ivLogo.setImageResource(R.mipmap.ic_get_points_details_shop);
        }
    }
}
