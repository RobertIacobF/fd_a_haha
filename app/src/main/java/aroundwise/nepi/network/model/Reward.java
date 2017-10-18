package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

/**
 * Created by mihai on 06/09/16.
 */
@ParcelablePlease
public class Reward implements Parcelable {
    @SerializedName("id")
    public long id;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("images")
    public List<Image> rewardImages;

    @SerializedName("resource_uri")
    public String resource_uri;
    @SerializedName("status")
    public String statusReward;

    @SerializedName("point_cost")
    public Integer pointCost;
    @SerializedName("store")
    public Shop store;

    @SerializedName("stock_remaining")
    public int stockRemaining;

    @SerializedName("stock_is_infinite")
    public boolean stockIsInfinite;

    @SerializedName("coupon_url")
    public String couponUrl;

    @SerializedName("code")
    public String code;

    @SerializedName("is_premium")
    public boolean isPremium;

    public boolean isSelected;

    public Reward(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Reward() {
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        RewardParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<Reward> CREATOR = new Creator<Reward>() {
        public Reward createFromParcel(Parcel source) {
            Reward target = new Reward();
            RewardParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Reward[] newArray(int size) {
            return new Reward[size];
        }
    };
}
