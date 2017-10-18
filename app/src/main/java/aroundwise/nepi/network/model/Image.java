package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by mihai on 20/09/16.
 */
@ParcelablePlease
public class Image implements Parcelable {
    @SerializedName("image")
    String image;
    @SerializedName("is_banner")
    boolean isBanner;
    public static final Creator<Image> CREATOR = new Creator() {
        public Image createFromParcel(Parcel source) {
            Image target = new Image();
            ImageParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public Image() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ImageParcelablePlease.writeToParcel(this, dest, flags);
    }

    public boolean isBanner() {
        return this.isBanner;
    }

    public void setIsBanner(boolean isBanner) {
        this.isBanner = isBanner;
    }

    public static Creator<Image> getCREATOR() {
        return CREATOR;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Image(com.aroundwise.aroundwisesdk.model.http.Image image) {
        this.image = image.getImage();
        this.isBanner = image.isBanner();
    }
}