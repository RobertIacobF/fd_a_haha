package aroundwise.nepi.network.responses;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import aroundwise.nepi.network.model.Mall;

/**
 * Created by mihai on 07/09/16.
 */
public class MallResponse {

    ResponseMeta meta;
    @SerializedName("objects")
    List<Mall> malls;

    public ResponseMeta getMeta() {
        return meta;
    }

    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }

    public List<Mall> getMalls() {
        return malls;
    }

    public void setMalls(List<Mall> malls) {
        this.malls = malls;
    }

    @Override
    public String toString() {
        return "MallResponse{" +
                "meta=" + meta.toString() +
                ", malls=" + malls +
                '}';
    }
}
