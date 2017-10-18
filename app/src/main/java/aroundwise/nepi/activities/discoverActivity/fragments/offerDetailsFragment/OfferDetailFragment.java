package aroundwise.nepi.activities.discoverActivity.fragments.offerDetailsFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferPresenter;
import aroundwise.nepi.adapters.NetworkViewPagerAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.eventbus.StoreDetailsEvent;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.CirclePageIndicator;
import aroundwise.nepi.util.views.CustomViewPager;
import aroundwise.nepi.util.views.SAutoBgButton;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 30/08/16.
 */
public class OfferDetailFragment extends BaseMvpFragment<OfferDetailFragmentView, OfferDetailFragmentPresenter> {

    @BindView(R.id.btn_back)
    SAutoBgButton btn_back;

    @BindView(R.id.btn_share_image)
    SAutoBgButton btnShare;

    @BindView(R.id.rl_buttom_buttons)
    RelativeLayout rl_bottom_buttons;

    @BindView(R.id.viewpager)
    CustomViewPager viewpager;

    @BindView(R.id.viewpager_indicator)
    CirclePageIndicator view_pager_indicator;

    @BindView(R.id.tv_offer_title)
    TextView tv_offer_title;

    @BindView(R.id.tv_offer_description)
    TextView tv_offer_description;

    @BindView(R.id.btn_like)
    SAutoBgButton btn_likes;

    @BindView(R.id.videoview)
    YouTubeThumbnailView videoView;

    @BindView(R.id.rl_videoview)
    RelativeLayout rlVideoView;

    @BindView(R.id.iv_play)
    ImageView ivPlayButton;

    @BindView(R.id.offer_header)
    RelativeLayout rlStoreHeader;

    @BindView(R.id.iv_offer_mall_logo)
    ImageView ivOfferMallLogo;

    @BindView(R.id.iv_offer_logo)
    ImageView ivOfferLogo;

    @BindView(R.id.tv_offer_owner)
    TextView tvOfferOwner;

    @BindView(R.id.btn_play)
    Button btnPlay;

    @BindView(R.id.badge_1)
    View badge1;

    @BindView(R.id.badge_2)
    View badge2;

    @BindView(R.id.tv_points)
    TextView tvPrice;

    Offer offer;
    Target target;

    public static OfferDetailFragment newInstance(Offer offer) {
        OfferDetailFragment fragment = new OfferDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.OFFER, offer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offer = getArguments().getParcelable(Constants.OFFER);
        hideProgress();

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_offer_detail;
    }

    @Override
    protected void setUpViews() {
        if (offer.getVideoUrl() != null && !offer.getVideoUrl().isEmpty())
            initilializeYoutubeView();
        rl_bottom_buttons.setVisibility(View.GONE);
        initializeViewPager();
        tv_offer_title.setText(offer.getTitle());
        tv_offer_description.setText(offer.getLongDesc());
        tvOfferOwner.setText(offer.getStore().getName());
        if (offer.getStore().mall != null)
            MyApplication.instance.getPicasso().load(offer.getStore().mall.getLogo()).into(ivOfferMallLogo);
        MyApplication.instance.getPicasso().load(offer.getStore().getLogo()).into(ivOfferLogo);
        if (Session.getFavoriteOfferIds().containsKey(offer.getId()))
            btn_likes.setActivated(true);
        else
            btn_likes.setActivated(false);
        if (offer.getPriceText() != null && !offer.getPriceText().equals("null")) {
            tvPrice.setText(offer.getPriceText());
        }
        initializeBadge();
    }

    @Override
    public OfferDetailFragmentPresenter createPresenter() {
        return new OfferDetailFragmentPresenter();
    }

