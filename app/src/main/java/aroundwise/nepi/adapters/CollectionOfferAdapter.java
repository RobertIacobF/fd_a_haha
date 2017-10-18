package aroundwise.nepi.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import java.util.TimerTask;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferPresenter;
import aroundwise.nepi.eventbus.StoreMapEvent;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by irina on 12/21/2016.
 */
public class CollectionOfferAdapter extends SupportAnnotatedAdapter implements CollectionOfferAdapterBinder {


    @ViewType(
            layout = R.layout.item_collection_offer,
            views =
                    {
                            @ViewField(id = R.id.iv_offer_logo, type = ImageView.class, name = "ivOfferLogo"),
                            @ViewField(id = R.id.tv_offer_owner, type = TextView.class, name = "tvOfferOwner"),
                            @ViewField(id = R.id.iv_offer_mall_logo, type = ImageView.class, name = "ivOfferMallLogo"),
                            @ViewField(id = R.id.iv_offer_photo, type = ImageView.class, name = "ivOfferPhoto"),
                            @ViewField(id = R.id.rl_videoview, type = RelativeLayout.class, name = "rlVideoView"),
                            @ViewField(id = R.id.videoview, type = VideoView.class, name = "videoView"),
                            @ViewField(id = R.id.iv_play, type = ImageView.class, name = "ivPlay"),
                            @ViewField(id = R.id.tv_offer_title, type = TextView.class, name = "tvOfferTitle"),
                            @ViewField(id = R.id.tv_offer_description, type = TextView.class, name = "tvOfferDescription"),
                            @ViewField(id = R.id.iv_heart, type = ImageView.class, name = "ivHeart"),
                            @ViewField(id = R.id.iv_like_photo, type = CircleImageView.class, name = "ivLikePhoto"),
                            @ViewField(id = R.id.tv_like_text, type = TextView.class, name = "tvLikeText"),
                            @ViewField(id = R.id.btn_location, type = Button.class, name = "btnLocation"),
                            @ViewField(id = R.id.btn_share, type = Button.class, name = "btnShare"),
                            @ViewField(id = R.id.offer_likes, type = RelativeLayout.class, name = "rlOfferLikes"),
                            @ViewField(id = R.id.badge_1, type = View.class, name = "badge1"),
                            @ViewField(id = R.id.badge_2, type = View.class, name = "badge2"),
                            @ViewField(id = R.id.tv_points, type = TextView.class, name = "tvPrice")
                    }
    )
    public static final int item = 0;


    private List<Offer> collectionOffers;
    private Context context;
    private Target target;

    public CollectionOfferAdapter(Context context, List<Offer> collectionOffers) {
        super(context);
        this.context = context;
        this.collectionOffers = collectionOffers;
    }

    @Override
    public int getItemCount() {
        return collectionOffers != null ? collectionOffers.size() : 0;
    }

    @Override
    public void bindViewHolder(final CollectionOfferAdapterHolders.ItemViewHolder vh, final int position) {
        final Offer offer = collectionOffers.get(position);
        MyApplication.instance.getPicasso().load(offer.getStore().getLogo()).into(vh.ivOfferLogo);
        MyApplication.instance.getPicasso().load(offer.getStore().mall.getLogo()).into(vh.ivOfferMallLogo);
        if (offer.getImages() != null && offer.getImages().size() > 0)
            MyApplication.instance.getPicasso().load(offer.getImages().get(0).getImage()).fit().centerCrop().into(vh.ivOfferPhoto);
        vh.ivHeart.setActivated(offer.isLike());
        vh.tvLikeText.setText(String.valueOf(offer.getLikes()));
        vh.tvOfferDescription.setText(offer.getLongDesc());
        vh.tvOfferOwner.setText(offer.getStore().getName());
        vh.tvOfferTitle.setText(offer.getTitle());
        vh.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askForWritePermission()) {
                    vh.btnShare.setEnabled(false);
                    /*Timer buttonTimer = new Timer();
                    buttonTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    vh.btnShare.setEnabled(true);
                                }
                            });
                        }
                    }, 3000);*/
                    share(offer);
                }
            }
        });
        vh.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(offer.getStore().getLatitude() != null && offer.getStore().getLongitude() != null)
                    EventBus.getDefault().post(new StoreMapEvent(offer.getStore(), null));
                else
                    Toast.makeText(context, "Acest magazin nu are setată o locație. Magazinul îl puteți găsi în mall-ul " + Session.getUser().getProfile().getMall().getName(),Toast.LENGTH_SHORT).show();
            }
        });

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
                vh.badge1.setVisibility(View.INVISIBLE);
                vh.badge2.setVisibility(View.INVISIBLE);
            }

        }

        if (offer.getPriceText() != null && !offer.getPriceText().equals("null")) {
            vh.tvPrice.setText(offer.getPriceText());
        }
        vh.ivHeart.setActivated(offer.isLike());
        vh.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long offerLikes = offer.getLikes();
                if (vh.ivHeart.isActivated()) {
                    OfferPresenter.dislikeOffer(offer);
                    offerLikes--;
                    vh.ivHeart.setActivated(false);
                    offer.setLike(vh.ivHeart.isActivated());
                    offer.setLikes(offerLikes);
                    notifyDataSetChanged();

                } else {
                    offerLikes++;
                    OfferPresenter.likeOffer(offer, Session.getUser());
                    vh.ivHeart.setActivated(true);
                    offer.setLike(vh.ivHeart.isActivated());
                    offer.setLikes(offerLikes);
                    notifyDataSetChanged();
                }
            }
        });

    }

    private boolean askForWritePermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_WRITE);
            return false;
        }
        return true;
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

    private void startShareIntentImage(Uri imageUri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, title);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //shareIntent.setPackage("com.facebook.katana");
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

    private Uri getBitmapUri(Bitmap bitmap, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, title, null);
        return Uri.parse(path);
    }

}
