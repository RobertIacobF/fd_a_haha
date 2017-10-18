package aroundwise.nepi.network.responses;


import com.google.gson.annotations.SerializedName;

public class LikeOfferResponse {

    private long status;
    @SerializedName("favourite_id")
    private long favouriteId;

    public long getStatus() {
        return status;
    }

    public long getFavouriteId() {
        return favouriteId;
    }
}
