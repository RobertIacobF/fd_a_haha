package aroundwise.nepi.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mihai on 05/09/16.
 */
public class BasicResponse{

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public BasicResponse(int status) {
        this.status = status;
    }

    public BasicResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BasicResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
