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
import aroundwise.nepi.network.model.Offer;
import de.greenrobot.event.EventBus;

/**
 * Created by irina on 12/20/2016.
 */
public class FeaturedOffersViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Offer> featuredOffers;
    Offer offer1;
    Offer offer2;
    Offer offer3;
    private String firstHeadline;
    private String secondHeadline;

    public FeaturedOffersViewPagerAdapter(Context context, List<Offer> featuredOffers) {
        this.context = context;
        this.featuredOffers = featuredOffers;
    }

    public FeaturedOffersViewPagerAdapter(Context context, List<Offer> featuredOffers, String firstHeadline, String secondHeadline) {
        this.context = context;
        this.featuredOffers = featuredOffers;
        this.firstHeadline = firstHeadline;
        this.secondHeadline = secondHeadline;
    }

    @Override
    public int getCount() {
        int pages = featuredOffers.size() / 3;
        if (featuredOffers.size() % 3 != 0)
            pages++;
        return featuredOffers != null ? pages : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int pageNumber = position * 3;
        final int poz1 = pageNumber;
        final int poz2 = pageNumber + 1;
        final int poz3 = pageNumber + 2;
        offer1 = null;
        offer2 = null;
        offer3 = null;
        if (featuredOffers.size() > poz1)
            offer1 = featuredOffers.get(poz1);
        if (featuredOffers.size() > poz2)
            offer2 = featuredOffers.get(poz2);
        if (featuredOffers.size() > poz3)
            offer3 = featuredOffers.get(poz3);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.item_featured_offer, container, false);
        TextView tvMain = (TextView) layout.findViewById(R.id.mainText);
        TextView tvSecond = (TextView) layout.findViewById(R.id.secondText);
        ImageView ivOffer1 = (ImageView) layout.findViewById(R.id.iv_1);
        ImageView ivOffer2 = (ImageView) layout.findViewById(R.id.iv_2);
        ImageView ivOffer3 = (ImageView) layout.findViewById(R.id.iv_3);
        Button btn1 = (Button) layout.findViewById(R.id.btn1);
        Button btn2 = (Button) layout.findViewById(R.id.btn2);
        Button btn3 = (Button) layout.findViewById(R.id.btn3);
        final Offer offer = featuredOffers.get(position);
        if (offer1 != null && offer1.getImages().size() > 0 && offer1.getImages().get(0) != null)
            MyApplication.instance.getPicasso().load(offer1.getImages().get(0).getImage()).into(ivOffer1);
        if (offer2 != null && offer2.getImages().size() > 0 && offer2.getImages().get(0) != null)
            MyApplication.instance.getPicasso().load(offer2.getImages().get(0).getImage()).into(ivOffer2);
        if (offer3 != null && offer3.getImages().size() > 0 && offer.getImages().get(0) != null)
            MyApplication.instance.getPicasso().load(offer3.getImages().get(0).getImage()).into(ivOffer3);

        tvMain.setText(firstHeadline);
        tvSecond.setText(secondHeadline);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (featuredOffers.size() > poz1 && featuredOffers.get(poz1) != null)
                    EventBus.getDefault().post(new OfferDetailEvent(featuredOffers.get(poz1)));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (featuredOffers.size() > poz2 && featuredOffers.get(poz2) != null)
                    EventBus.getDefault().post(new OfferDetailEvent(featuredOffers.get(poz2)));
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (featuredOffers.size() > poz3 && featuredOffers.get(poz3) != null)
                    EventBus.getDefault().post(new OfferDetailEvent(featuredOffers.get(poz3)));
            }
        });
        tvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CollectionOfferEvent(0, firstHeadline, true));
            }
        });
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
