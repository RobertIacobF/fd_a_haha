package aroundwise.nepi.network.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adonis on 3/16/2017.
 */
public class TokenRequest {

    @SerializedName("phone_os")
    String phoneOs;

    @SerializedName("notification_token")
    String token;

    public TokenRequest(String token) {
        this.token = token;
        this.phoneOs = "android";
    }
}
