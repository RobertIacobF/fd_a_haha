package aroundwise.nepi.network.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mihai on 05/09/16.
 */
public class RegisterRequest {
    String phone;
    boolean is_manager;
    String password;
    String first_name;
    String last_name;
    String email;
    int mall_id;
    @SerializedName("deviceToken")
    public String accessToken;
    @SerializedName("phone_no")
    public String phoneNo;

    public RegisterRequest(String phone, boolean is_manager, String password, String first_name, String last_name, String email, int mall_id, String tokem) {
        this.phone = phone;
        this.is_manager = is_manager;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.mall_id = mall_id;
        this.accessToken = tokem;
    }

    public RegisterRequest(String phone, boolean is_manager, String password, String first_name, String last_name, String email, int mall_id, String tokem, String phoneNo) {
        this.phone = phone;
        this.is_manager = is_manager;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.mall_id = mall_id;
        this.accessToken = tokem;
        this.phoneNo = phoneNo;
    }
}
