package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihai on 20/09/16.
 */
@ParcelablePlease
public class Shop implements Parcelable, ClusterItem {
    @SerializedName("address")
    public String address;
    @SerializedName("description")
    public String description;
    @SerializedName("hours")
    public String hours;
    @SerializedName("id")
    public Long id;
    @SerializedName("images")
    public List<Image> images;
    @SerializedName("locations")
    public List<Location> lcoations;
    @SerializedName("latitude")
    public Double latitude;
    @SerializedName("longitude")
    public Double longitude;
    @SerializedName("logo")
    public String logo;
    @SerializedName("name")
    public String name;
    @SerializedName("phone")
    public String phone;
    @SerializedName("profile_photo")
    public String profilePhoto;
    @SerializedName("resource_uri")
    public String resourceUri;
    @SerializedName("walkin_points")
    public Integer walkin_points;
    @SerializedName("points_per_buy")
    public Integer pointsPerBuy;
    @SerializedName("offers")
    public List<Offer> offers;
    @SerializedName("tags")
    public List<Tag> tags;
    @SerializedName("mall")
    public Mall mall;
    @SerializedName("store_url")
    public String storeUrl;
    public boolean isPictureSet;
    public static final Creator<Shop> CREATOR = new Creator() {
        public Shop createFromParcel(Parcel source) {
            Shop target = new Shop();
            ShopParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public Shop() {
    }

    public Shop(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ShopParcelablePlease.writeToParcel(this, dest, flags);
    }

    public List<Location> getLcoations() {
        return this.lcoations;
    }

    public void setLcoations(List<Location> lcoations) {
        this.lcoations = lcoations;
    }

    //TODO change how it was before
    /*public LatLng getPosition() {
        return new LatLng(this.latitude.doubleValue(), this.longitude.doubleValue());
    }*/

    @Override
    public LatLng getPosition() {
        if (this.getLcoations().size() != 0)
            return new LatLng(this.getLcoations().get(0).getLatitude(), this.getLcoations().get(0).getLongitude());
        else
            return new LatLng(0, 0);
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHours() {
        return this.hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return this.images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePhoto() {
        return this.profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getResourceUri() {
        return this.resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public Integer getWalkin_points() {
        return this.walkin_points;
    }

    public void setWalkin_points(Integer walkin_points) {
        this.walkin_points = walkin_points;
    }

    public List<Offer> getOffers() {
        return this.offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public Integer getPointsPerBuy() {
        return this.pointsPerBuy;
    }

    public void setPointsPerBuy(Integer pointsPerBuy) {
        this.pointsPerBuy = pointsPerBuy;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Shop(com.aroundwise.aroundwisesdk.model.http.shop.Shop shop) {
        imagesFromAroundwise(shop.getImages());
        this.address = shop.getAddress();
        this.description = shop.getDescription();
        this.hours = shop.hours;
        this.id = shop.id;
        locationFromAroundWise(shop.getLcoations());
        this.latitude = shop.latitude;
        this.longitude = shop.longitude;
        this.logo = shop.logo;
        this.name = shop.name;
        this.phone = shop.phone;
        this.profilePhoto = shop.profilePhoto;
        this.resourceUri = shop.resourceUri;
        this.walkin_points = shop.walkin_points;
        this.pointsPerBuy = shop.pointsPerBuy;
        tagsFromAroundwise(shop.tags);
        offersFromAroundWise(shop.offers);
    }

    private void offersFromAroundWise(List<com.aroundwise.aroundwisesdk.model.http.offer.Offer> offers) {
        this.offers = new ArrayList<>();
        if (offers != null)
            for (int i = 0; i < offers.size(); i++)
                this.offers.add(new Offer(offers.get(i)));
    }

    private void tagsFromAroundwise(List<com.aroundwise.aroundwisesdk.model.http.shop.Tag> tags) {
        this.tags = new ArrayList<>();
        if (tags != null)
            for (int i = 0; i < tags.size(); i++)
                this.tags.add(new Tag(tags.get(i)));
    }

    private void locationFromAroundWise(List<com.aroundwise.aroundwisesdk.model.http.shop.Location> lcoations) {
        this.lcoations = new ArrayList<>();
        if (lcoations != null)
            for (int i = 0; i < lcoations.size(); i++)
                this.lcoations.add(new Location(lcoations.get(i)));
    }

    public void imagesFromAroundwise(List<com.aroundwise.aroundwisesdk.model.http.Image> likesFromAroundwise) {
        this.images = new ArrayList<>();
        if (likesFromAroundwise != null)
            for (int i = 0; i < likesFromAroundwise.size(); i++)
                images.add(new Image(likesFromAroundwise.get(i)));
    }


}