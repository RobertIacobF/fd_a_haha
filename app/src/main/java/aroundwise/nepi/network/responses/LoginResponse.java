package aroundwise.nepi.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mihai on 05/09/16.
 */
public class LoginResponse extends BasicResponse {


    @SerializedName("user_profile_id")
    private long userProfileId;
    @SerializedName("user_id")
    private long userId;
    @SerializedName("session_id")
    private String sessionId;

    public LoginResponse(int status, long userProfileId, long userId, String sessionId, String message) {
        this.setStatus(status);
        this.setMessage(message);
        this.userProfileId = userProfileId;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public LoginResponse() {
    }


    public long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "userProfileId=" + userProfileId +
                ", userId=" + userId +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
