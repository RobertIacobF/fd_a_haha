package aroundwise.nepi.network.responses;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import aroundwise.nepi.network.model.CoverOfferGroup;

/**
 * Created by irina on 12/21/2016.
 */
public class CoverOfferGroupResponse extends BasicResponse {

    @SerializedName("meta")
    ResponseMeta meta;

    @SerializedName("objects")
    List<CoverOfferGroup> coverOfferGroups;

    public List<CoverOfferGroup> getCoverOfferGroups() {
        return coverOfferGroups;
    }

    public ResponseMeta getMeta() {
        return meta;
    }
}
