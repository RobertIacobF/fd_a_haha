package aroundwise.nepi.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by irina on 1/13/2017.
 */
public class BeaconResponse {

    @SerializedName("store_info")
    Shop stores;

    @SerializedName("points_received")
    int pointsReceived;

    public Shop getStores() {
        return stores;
    }

    public int getPointsReceived() {
        return pointsReceived;
    }
}
