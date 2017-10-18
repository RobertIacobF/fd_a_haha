package aroundwise.nepi.network.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irina on 12/20/2016.
 */
public class FacebookLoginRequest {

    private String token;
    @SerializedName("user_id")
    private String userId;
    private String os;

    public FacebookLoginRequest(String token, String userId) {
        this.token = token;
        this.userId = userId;
        os = "android";
    }
}


