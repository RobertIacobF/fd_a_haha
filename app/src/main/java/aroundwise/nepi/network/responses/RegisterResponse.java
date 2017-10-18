package aroundwise.nepi.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mihai on 05/09/16.
 */
public class RegisterResponse {

    @SerializedName("status")
    private int status;
    @SerializedName("user_id")
    private long userId;

    @SerializedName("message")
    private String message;

    @SerializedName("session_id")
    private String sessionId;

    public RegisterResponse(int status, long userId, String message) {
        this.status = status;
        this.userId = userId;
        this.message = message;
    }

    public RegisterResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "status=" + status +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}