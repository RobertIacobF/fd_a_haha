package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.LinkedList;

/**
 * Created by mihai on 13/09/16.
 */
@ParcelablePlease
public class UserReward implements Parcelable {

    @SerializedName("claim_date")
    String claimDate;
    @SerializedName("coupon_url")
    String coupon;
    @SerializedName("id")
    long id;
    @SerializedName("code")
    String code;
    @SerializedName("receive_date")
    String receiveDate;
    @SerializedName("resource_uri")
    String resourceUri;
    @SerializedName("status")
    String status;
    @SerializedName("reward")
    Reward reward;


    int httpCode;

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        UserRewardParcelablePlease.writeToParcel(this, dest, flags);
    }


    public static final Creator<UserReward> CREATOR = new Creator<UserReward>() {
        public UserReward createFromParcel(Parcel source) {
            UserReward target = new UserReward();
            UserRewardParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public UserReward[] newArray(int size) {
            return new UserReward[size];
        }
    };

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Reward getReward() {
        if (reward == null) {
            reward = new Reward();
            reward.id = 0;
            reward.title = "";
            reward.description = "";
            reward.resource_uri = "";
            reward.statusReward = "";
            Shop store = new Shop();
            store.address = "";
            store.hours = "";
            store.id = 0L;
            Image img = new Image();
            img.setImage("");
            img.setIsBanner(false);
            store.images = new LinkedList<>();
            store.images.add(img);
            reward.store = store;
            reward.pointCost = 0;
            reward.rewardImages = new LinkedList<>();
            reward.rewardImages.add(img);
        }

        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }
}
