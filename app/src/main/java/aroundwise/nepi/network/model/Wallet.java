package aroundwise.nepi.network.model;

import com.google.gson.annotations.SerializedName;


public class Wallet {

    @SerializedName("points_total")
    private int totalPoints;

    public int getTotalPoints() {
        return totalPoints;
    }
}
