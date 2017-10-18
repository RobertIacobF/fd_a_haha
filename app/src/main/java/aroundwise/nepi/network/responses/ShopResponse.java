package aroundwise.nepi.network.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import aroundwise.nepi.network.model.Shop;


/**
 * Created by mihai on 12/09/16.
 */
@ParcelablePlease
public class ShopResponse implements Parcelable {
    @SerializedName("meta")
    public ResponseMeta meta;
    @SerializedName("objects")
    public List<Shop> shops;
    public static final Creator<ShopResponse> CREATOR = new Creator() {
        public ShopResponse createFromParcel(Parcel source) {
            ShopResponse target = new ShopResponse();
            ShopResponseParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public ShopResponse[] newArray(int size) {
            return new ShopResponse[size];
        }
    };

    public ShopResponse() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ShopResponseParcelablePlease.writeToParcel(this, dest, flags);
    }

    public ResponseMeta getMeta() {
        return this.meta;
    }

    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }

    public List<Shop> getShops() {
        return this.shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}