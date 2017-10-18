package aroundwise.nepi.network.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import aroundwise.nepi.network.model.BeaconResponse;
import aroundwise.nepi.network.model.Shop;

/**
 * Created by irina on 1/13/2017.
 */
public class BeaconScannerResponse extends BasicResponse{

    @SerializedName("objects")
    List<BeaconResponse> store;

    public List<BeaconResponse> getStore() {
        return store;
    }
}
