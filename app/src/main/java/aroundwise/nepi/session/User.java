package aroundwise.nepi.session;

import com.google.gson.annotations.SerializedName;

import aroundwise.nepi.network.model.Profile;
import aroundwise.nepi.network.responses.UserResponse;
import aroundwise.nepi.util.Constants;

/**
 * Created by mihai on 07/09/16.
 */
public class User {
    @SerializedName("user_id")
    long id;
    @SerializedName("user_profile_id")
    long userProfileId;
    @SerializedName("session_id")
    String sessionId;
    @SerializedName("phone")
    String phone;
    @SerializedName("resource_uri")
    String resourceUri;
    @SerializedName("profile")
    Profile profile;

    String first_name;
    String last_name;

    public User() {
        first_name = Constants.NEW_USER;
    }

    public long getUserProfileId() {
        return this.userProfileId;
    }

    public void setUserProfileId(long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getResourceUri() {
        return this.resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userProfileId=" + userProfileId +
                ", sessionId='" + sessionId + '\'' +
                ", phone='" + phone + '\'' +
                ", resourceUri='" + resourceUri + '\'' +
                ", profile=" + profile.toString() +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }

    public void setUserResponse(UserResponse userResponse) {
        this.setPhone(userResponse.getUsername());
        this.setProfile(userResponse.getProfile());
        this.setLast_name(userResponse.getLast_name());
        this.setFirst_name(userResponse.getFirst_name());
        this.setResourceUri(userResponse.getResource_uri());
        this.setUserProfileId(userResponse.getProfile().getId());
    }
}