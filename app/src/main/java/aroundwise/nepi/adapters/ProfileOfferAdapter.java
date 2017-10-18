package aroundwise.nepi.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferPresenter;
import aroundwise.nepi.eventbus.AnswerPollEvent;
import aroundwise.nepi.eventbus.OfferDetailEvent;
import aroundwise.nepi.eventbus.OffersListEmptyEvent;
import aroundwise.nepi.eventbus.StoreDetailsEvent;
import aroundwise.nepi.eventbus.StoreMapEvent;
import aroundwise.nepi.network.model.Answer;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.Poll;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.NetworkUtils;
import aroundwise.nepi.util.views.CirclePageIndicator;
import aroundwise.nepi.util.views.CustomViewPager;
import aroundwise.nepi.util.views.SAutoBgButton;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mihai on 29/08/16.
 */
public class ProfileOfferAdapter extends SupportAnnotatedAdapter implements ProfileOfferAdapterBinder {

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
                            @ViewField(id = R.id.videoview, type = YouTubeThumbnailView.class, name = "videoView"),
                            @ViewField(id = R.id.rl_top, type = RelativeLayout.class, name = "rlBottomBar"),
                            @ViewField(id = R.id.iv_play, type = ImageView.class, name = "ivPlayButton"),
                            @ViewField(id = R.id.rl_videoview, type = RelativeLayout.class, name = "rlVideoView"),
                            @ViewField(id = R.id.offer_header, type = RelativeLayout.class, name = "rlHeaderStore"),
                            @ViewField(id = R.id.btn_play, type = Button.class, name = "btnPlayVideo"),
                            @ViewField(id = R.id.badge_1, type = View.class, name = "badge1"),
                            @ViewField(id = R.id.badge_2, type = View.class, name = "badge2"),
                            @ViewField(id = R.id.tv_points, type = TextView.class, name = "tvPrice")
                    }
    )
    public static final int offerBig = 0;

    @ViewType(
            layout = R.layout.item_offer_small,
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
                            @ViewField(id = R.id.videoview, type = YouTubeThumbnailView.class, name = "videoView"),
                            @ViewField(id = R.id.rl_top, type = RelativeLayout.class, name = "rlBottomBar"),
                            @ViewField(id = R.id.iv_play, type = ImageView.class, name = "ivPlayButton"),
                            @ViewField(id = R.id.rl_videoview, type = RelativeLayout.class, name = "rlVideoView"),
                            @ViewField(id = R.id.offer_header, type = RelativeLayout.class, name = "rlHeaderStore"),
                            @ViewField(id = R.id.btn_play, type = Button.class, name = "btnPlayVideo"),
                            @ViewField(id = R.id.badge_1, type = View.class, name = "badge1"),
                            @ViewField(id = R.id.badge_2, type = View.class, name = "badge2"),
                            @ViewField(id = R.id.tv_points, type = TextView.class, name = "tvPrice")

                    }
    )
    public static final int offerSmall = 1;

    @ViewType(
            layout = R.layout.item_loading_row,
            views =
                    {
                            @ViewField(id = R.id.progressbar, type = ProgressBar.class, name = "progressbar"),
                    }
    )
    public static final int loadingRow = 2;

    @ViewType(
            layout = R.layout.poll_item_pictures,
            views =
                    {
                            @ViewField(id = R.id.tvQuestion, type = TextView.class, name = "tvQuestions"),
                            @ViewField(id = R.id.tvQuestionPoints, type = TextView.class, name = "tvQuestionPoints"),
                            @ViewField(id = R.id.ivTopLeft, type = ImageView.class, name = "ivTopLeft"),
                            @ViewField(id = R.id.ivTopRight, type = ImageView.class, name = "ivTopRight"),
                            @ViewField(id = R.id.ivBottomLeft, type = ImageView.class, name = "ivBottomLeft"),
                            @ViewField(id = R.id.ivBottomRight, type = ImageView.class, name = "ivBottomRight"),
                            @ViewField(id = R.id.pollSubmitButton, type = SAutoBgButton.class, name = "buttonSubmit")
                    }

    )
    public static final int pollPictureRow = 3;

    @ViewType(
            layout = R.layout.poll_item_slide,
            views =
                    {
                            @ViewField(id = R.id.tvQuestion, type = TextView.class, name = "tvQuestions"),
                            @ViewField(id = R.id.tvQuestionPoints, type = TextView.class, name = "tvQuestionPoints"),
                            @ViewField(id = R.id.iv1, type = ImageView.class, name = "iv1"),
                            @ViewField(id = R.id.iv2, type = ImageView.class, name = "iv2"),
                            @ViewField(id = R.id.iv3, type = ImageView.class, name = "iv3"),
                            @ViewField(id = R.id.iv4, type = ImageView.class, name = "iv4"),
                            @ViewField(id = R.id.iv5, type = ImageView.class, name = "iv5"),
                            @ViewField(id = R.id.pollSubmitButton, type = SAutoBgButton.class, name = "buttonSubmit")
                    }

    )
    public static final int pollSlideRow = 4;

    @ViewType(
            layout = R.layout.poll_multiple,
            views =
                    {
                            @ViewField(id = R.id.tvQuestion, type = TextView.class, name = "tvQuestions"),
                            @ViewField(id = R.id.tvQuestionPoints, type = TextView.class, name = "tvQuestionPoints"),
                            @ViewField(id = R.id.wholeItem, type = RelativeLayout.class, name = "wholeItem"),
                            @ViewField(id = R.id.recycler_questions, type = RecyclerView.class, name = "recyclerViewAnswers"),
                            @ViewField(id = R.id.pollSubmitButton, type = SAutoBgButton.class, name = "buttonSubmit")
                    }

    )
    public static final int pollMultipleeRow = 5;

    @ViewType(
            layout = R.layout.item_offers_slider,
            views =
                    {
                            @ViewField(id = R.id.rl, type = RelativeLayout.class, name = "rl"),
                            @ViewField(id = R.id.viewpager, type = CustomViewPager.class, name = "viewPager"),
                            @ViewField(id = R.id.viewpager_indicator, type = CirclePageIndicator.class, name = "pageIndicator")
                    }
    )
    public static final int slider = 6;


    private List<Offer> offers;
    private List<Offer> firstSliderOffers;
    private List<Offer> secondSliderOffers;
    private Context context;
    public OnLoadMoreListener onLoadMoreListener;
    private int positionSelected = -1;
    private Target target;
    private boolean isInDetailStore = false;
    private boolean isInFavoriteScreen = false;
    private boolean shouldShowSliders = false;
    private boolean shouldShowSecondSlider = false;
    private long offerLikes;


    public ProfileOfferAdapter(Context context, List<Offer> offers) {
        super(context);
        this.context = context;
        this.offers = offers;
    }

    public void setShouldShowSecondSlider(boolean shouldShowSecondSlider) {
        this.shouldShowSecondSlider = shouldShowSecondSlider;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void setFirstSliderOffers(List<Offer> firstSliderOffers) {
        this.firstSliderOffers = firstSliderOffers;
    }

    public void setSecondSliderOffers(List<Offer> secondSliderOffers) {
        this.secondSliderOffers = secondSliderOffers;
    }

    public void setShouldShowSliders(boolean shouldShowSliders) {
        this.shouldShowSliders = shouldShowSliders;
    }

    public void setInFavoriteScreen(boolean inFavoriteScreen) {
        isInFavoriteScreen = inFavoriteScreen;
    }

    public void setInDetailStore(boolean inDetailStore) {
        isInDetailStore = inDetailStore;
    }

    public int getPositionSelected() {
        return positionSelected;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemCount() {
        int firstSliderItems = shouldShowSliders ? 1 : 0;
        int secondSliderItems = shouldShowSecondSlider ? 1 : 0;
        return offers == null ? 0 : offers.size() + firstSliderItems + secondSliderItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (offers.get(position) == null)
            return loadingRow;
        if (offers.get(position).getPoll() != null)
            return pollMultipleeRow;
        if (position % 3 == 0) {
            return offerBig;
        }
        if (position % 3 == 1) {
            return offerSmall;
        }
        if (position % 3 == 2) {
            return offerSmall;
        }
        return offerBig;

    }

    @Override
    public void bindViewHolder(final ProfileOfferAdapterHolders.OfferBigViewHolder vh, final int position) {
        final Offer offer = offers.get(position);
        vh.iv_offer_photo.setVisibility(View.VISIBLE);
        vh.videoView.setVisibility(View.INVISIBLE);
        vh.rlVideoView.setVisibility(View.INVISIBLE);
        if (offer.getImages() != null && offer.getImages().size() > 0)
            MyApplication.instance.getPicasso().load(offer.getImages().get(0).getImage()).fit().centerCrop().placeholder(R.drawable.placeholder).into(vh.iv_offer_photo);
        vh.tv_likes.setText(offer.getLikes() + "");
        if (Session.getUser().getProfile() != null
                && Session.getUser().getProfile().getMall() != null)
            MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getMall().getLogo()).into(vh.iv_offer_mall_logo);
        vh.tv_offer_description.setText(offer.getLongDesc());
        vh.tv_offer_title.setText(Html.fromHtml(offer.getTitle()));
        vh.tv_offer_owner.setText(offer.getStore().getName());
        vh.layout_offer_likes.setVisibility(View.GONE);
        vh.iv_heart.setActivated(offer.isLike());
        MyApplication.instance.getPicasso().load(offer.getStore().getLogo()).into(vh.iv_offer_logo);
        vh.iv_offer_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new OfferDetailEvent(offer));
                positionSelected = position;
            }
        });
        vh.rlBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new OfferDetailEvent(offer));
                positionSelected = position;
            }
        });
        vh.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (askForWritePermission()) {
                    //vh.btn_share.setEnabled(false);
                    /*Timer buttonTimer = new Timer();
                    buttonTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    vh.btn_share.setEnabled(true);
                                }
                            });
                        }
                    }, 3000);*/
                    share(offer);
                    positionSelected = position;
                }
            }
        });
        vh.iv_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerLikes = offer.getLikes();
                if (isInFavoriteScreen) {
                    if (vh.iv_heart.isActivated()) {
                        new AlertDialog.Builder(context)
                                .setMessage("Oferta nu o sa mai fie in lista de Favorite. Esti sigur?")
                                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        OfferPresenter.dislikeOffer(offer);
                                        removeItem(position);
                                        offerLikes--;
                                        vh.iv_heart.setActivated(false);
                                        offer.setLike(vh.iv_heart.isActivated());
                                        offer.setLikes(offerLikes);
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }
                } else {
                    if (vh.iv_heart.isActivated()) {
                        OfferPresenter.dislikeOffer(offer);
                        offerLikes--;
                        vh.iv_heart.setActivated(false);
                        offer.setLike(vh.iv_heart.isActivated());
                        offer.setLikes(offerLikes);
                        notifyDataSetChanged();

                    } else {
                        offerLikes++;
                        OfferPresenter.likeOffer(offer, Session.getUser());
                        vh.iv_heart.setActivated(true);
                        offer.setLike(vh.iv_heart.isActivated());
                        offer.setLikes(offerLikes);
                        notifyDataSetChanged();
                    }
                }
            }
        });
        vh.btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionSelected = position;
                if (offer.getStore().getLatitude() != null && offer.getStore().getLongitude() != null)
                    EventBus.getDefault().post(new StoreMapEvent(offer.getStore(), null));
                else
                    Toast.makeText(context, "Acest magazin nu are setată o locație. Magazinul îl puteți găsi în mall-ul " + Session.getUser().getProfile().getMall().getName(), Toast.LENGTH_SHORT).show();
            }
        });

        if (offer.getVideoUrl() != null && !offer.getVideoUrl().isEmpty()
                && offer.getVideoUrl().trim().length() > 1) {
            vh.iv_offer_photo.setVisibility(View.INVISIBLE);
            vh.videoView.setVisibility(View.VISIBLE);
            vh.rlVideoView.setVisibility(View.VISIBLE);
            UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(offer.getVideoUrl());
            final String videoId = sanitizer.getValue("v");
            final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

                }

                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                    Log.e(OfferAdapter.class.getSimpleName(), errorReason.toString());
                }
            };
            vh.videoView.initialize(context.getString(R.string.google_api_key_me), new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(videoId);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.e(OfferAdapter.class.getSimpleName(), youTubeInitializationResult.toString());
                }
            });
            vh.btnPlayVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positionSelected = position;
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context,
                            context.getString(R.string.google_api_key_me),
                            videoId,//video id
                            100,     //after this time, video will start automatically
                            true,               //autoplay or not
                            true);             //lightbox mode or not; show the video in a small box
                    context.startActivity(intent);
                }
            });
        }
        vh.rlHeaderStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInDetailStore)
                    EventBus.getDefault().post(new StoreDetailsEvent(offer.getStore()));
            }
        });
        if (isInDetailStore)
            vh.rlHeaderStore.setVisibility(View.GONE);

        vh.badge1.setVisibility(View.INVISIBLE);
        vh.badge2.setVisibility(View.INVISIBLE);

        if (offer.getDiscount() != null && !offer.getDiscount().isEmpty()) {
            vh.badge1.setVisibility(View.VISIBLE);
            TextView tv1 = (TextView) vh.badge1.findViewById(R.id.tvBuc);
            TextView tv2 = (TextView) vh.badge1.findViewById(R.id.tvDesc);
            tv1.setText(offer.getDiscount());
            tv2.setText("%");
        }
        if (offer.getDate() != null && !offer.getDate().isEmpty()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(offer.getDate());
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(date);
                vh.badge1.setVisibility(View.VISIBLE);
                vh.badge2.setVisibility(View.VISIBLE);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                String sMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
                if (month == 0)
                    sMonth = "Ianuarie";
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.valueOf(minutes));
                if (minutes == 0)
                    stringBuilder.append("0");
                TextView tv1 = (TextView) vh.badge1.findViewById(R.id.tvBuc);
                TextView tv2 = (TextView) vh.badge1.findViewById(R.id.tvDesc);
                tv1.setText(String.valueOf(dayOfMonth));
                tv2.setText(sMonth.substring(0, 3).toUpperCase());
                tv1.setTextSize(16);
                tv2.setTextSize(14);
                TextView tv3 = (TextView) vh.badge2.findViewById(R.id.tvBuc);
                TextView tv4 = (TextView) vh.badge2.findViewById(R.id.tvDesc);
                tv4.setVisibility(View.GONE);
                String shour = String.valueOf(hour) + ":" + stringBuilder.toString();
                tv3.setText(shour);
                tv3.setTextSize(16);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        if (offer.getPriceText() != null && !offer.getPriceText().equals("null")) {
            vh.tvPrice.setText(offer.getPriceText());
        }
    }

    @Override
    public void bindViewHolder(final ProfileOfferAdapterHolders.OfferSmallViewHolder vh, final int position) {
        final Offer offer = offers.get(position);
        vh.iv_offer_photo.setVisibility(View.VISIBLE);
        vh.videoView.setVisibility(View.INVISIBLE);
        vh.rlVideoView.setVisibility(View.INVISIBLE);
        if (offer.getImages() != null && offer.getImages().size() > 0)
            MyApplication.instance.getPicasso().load(offer.getImages().get(0).getImage()).fit().centerCrop().placeholder(R.drawable.placeholder).into(vh.iv_offer_photo);
        if (Session.getUser().getProfile() != null
                && Session.getUser().getProfile().getMall() != null)
            MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getMall().getLogo()).into(vh.iv_offer_mall_logo);
        vh.tv_likes.setText(offer.getLikes() + "");
        vh.tv_offer_description.setText(offer.getLongDesc());
        vh.tv_offer_title.setText(offer.getTitle());
        vh.iv_heart.setActivated(offer.isLike());
        vh.tv_offer_owner.setText(offer.getStore().getName());
        vh.layout_offer_likes.setVisibility(View.GONE);
        if (offer.getStore().getLogo() != null)
            MyApplication.instance.getPicasso().load(offer.getStore().getLogo()).into(vh.iv_offer_logo);
        vh.iv_offer_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new OfferDetailEvent(offer));
                positionSelected = position;
            }
        });
        vh.rlBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new OfferDetailEvent(offer));
                positionSelected = position;
            }
        });
        vh.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (askForWritePermission()) {
                    vh.btn_share.setEnabled(false);
                    Timer buttonTimer = new Timer();
                    /*buttonTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            vh.btn_share.setEnabled(true);
                        }
                    }, 3000);*/
                    share(offer);
                    positionSelected = position;
                }
            }
        });
        vh.btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionSelected = position;
                if (offer.getStore().getLatitude() != null && offer.getStore().getLongitude() != null)
                    EventBus.getDefault().post(new StoreMapEvent(offer.getStore(), null));
                else
                    Toast.makeText(context, "Acest magazin nu are setată o locație. Magazinul îl puteți găsi în mall-ul " + Session.getUser().getProfile().getMall().getName(), Toast.LENGTH_SHORT).show();
            }
        });
       /* vh.iv_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long offerLikes = offer.getLikes();
                if (vh.iv_heart.isActivated()) {
                    OfferPresenter.dislikeOffer(offer);
                    if (isInFavoriteScreen) {
                        removeItem(position);
                    } else {
                        offerLikes--;
                        vh.iv_heart.setActivated(false);
                        offer.setLike(vh.iv_heart.isActivated());
                        offer.setLikes(offerLikes);
                        notifyDataSetChanged();
                    }
                } else {
                    offerLikes++;
                    OfferPresenter.likeOffer(offer, Session.getUser());
                    vh.iv_heart.setActivated(true);
                    offer.setLike(vh.iv_heart.isActivated());
                    offer.setLikes(offerLikes);
                    notifyDataSetChanged();
                }
            }
        });*/
        vh.iv_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerLikes = offer.getLikes();
                if (isInFavoriteScreen) {
                    if (vh.iv_heart.isActivated()) {
                        new AlertDialog.Builder(context)
                                .setMessage("Oferta nu o sa mai fie in lista de Favorite. Esti sigur?")
                                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        OfferPresenter.dislikeOffer(offer);
                                        removeItem(position);
                                        offerLikes--;
                                        vh.iv_heart.setActivated(false);
                                        offer.setLike(vh.iv_heart.isActivated());
                                        offer.setLikes(offerLikes);
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }
                } else {
                    if (vh.iv_heart.isActivated()) {
                        OfferPresenter.dislikeOffer(offer);
                        offerLikes--;
                        vh.iv_heart.setActivated(false);
                        offer.setLike(vh.iv_heart.isActivated());
                        offer.setLikes(offerLikes);
                        notifyDataSetChanged();

                    } else {
                        offerLikes++;
                        OfferPresenter.likeOffer(offer, Session.getUser());
                        vh.iv_heart.setActivated(true);
                        offer.setLike(vh.iv_heart.isActivated());
                        offer.setLikes(offerLikes);
                        notifyDataSetChanged();
                    }
                }
            }
        });
        if (offer.getVideoUrl() != null && !offer.getVideoUrl().isEmpty()
                && offer.getVideoUrl().trim().length() > 1) {
            vh.iv_offer_photo.setVisibility(View.INVISIBLE);
            vh.videoView.setVisibility(View.VISIBLE);
            vh.rlVideoView.setVisibility(View.VISIBLE);
            UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(offer.getVideoUrl());
            final String videoId = sanitizer.getValue("v");
            final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

                }

                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                    Log.e(OfferAdapter.class.getSimpleName(), errorReason.toString());
                }
            };
            vh.videoView.initialize(context.getString(R.string.google_api_key_me), new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(videoId);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.e(OfferAdapter.class.getSimpleName(), youTubeInitializationResult.toString());
                }
            });
            vh.btnPlayVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positionSelected = position;
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context,
                            context.getString(R.string.google_api_key_me),
                            videoId,//video id
                            100,     //after this time, video will start automatically
                            true,               //autoplay or not
                            true);             //lightbox mode or not; show the video in a small box
                    context.startActivity(intent);
                }
            });
        }

        vh.rlHeaderStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInDetailStore)
                    EventBus.getDefault().post(new StoreDetailsEvent(offer.getStore()));
            }
        });

        if (isInDetailStore)
            vh.rlHeaderStore.setVisibility(View.GONE);

        vh.badge1.setVisibility(View.INVISIBLE);
        vh.badge2.setVisibility(View.INVISIBLE);

        if (offer.getDiscount() != null && !offer.getDiscount().isEmpty()) {
            vh.badge1.setVisibility(View.VISIBLE);
            TextView tv1 = (TextView) vh.badge1.findViewById(R.id.tvBuc);
            TextView tv2 = (TextView) vh.badge1.findViewById(R.id.tvDesc);
            tv1.setTextSize(10);
            tv1.setText(offer.getDiscount());
            tv2.setText("%");
            tv2.setTextSize(10);
        }
        if (offer.getDate() != null && !offer.getDate().isEmpty()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(offer.getDate());
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(date);
                vh.badge1.setVisibility(View.VISIBLE);
                vh.badge2.setVisibility(View.VISIBLE);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                String sMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
                if (month == 0)
                    sMonth = "Ianuarie";
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.valueOf(minutes));
                if (minutes == 0)
                    stringBuilder.append("0");
                TextView tv1 = (TextView) vh.badge1.findViewById(R.id.tvBuc);
                TextView tv2 = (TextView) vh.badge1.findViewById(R.id.tvDesc);
                tv1.setText(String.valueOf(dayOfMonth));
                tv2.setText(sMonth.substring(0, 3).toUpperCase());
                tv1.setTextSize(12);
                tv2.setTextSize(10);
                TextView tv3 = (TextView) vh.badge2.findViewById(R.id.tvBuc);
                TextView tv4 = (TextView) vh.badge2.findViewById(R.id.tvDesc);
                tv4.setVisibility(View.GONE);
                String shour = String.valueOf(hour) + ":" + stringBuilder.toString();
                tv3.setText(shour);
                tv3.setTextSize(12);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        if (offer.getPriceText() != null && !offer.getPriceText().equals("null")) {
            vh.tvPrice.setText(offer.getPriceText());
        }
    }

    @Override
    public void bindViewHolder(ProfileOfferAdapterHolders.LoadingRowViewHolder vh, int position) {

    }

    @Override
    public void bindViewHolder(ProfileOfferAdapterHolders.PollPictureRowViewHolder vh, int position) {

    }

    @Override
    public void bindViewHolder(ProfileOfferAdapterHolders.PollSlideRowViewHolder vh, int position) {

    }

    @Override
    public void bindViewHolder(final ProfileOfferAdapterHolders.PollMultipleeRowViewHolder vh, int position) {
        Offer offer = offers.get(position);
        final Poll poll = offer.getPoll();
        vh.tvQuestions.setText(poll.getQuestion());
        String points = " PUNCTE";
        if (poll.getPoints() == 1)
            points = " PUNCT";
        vh.tvQuestionPoints.setText(poll.getPoints() + points);
        final QuestionsAdapter adapter = new QuestionsAdapter(context, poll.getAnswers());
        vh.recyclerViewAnswers.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        vh.recyclerViewAnswers.setAdapter(adapter);
        vh.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.checkNetworkConnection(context)) {
                    Toast.makeText(context, "Nu exista conexiune la internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                int positionSelected = adapter.positionSelected;
                if (positionSelected == -1) {
                    Toast.makeText(context, "Alegeți mai întăi un raspuns.", Toast.LENGTH_SHORT).show();
                    return;
                }
                OfferPresenter.submitPollAnswer(poll.getResourceUri(),
                        Integer.valueOf(poll.getAnswers().get(positionSelected).getId()), poll.getPoints());
                setViewAndChildrenEnabled(vh.wholeItem, false);
                vh.buttonSubmit.setText("Mulțumim pentru răspuns");
                EventBus.getDefault().post(new AnswerPollEvent(Session.getUser().getProfile().getMall().getLogo(), poll.getPoints()));
            }
        });

        for (Answer answer : poll.getAnswers()) {
            if (answer.isUserAnswered()) {
                setViewAndChildrenEnabled(vh.wholeItem, false);
                vh.buttonSubmit.setText("Mulțumim pentru răspuns");
                break;
            }
        }
    }

    @Override
    public void bindViewHolder(ProfileOfferAdapterHolders.SliderViewHolder vh, int position) {
        List<Offer> sliderOffers = position == 0 ? firstSliderOffers : secondSliderOffers;
        if (sliderOffers != null) {
            FeaturedOffersViewPagerAdapter adapter = new FeaturedOffersViewPagerAdapter(context, sliderOffers);
            vh.viewPager.setAdapter(adapter);
            vh.pageIndicator.setViewPager(vh.viewPager);
        }
    }

    private void deselectRow(View row1, View row2, View row3, View icon1, View icon2, View icon3) {
        row1.setSelected(false);
        row2.setSelected(false);
        row3.setSelected(false);
        icon1.setSelected(false);
        icon2.setSelected(false);
        icon3.setSelected(false);
    }

    private void disableRows(View row1, View row2, View row3, View icon1, View icon2, View icon3) {
        row1.setEnabled(false);
        row2.setEnabled(false);
        row3.setEnabled(false);
        icon1.setEnabled(false);
        icon2.setEnabled(false);
        icon3.setEnabled(false);
    }

    private void enableRow(View row, View icon) {
        row.setEnabled(true);
        icon.setEnabled(true);
    }

    private int getSelectedRow(View row1, View row2, View row3) {
        if (row1.isSelected())
            return 0;
        if (row2.isSelected())
            return 1;
        if (row3.isSelected())
            return 2;
        return -1;
    }

    private void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        view.setClickable(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }


    private void share(final Offer offer) {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Uri imageUri = getBitmapUri(bitmap, offer.getTitle());
                String title = "A-ha!, am gasit ceva ce ti-ar placea: " + offer.getTitle() + " "
                        + offer.getStore().getName() + ". Descarca aplicatia Aha!:https://play.google.com/store/apps/details?id=aroundwise.nepi";
                startShareIntentImage(imageUri, title);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (offer.getVideoUrl() != null && !offer.getVideoUrl().isEmpty()
                && offer.getVideoUrl().trim().length() > 1)
            startShareIntentVideo(offer.getVideoUrl(), offer.getTitle());
        else
            MyApplication.instance.getPicasso().load(offer.getImages().get(0).getImage()).into(target);
    }

    private Uri getBitmapUri(Bitmap bitmap, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, title, null);
        return Uri.parse(path);
    }

    private void startShareIntentImage(Uri imageUri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, title);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // shareIntent.setPackage("com.facebook.katana");
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.label_share)));
    }

    private void startShareIntentVideo(String imageUri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, imageUri);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.label_share)));
    }

    private void removeItem(int position) {
        offers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, offers.size());
        if (offers.size() == 0)
            EventBus.getDefault().post(new OffersListEmptyEvent("You have no favourite offers"));
    }

    private boolean askForWritePermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_WRITE);
            return false;
        }
        return true;
    }

}
