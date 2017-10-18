package aroundwise.nepi.network.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mihai on 05/09/16.
 */
public class LoginRequest {
    public String phone;

    public String pwd;

    @SerializedName("deviceToken")
    public String accessToken;

    public String os;

    public LoginRequest(String phone, String pwd) {
        this.phone = phone;
        this.pwd = pwd;
    }

    public LoginRequest(String accessToken, String phone, String pwd) {
        this.accessToken = accessToken;
        this.phone = phone;
        this.pwd = pwd;
        this.os = "android";
    }
}
