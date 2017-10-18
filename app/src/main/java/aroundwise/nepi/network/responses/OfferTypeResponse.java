package aroundwise.nepi.network.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import aroundwise.nepi.network.model.OfferType;

/**
 * Created by mihai on 20/09/16.
 */
@ParcelablePlease
public class OfferTypeResponse implements Parcelable {
    @SerializedName("meta")
    ResponseMeta meta;
    @SerializedName("objects")
    List<OfferType> offerType;
    public static final Creator<OfferTypeResponse> CREATOR = new Creator() {
        public OfferTypeResponse createFromParcel(Parcel source) {
            OfferTypeResponse target = new OfferTypeResponse();
            OfferTypeResponseParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public OfferTypeResponse[] newArray(int size) {
            return new OfferTypeResponse[size];
        }
    };

    public OfferTypeResponse() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        OfferTypeResponseParcelablePlease.writeToParcel(this, dest, flags);
    }

    public ResponseMeta getMeta() {
        return this.meta;
    }

    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }

    public List<OfferType> getOfferType() {
        return this.offerType;
    }

    public void setOfferType(List<OfferType> offerType) {
        this.offerType = offerType;
    }
}
