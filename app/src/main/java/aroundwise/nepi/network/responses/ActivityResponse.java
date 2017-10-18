package aroundwise.nepi.network.responses;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import aroundwise.nepi.network.model.UserActivity;

/**
 * Created by mihai on 23/09/16.
 */


public class ActivityResponse extends BasicResponse{

    @SerializedName("objects")
    List<UserActivity> userActivities;
    @SerializedName("meta")
    ResponseMeta meta;

    public List<UserActivity> getUserActivities() {
        return userActivities;
    }

    public void setUserActivities(List<UserActivity> userActivities) {
        this.userActivities = userActivities;
    }

    public ResponseMeta getMeta() {
        return meta;
    }

    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }


}
