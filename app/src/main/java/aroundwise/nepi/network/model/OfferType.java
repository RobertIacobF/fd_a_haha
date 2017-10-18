package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by mihai on 20/09/16.
 */
@ParcelablePlease
public class OfferType implements Parcelable {
    @SerializedName("id")
    long id;
    @SerializedName("name")
    String name;
    @SerializedName("resource_uri")
    String resourceUri;
    public static final Creator<OfferType> CREATOR = new Creator() {
        public OfferType createFromParcel(Parcel source) {
            OfferType target = new OfferType();
            OfferTypeParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public OfferType[] newArray(int size) {
            return new OfferType[size];
        }
    };

    public OfferType() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        OfferTypeParcelablePlease.writeToParcel(this, dest, flags);
    }

    public String getResourceUri() {
        return this.resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OfferType(com.aroundwise.aroundwisesdk.model.http.offer.OfferType type) {
        this.id = type.getId();
        this.name = type.getName();
        this.resourceUri = type.getResourceUri();
    }
}