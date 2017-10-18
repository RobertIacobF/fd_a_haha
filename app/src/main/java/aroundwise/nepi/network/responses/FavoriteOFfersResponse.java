package aroundwise.nepi.network.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import aroundwise.nepi.network.model.FavoriteOffer;

/**
 * Created by mihai on 12/09/16.
 */
@ParcelablePlease
public class FavoriteOffersResponse extends BasicResponse implements Parcelable {

    @SerializedName("objects")
    List<FavoriteOffer> offers;
    @SerializedName("meta")
    ResponseMeta meta;


    public List<FavoriteOffer> getOffers() {

        return offers;
    }

    public void setOffers(List<FavoriteOffer> offers) {
        this.offers = offers;
    }

    public ResponseMeta getMeta() {
        return meta;
    }

    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        FavoriteOffersResponseParcelablePlease.writeToParcel(this, dest, flags);
    }


    public static final Creator<FavoriteOffersResponse> CREATOR = new Creator<FavoriteOffersResponse>() {
        public FavoriteOffersResponse createFromParcel(Parcel source) {
            FavoriteOffersResponse target = new FavoriteOffersResponse();
            FavoriteOffersResponseParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public FavoriteOffersResponse[] newArray(int size) {
            return new FavoriteOffersResponse[size];
        }
    };
}
