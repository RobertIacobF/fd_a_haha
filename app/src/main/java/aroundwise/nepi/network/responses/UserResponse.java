package aroundwise.nepi.network.responses;

import com.google.gson.annotations.SerializedName;

import aroundwise.nepi.network.model.Profile;

/**
 * Created by mihai on 07/09/16.
 */
public class UserResponse {

    String username;
    String email;
    String first_name;
    int id;
    String last_name;
    Profile profile;
    String resource_uri;
    @SerializedName("phone_number")
    String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getResource_uri() {
        return resource_uri;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public int getId() {
        return id;
    }

    public String getLast_name() {
        return last_name;
    }

    public Profile getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", id=" + id +
                ", last_name='" + last_name + '\'' +
                ", profile=" + profile.toString() +
                '}';
    }
}
