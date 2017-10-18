package aroundwise.nepi.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.eventbus.CollectionOfferEvent;
import aroundwise.nepi.eventbus.OfferDetailEvent;
import aroundwise.nepi.network.model.CoverOfferGroup;
import aroundwise.nepi.network.model.Offer;
import de.greenrobot.event.EventBus;

/**
 * Created by irina on 12/22/2016.
 */
public class CoverOfferPagerAdapter extends PagerAdapter {

    private Context context;
    private List<CoverOfferGroup> coverOfferGroups;

    public CoverOfferPagerAdapter(Context context, List<CoverOfferGroup> coverOfferGroups) {
        this.context = context;
        this.coverOfferGroups = coverOfferGroups;
    }

    @Override
    public int getCount() {
        return coverOfferGroups != null ? coverOfferGroups.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.item_cover_group, container, false);
        ImageView ivOffer = (ImageView) layout.findViewById(R.id.offerImage);
        TextView tvMain = (TextView) layout.findViewById(R.id.mainText);
        TextView tvSecond = (TextView) layout.findViewById(R.id.secondText);
        Button button = (Button) layout.findViewById(R.id.offerButton);
        final CoverOfferGroup offer = coverOfferGroups.get(position);
        tvMain.setText(offer.getGroupHeadline());
        tvSecond.setText(offer.getGroupSubHeadline());
        MyApplication.instance.getPicasso().load(offer.getImage()).fit().placeholder(R.drawable.placeholder).into(ivOffer);
        container.addView(layout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new CollectionOfferEvent(offer.getId(), offer.getGroupHeadline(), false));
            }
        });
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}