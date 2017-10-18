package aroundwise.nepi.network.responses;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import aroundwise.nepi.network.model.CoverOfferGroup;
import aroundwise.nepi.network.model.SimpleOfferGroup;

/**
 * Created by irina on 12/22/2016.
 */
public class SimpleOfferGroupResponse {

    @SerializedName("meta")
    ResponseMeta meta;

    @SerializedName("objects")
    List<SimpleOfferGroup> coverOfferGroups;

    public List<SimpleOfferGroup> getCoverOfferGroups() {
        return coverOfferGroups;
    }

    public ResponseMeta getMeta() {
        return meta;
    }
}
