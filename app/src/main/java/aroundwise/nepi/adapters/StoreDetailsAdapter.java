package aroundwise.nepi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferPresenter;
import aroundwise.nepi.eventbus.PurchasePointsEvent;
import aroundwise.nepi.eventbus.StoreMapEvent;
import aroundwise.nepi.eventbus.WalkinPointsEvent;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.util.Blur;
import aroundwise.nepi.util.LocationUtil;
import aroundwise.nepi.util.views.CirclePageIndicator;
import aroundwise.nepi.util.views.CustomViewPager;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by irina on 1/11/2017.
 */
public class StoreDetailsAdapter extends SupportAnnotatedAdapter implements StoreDetailsAdapterBinder {


    @ViewType(
            layout = R.layout.store_details_viewpager,
            views = {
                    @ViewField(id = R.id.iv_placeholder, type = ImageView.class, name = "ivPlaceholder"),
                    @ViewField(id = R.id.viewpager, type = CustomViewPager.class, name = "viewpager"),
                    @ViewField(id = R.id.viewpager_indicator, type = CirclePageIndicator.class, name = "circlepageindicator"),
                    @ViewField(id = R.id.rl_mall_location, type = RelativeLayout.class, name = "rlMallLocation"),
                    @ViewField(id = R.id.iv_store_logo, type = ImageView.class, name = "ivStoreLogo"),
                    @ViewField(id = R.id.tv_store_location, type = TextView.class, name = "tvStoreLocation"),
                    @ViewField(id = R.id.tv_dialog_arrog, type = TextView.class, name = "tvStoreDistance")
            }
    )
    public static final int storeViewpager = 0;

    @ViewType(
            layout = R.layout.store_details_points,
            views = {
                    @ViewField(id = R.id.iv_getpoints_blurred, type = ImageView.class, name = "ivGetPointsBlurred"),
                    @ViewField(id = R.id.rl_get_points, type = RelativeLayout.class, name = "rlGetPointsBuying"),
                    @ViewField(id = R.id.tv_stores_points_left, type = TextView.class, name = "tvPointsBuying"),
                    @ViewField(id = R.id.tv_buy_label_puncte, type = TextView.class, name = "tvBuyLabelPuncte"),
                    @ViewField(id = R.id.rl_walk_in, type = RelativeLayout.class, name = "rlGetPointsWalkIn"),
                    @ViewField(id = R.id.tv_stores_points_right, type = TextView.class, name = "tvPointsWalkIn"),
                    @ViewField(id = R.id.tv_walkin_label_puncte, type = TextView.class, name = "tvWalkInLabelPuncte")
            }
    )
    public static final int storePoints = 1;

    @ViewType(
            layout = R.layout.store_details_rewards,
            views = {
                    @ViewField(id = R.id.tv_store_rewards, type = TextView.class, name = "tvStoreRewards"),
                    @ViewField(id = R.id.recycler_rewards, type = RecyclerView.class, name = "recyclerViewRewards")
            }
    )
    public static final int storeRewads = 2;

    @ViewType(
            layout = R.layout.store_details_offers,
            views = {
                    @ViewField(id = R.id.recycler_offers, type = RecyclerView.class, name = "recyclerViewOffers")
            }
    )
    public static final int storeOffers = 3;

    @ViewType(
            layout = R.layout.item_offer_big_discount_top_right,
            views =
                    {
                            @ViewField(id = R.id.offer_likes, type = RelativeLayout.class, name = "layout_offer_likes"),
                            @ViewField(id = R.id.iv_like_photo, type = CircleImageView.class, name = "iv_like_photo"),
                            @ViewField(id = R.id.tv_like_text, type = TextView.class, name = "tv_like_text"),
                            @ViewField(id = R.id.iv_offer_logo, type = ImageView.class, name = "iv_offer_logo"),
                            @ViewField(id = R.id.tv_offer_owner, type = TextView.class, name = "tv_offer_owner"),
                            @ViewField(id = R.id.iv_offer_mall_logo, type = ImageView.class, name = "iv_offer_mall_logo"),
                            @ViewField(id = R.id.iv_offer_photo, type = ImageView.class, name = "iv_offer_photo"),
                            @ViewField(id = R.id.tv_offer_title, type = TextView.class, name = "tv_offer_title"),
                            @ViewField(id = R.id.tv_offer_description, type = TextView.class, name = "tv_offer_description"),
                            @ViewField(id = R.id.iv_heart, type = ImageView.class, name = "iv_heart"),
                            @ViewField(id = R.id.btn_heart, type = Button.class, name = "btn_heart"),
                            @ViewField(id = R.id.tv_likes, type = TextView.class, name = "tv_likes"),
                            @ViewField(id = R.id.btn_share, type = Button.class, name = "btn_share"),
                            @ViewField(id = R.id.btn_location, type = Button.class, name = "btn_location"),
                            @ViewField(id = R.id.videoview, type = VideoView.class, name = "videoView"),
                            @ViewField(id = R.id.rl_top, type = RelativeLayout.class, name = "rlBottomBar"),
                            @ViewField(id = R.id.iv_play, type = ImageView.class, name = "ivPlayButton"),
                            @ViewField(id = R.id.rl_videoview, type = RelativeLayout.class, name = "rlVideoView"),
                            @ViewField(id = R.id.offer_header, type = RelativeLayout.class, name = "rlHeaderStore"),
                            @ViewField(id = R.id.badge_1, type = View.class, name = "badge1"),
                            @ViewField(id = R.id.badge_2, type = View.class, name = "badge2")

                    }
    )
    public static final int offerBig = 4;

