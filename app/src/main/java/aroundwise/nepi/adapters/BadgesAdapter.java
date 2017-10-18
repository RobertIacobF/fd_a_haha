package aroundwise.nepi.adapters;

import android.content.Context;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.network.model.Badge;
import de.hdodenhof.circleimageview.CircleImageView;


public class BadgesAdapter extends SupportAnnotatedAdapter implements BadgesAdapterBinder {

    List<Badge> badgeList;

    public BadgesAdapter(Context context, List<Badge> badges) {
        super(context);
        badgeList = badges;
    }

    @ViewType(
            layout = R.layout.item_badge,
            views =
                    {
                            @ViewField(id = R.id.badge, type = CircleImageView.class, name = "badge"),
                            @ViewField(id = R.id.badge_title, type = TextView.class, name = "badgeTitle")

                    }
    )
    public static final int BADGE = 0;

    @Override
    public int getItemCount() {
        return badgeList != null ? badgeList.size() : 0;
    }


    @Override
    public void bindViewHolder(BadgesAdapterHolders.BADGEViewHolder vh, int position) {
        vh.badgeTitle.setText(badgeList.get(position).getTitle());
        MyApplication.instance.getPicasso().load(badgeList.get(position).getImage()).into(vh.badge);
    }
}
