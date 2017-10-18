package aroundwise.nepi.network.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import aroundwise.nepi.network.model.Offer;

/**
 * Created by mihai on 12/09/16.
 */
@ParcelablePlease
public class OfferResponse implements Parcelable {
    @SerializedName("objects")
    List<Offer> offers;
    @SerializedName("meta")
    ResponseMeta meta;
    public static final Creator<OfferResponse> CREATOR = new Creator() {
        public OfferResponse createFromParcel(Parcel source) {
            OfferResponse target = new OfferResponse();
            OfferResponseParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public OfferResponse[] newArray(int size) {
            return new OfferResponse[size];
        }
    };

    public OfferResponse() {
    }

    public ResponseMeta getMeta() {
        return this.meta;
    }

    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }

    public List<Offer> getOffers() {
        return this.offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        OfferResponseParcelablePlease.writeToParcel(this, dest, flags);
    }
}