    Shop store;
    Context context;
    Target target;
    List<Reward> storeRewards;
    List<Offer> offerRewards;

    public StoreDetailsAdapter(Context context, Shop store, List<Reward> storeRewards, List<Offer> offerRewards) {
        super(context);
        this.context = context;
        this.store = store;
        this.storeRewards = storeRewards;
        this.offerRewards = offerRewards;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return storeViewpager;
            case 1:
                return storePoints;
            case 2:
                return storeRewads;
            case 3:
                return storeOffers;
            default:
                return storeViewpager;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void bindViewHolder(StoreDetailsAdapterHolders.StoreViewpagerViewHolder vh, int position) {
        if (store.getImages() != null && store.getImages().size() > 0) {
            NetworkViewPagerAdapter adapter = new NetworkViewPagerAdapter(store.getImages(), context);
            vh.viewpager.setAdapter(adapter);
            vh.circlepageindicator.setViewPager(vh.viewpager);
        } else
            MyApplication.instance.getPicasso().load(R.drawable.placeholder).fit().into(vh.ivPlaceholder);
        MyApplication.instance.getPicasso().load(store.getLogo()).into(vh.ivStoreLogo);
        vh.tvStoreLocation.setText(store.getAddress());
        if (store.getLcoations().size() > 0) {
            String km = "";
            Location location = new Location("");
            location.setLatitude(store.getLcoations().get(0).getLatitude());
            location.setLongitude(store.getLcoations().get(0).getLongitude());
            Location lastLoc = LocationUtil.getLocationUtil(context).getLastLocation();
            if (lastLoc != null) {
                double distance = lastLoc.distanceTo(location);
                distance = distance / 1000;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(1);
                km = df.format(distance) + " ";
                vh.tvStoreDistance.setText(km);
            }
        }
        vh.rlMallLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StoreMapEvent(store, null));
            }
        });
    }

    @Override
    public void bindViewHolder(final StoreDetailsAdapterHolders.StorePointsViewHolder vh, int position) {
        if (store.getWalkin_points() == 1)
            vh.tvWalkInLabelPuncte.setText("PUNCT");
        if (store.getPointsPerBuy() == 1)
            vh.tvBuyLabelPuncte.setText("PUNCT");
        vh.tvPointsBuying.setText(store.getPointsPerBuy() + "%");
        vh.tvPointsWalkIn.setText(store.getWalkin_points() + "");
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                /*Bitmap blurred = Blur.blurRenderScript(bitmap, 15, context);//second parametre is radius
                vh.ivGetPointsBlurred.setImageBitmap(blurred);*/
                Blurry.with(context).radius(25).sampling(2).from(bitmap).into(vh.ivGetPointsBlurred);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d(StoreDetailsAdapter.class.getSimpleName(), "Bitmap failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (store.getImages() != null && store.getImages().size() > 0)
            MyApplication.instance.getPicasso().load(store.getImages().get(0).getImage()).placeholder(R.drawable.placeholder).into(target);
        else
            MyApplication.instance.getPicasso().load(R.drawable.placeholder).into(target);
        vh.rlGetPointsBuying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PurchasePointsEvent(store));
            }
        });
        vh.rlGetPointsWalkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new WalkinPointsEvent(store));
            }
        });

    }

    @Override
    public void bindViewHolder(StoreDetailsAdapterHolders.StoreRewadsViewHolder vh, int position) {
        if (storeRewards.size() != 0) {
            OnBoardingRewardAdapter adapterRewards = new OnBoardingRewardAdapter(context, false, true);
            adapterRewards.setRewards(storeRewards);
            vh.recyclerViewRewards.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            vh.recyclerViewRewards.setItemAnimator(new DefaultItemAnimator());
            vh.recyclerViewRewards.setAdapter(adapterRewards);
        } else {
            EmptyAdapter emptyAdapter = new EmptyAdapter(context, "Momentan nu există cadouri.");
            vh.recyclerViewRewards.setLayoutManager(new LinearLayoutManager(context));
            vh.recyclerViewRewards.setItemAnimator(new DefaultItemAnimator());
            vh.recyclerViewRewards.setAdapter(emptyAdapter);
        }
    }

    @Override
    public void bindViewHolder(StoreDetailsAdapterHolders.StoreOffersViewHolder vh, int position) {
        OfferPresenter.setLikedOffers(offerRewards);
        if (offerRewards != null && offerRewards.size() != 0) {
            ProfileOfferAdapter adapterOffer = new ProfileOfferAdapter(context, offerRewards);
            adapterOffer.setInDetailStore(true);
            initializeOffersRecyclerView(vh.recyclerViewOffers, adapterOffer);
        } else {
            EmptyAdapter emptyAdapter = new EmptyAdapter(context, "Momentan nu există oferte.");
            vh.recyclerViewOffers.setLayoutManager(new LinearLayoutManager(context));
            vh.recyclerViewOffers.setItemAnimator(new DefaultItemAnimator());
            vh.recyclerViewOffers.setAdapter(emptyAdapter);

        }
    }

    @Override
    public void bindViewHolder(StoreDetailsAdapterHolders.OfferBigViewHolder vh, int position) {

    }

    private void initializeOffersRecyclerView(RecyclerView recyclerView, ProfileOfferAdapter adapterOffer) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0) {
                    return 4;
                } else {
                    GridLayoutManager manager = new GridLayoutManager(context, 4);
                    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if (position % 3 == 0) {
                                return 4;
                            } else {
                            }
                            return 2;
                        }
                    });
                }
                return 2;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterOffer);
    }
}