    private void initializeBadge() {
        badge1.setVisibility(View.GONE);
        badge2.setVisibility(View.GONE);

        if (offer.getDiscount() != null && !offer.getDiscount().isEmpty()) {
            badge1.setVisibility(View.VISIBLE);
            TextView tv1 = (TextView) badge1.findViewById(R.id.tvBuc);
            TextView tv2 = (TextView) badge1.findViewById(R.id.tvDesc);
            tv1.setText(offer.getDiscount());
            tv2.setText("%");
        }

        if (offer.getDate() != null && !offer.getDate().isEmpty()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(offer.getDate());
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(date);
                badge1.setVisibility(View.VISIBLE);
                badge2.setVisibility(View.VISIBLE);
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
                TextView tv1 = (TextView) badge1.findViewById(R.id.tvBuc);
                TextView tv2 = (TextView) badge1.findViewById(R.id.tvDesc);
                tv1.setText(String.valueOf(dayOfMonth));
                tv2.setText(sMonth.substring(0, 3).toUpperCase());
                tv1.setTextSize(16);
                tv2.setTextSize(14);
                TextView tv3 = (TextView) badge2.findViewById(R.id.tvBuc);
                TextView tv4 = (TextView) badge2.findViewById(R.id.tvDesc);
                tv4.setVisibility(View.GONE);
                String shour = String.valueOf(hour) + ":" + stringBuilder.toString();
                tv3.setText(shour);
                tv3.setTextSize(16);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private void initilializeYoutubeView() {
        rlVideoView.setVisibility(View.VISIBLE);
        ivPlayButton.setVisibility(View.VISIBLE);
        viewpager.setVisibility(View.INVISIBLE);
        view_pager_indicator.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.VISIBLE);
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(offer.getVideoUrl()); //url is youtube url for which you want to extract the id.
        if (matcher.find()) {


            UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(offer.getVideoUrl());
            //final String videoId = sanitizer.getValue("v");
            final String videoId = matcher.group();
            final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

                }

                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                    Log.e(OfferDetailFragment.class.getSimpleName(), errorReason.toString());
                }
            };
            videoView.initialize(getActivity().getString(R.string.google_api_key_me), new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(videoId);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.e(OfferDetailFragment.class.getSimpleName(), youTubeInitializationResult.toString());
                }
            });
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                            getActivity().getString(R.string.google_api_key_me),
                            videoId,//video id
                            100,     //after this time, video will start automatically
                            true,               //autoplay or not
                            true);             //lightbox mode or not; show the video in a small box
                    startActivity(intent);
                }
            });
        }
    }


    private void initializeViewPager() {
        NetworkViewPagerAdapter adapter = new NetworkViewPagerAdapter(offer.getImages(), getActivity());
        viewpager.setAdapter(adapter);
        view_pager_indicator.setViewPager(viewpager);
    }

    @OnClick(R.id.btn_back)
    public void back() {
        ((DiscoverActivity) getActivity()).popFragment();
    }

    @OnClick(R.id.btn_share_image)
    public void onShareClick() {
        if (askForWritePermission()) {
            if (offer != null) {
                share(offer);
                //btnShare.setEnabled(false);
               /* Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btnShare.setEnabled(true);
                            }
                        });
                    }
                }, 3000);*/
            }
        }
    }

    @OnClick(R.id.btn_like)
    public void like() {
        btn_likes.setActivated(!btn_likes.isActivated());
        if (btn_likes.isActivated()) {
            OfferPresenter.likeOffer(offer, Session.getUser());
            offer.setLikes(offer.getLikes() + 1);
            offer.setLike(true);
        } else {
            offer.setLike(false);
            OfferPresenter.dislikeOffer(offer);
            offer.setLikes(offer.getLikes() - 1);
        }
    }

    @OnClick(R.id.offer_header)
    public void goToStore() {
        EventBus.getDefault().post(new StoreDetailsEvent(offer.getStore()));
    }


    private void startShareIntentImage(Uri imageUri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, title);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //shareIntent.setPackage("com.facebook.katana");
        getActivity().startActivity(Intent.createChooser(shareIntent, getActivity().getString(R.string.label_share)));
    }

    private void startShareIntentVideo(String imageUri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, imageUri);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getActivity().startActivity(Intent.createChooser(shareIntent, getActivity().getString(R.string.label_share)));
    }

    private void share(final Offer offer) {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Uri imageUri = presenter.getBitmapUri(bitmap, offer.getTitle(), getActivity());
                if (imageUri != null) {
                    String title = "A-ha!, am gasit ceva ce ti-ar placea: " + offer.getTitle() + " "
                            + offer.getStore().getName() + ". Descarca aplicatia Aha!:https://play.google.com/store/apps/details?id=aroundwise.nepi";
                    startShareIntentImage(imageUri, title);
                }
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
        else if (offer.getImages() != null && offer.getImages().size() > 0)
            MyApplication.instance.getPicasso().load(offer.getImages().get(0).getImage()).into(target);
    }

    private boolean askForWritePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_WRITE);
            return false;
        }
        return true;
    }

}
