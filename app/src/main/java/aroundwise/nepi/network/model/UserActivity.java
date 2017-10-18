package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by mihai on 23/09/16.
 */
@ParcelablePlease
public class UserActivity implements Parcelable {


    @SerializedName("store")
    public Shop shops;
    @SerializedName("created")
    String createdAt;
    @SerializedName("id")
    long id;
    @SerializedName("points_claimed")
    long pointsClaimed;
    @SerializedName("resource_uri")
    String resourceUri;
    @SerializedName("activity_type")
    String activityType;
    @SerializedName("activity_message")
    String activityMessage;
    @SerializedName("custom_notification_message")
    String reasonMessage;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        UserActivityParcelablePlease.writeToParcel(this, dest, flags);
    }


    public static final Parcelable.Creator<UserActivity> CREATOR = new Parcelable.Creator<UserActivity>() {
        public UserActivity createFromParcel(Parcel source) {
            UserActivity target = new UserActivity();
            UserActivityParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public UserActivity[] newArray(int size) {
            return new UserActivity[size];
        }
    };

    public String getReasonMessage() {
        return reasonMessage;
    }

    public Shop getShops() {
        return shops;
    }

    public void setShops(Shop shops) {
        this.shops = shops;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPointsClaimed() {
        return pointsClaimed;
    }

    public void setPointsClaimed(long pointsClaimed) {
        this.pointsClaimed = pointsClaimed;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityMessage() {
        return activityMessage;
    }
}
