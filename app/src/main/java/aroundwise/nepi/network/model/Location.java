package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by mihai on 20/09/16.
 */
@ParcelablePlease
public class Location implements Parcelable {
    @SerializedName("address")
    String address;
    @SerializedName("id")
    long id;
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;
    @SerializedName("resource_uri")
    String resourceId;
    public static final Creator<Location> CREATOR = new Creator() {
        public Location createFromParcel(Parcel source) {
            Location target = new Location();
            LocationParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public Location() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        LocationParcelablePlease.writeToParcel(this, dest, flags);
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Location(com.aroundwise.aroundwisesdk.model.http.shop.Location location){
        this.address = location.getAddress();
        this.id = location.getId();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.resourceId = location.getResourceId();
    }
}