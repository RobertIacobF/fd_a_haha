package aroundwise.nepi.network.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.LinkedList;
import java.util.List;

import aroundwise.nepi.network.model.UserReward;

/**
 * Created by mihai on 13/09/16.
 */
@ParcelablePlease
public class UserRewardResponse implements Parcelable {

    @SerializedName("objects")
    List<UserReward> rewards;
    @SerializedName("meta")
    ResponseMeta meta;


    public List<UserReward> getRewards()
    {
        List<UserReward> correct=new LinkedList<>();
        for (UserReward reward : rewards)
        {
            if (reward.getReward()!=null)
            {
                correct.add(reward);
            }
        }

        return correct;
    }

    public void setRewards(List<UserReward> rewards) {
        this.rewards = rewards;
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
        UserRewardResponseParcelablePlease.writeToParcel(this, dest, flags);
    }


    public static final Creator<UserRewardResponse> CREATOR = new Creator<UserRewardResponse>() {
        public UserRewardResponse createFromParcel(Parcel source) {
            UserRewardResponse target = new UserRewardResponse();
            UserRewardResponseParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public UserRewardResponse[] newArray(int size) {
            return new UserRewardResponse[size];
        }
    };
}
