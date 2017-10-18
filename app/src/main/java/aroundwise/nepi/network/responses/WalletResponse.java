package aroundwise.nepi.network.responses;

import com.aroundwise.aroundwisesdk.model.http.base.ResponseMeta;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import aroundwise.nepi.network.model.Wallet;

/**
 * Created by irina on 1/4/2017.
 */

public class WalletResponse {
    @SerializedName("meta")
    public ResponseMeta meta;

    @SerializedName("objects")
    public List<Wallet> wallets;


}
