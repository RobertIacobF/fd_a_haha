package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihai on 20/09/16.
 */
@ParcelablePlease
public class Offer implements Parcelable {
    @SerializedName("age_target")
    String ageTarget;
    @SerializedName("end_date")
    String endDate;
    @SerializedName("gender_target")
    String genderTarget;
    @SerializedName("id")
    long id;
    @SerializedName("fav_count")
    long likes;
    @SerializedName("images")
    List<aroundwise.nepi.network.model.Image> images;
    @SerializedName("long_desc")
    String longDesc;
    @SerializedName("offer_type")
    List<OfferType> offerType;
    @SerializedName("price")
    double price;
    @SerializedName("short_desc")
    String shortDesc;
    @SerializedName("store")
    Shop store;
    @SerializedName("title")
    String title;
    @SerializedName("resource_uri")
    String resourceUri;
    @SerializedName("video_url")
    String videoUrl;
    @SerializedName("poll")
    Poll poll;
    @SerializedName("discount")
    String discount;
    @SerializedName("date_time_text")
    String date;
    @SerializedName("price_text")
    String priceText;


    public boolean hideSlider = false;
    boolean like;


    public static final Creator<Offer> CREATOR = new Creator() {
        public Offer createFromParcel(Parcel source) {
            Offer target = new Offer();
            OfferParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    public Offer() {
    }

    public String getDate() {
        return date;
    }

    public String getDiscount() {
        return discount;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getLikes() {
        return this.likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public String getAgeTarget() {
        return this.ageTarget;
    }

    public void setAgeTarget(String ageTarget) {
        this.ageTarget = ageTarget;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getGenderTarget() {
        return this.genderTarget;
    }

    public void setGenderTarget(String genderTarget) {
        this.genderTarget = genderTarget;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return this.images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getLongDesc() {
        return this.longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public List<OfferType> getOfferType() {
        return this.offerType;
    }

    public void setOfferType(List<OfferType> offerType) {
        this.offerType = offerType;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public Shop getStore() {
        return this.store;
    }

    public void setStore(Shop store) {
        this.store = store;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        OfferParcelablePlease.writeToParcel(this, dest, flags);
    }

    public String getResourceUri() {
        return this.resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public boolean isLike() {
        return this.like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getPriceText() {
        return priceText;
    }

    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    public Offer(com.aroundwise.aroundwisesdk.model.http.offer.Offer offer) {
        this.ageTarget = offer.getAgeTarget();
        this.endDate = offer.getEndDate();
        this.genderTarget = offer.getGenderTarget();
        this.id = offer.getId();
        this.likes = offer.getLikes();
        imagesFromAroundwise(offer.getImages());
        this.longDesc = offer.getLongDesc();
        this.shortDesc = offer.getShortDesc();
        offerTypesFromAroundwise(offer.getOfferType());
        this.price = offer.getPrice();
        storeFromAroundwise(offer.getStore());
        this.title = offer.getTitle();
        this.resourceUri = offer.getResourceUri();
        this.videoUrl = offer.getVideoUrl();
    }

    private void storeFromAroundwise(com.aroundwise.aroundwisesdk.model.http.shop.Shop store) {
        this.store = new Shop(store);
    }

    private void offerTypesFromAroundwise(List<com.aroundwise.aroundwisesdk.model.http.offer.OfferType> offerType) {
        this.offerType = new ArrayList<>();
        if (offerType != null)
            for (int i = 0; i < offerType.size(); i++)
                this.offerType.add(new OfferType(offerType.get(i)));
    }


    public void imagesFromAroundwise(List<com.aroundwise.aroundwisesdk.model.http.Image> likesFromAroundwise) {
        this.images = new ArrayList<>();
        if (likesFromAroundwise != null)
            for (int i = 0; i < likesFromAroundwise.size(); i++)
                images.add(new Image(likesFromAroundwise.get(i)));
    }
}