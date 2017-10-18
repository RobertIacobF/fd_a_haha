package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by mihai on 12/09/16.
 */
@ParcelablePlease
public class FavoriteOffer implements Parcelable {

    @SerializedName("offer")
    Offer offer;
    @SerializedName("created")
    String createdAt;
    @SerializedName("id")
    long id;
    @SerializedName("resource_uri")
    String resourceUri;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        FavoriteOfferParcelablePlease.writeToParcel(this, dest, flags);
    }


    public static final Creator<FavoriteOffer> CREATOR = new Creator<FavoriteOffer>() {
        public FavoriteOffer createFromParcel(Parcel source) {
            FavoriteOffer target = new FavoriteOffer();
            FavoriteOfferParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public FavoriteOffer[] newArray(int size) {
            return new FavoriteOffer[size];
        }
    };

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
