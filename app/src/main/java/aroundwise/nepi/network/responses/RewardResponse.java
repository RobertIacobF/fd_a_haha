package aroundwise.nepi.network.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.ArrayList;

import aroundwise.nepi.network.model.Reward;

/**
 * Created by mihai on 06/09/16.
 */
@ParcelablePlease
public class RewardResponse implements Parcelable {
    @SerializedName("meta")
    ResponseMeta meta;
    @SerializedName("objects")
    public ArrayList<Reward> reward;

    public RewardResponse() {
    }

    public ResponseMeta getMeta() {
        return meta;
    }

    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }


    public ArrayList<Reward> getRewards() {
        return reward;
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.reward = rewards;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        RewardResponseParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<RewardResponse> CREATOR = new Creator<RewardResponse>() {
        public RewardResponse createFromParcel(Parcel source) {
            RewardResponse target = new RewardResponse();
            RewardResponseParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public RewardResponse[] newArray(int size) {
            return new RewardResponse[size];
        }
    };
}
