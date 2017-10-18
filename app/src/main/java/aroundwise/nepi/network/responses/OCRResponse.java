package aroundwise.nepi.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irina on 12/15/2016.
 */
public class OCRResponse extends BasicResponse {

    @SerializedName("points_received")
    private int pointsReceived;

    public int getPointsReceived() {
        return pointsReceived;
    }

    @Override
    public String toString() {
        return "OCRResponse{" +
                "pointsReceived=" + pointsReceived +
                "} " + super.toString();
    }
}
