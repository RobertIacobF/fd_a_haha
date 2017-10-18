package aroundwise.nepi.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mihai on 22/09/16.
 */
public class UpdateProfileResponse extends BasicResponse {

    @SerializedName("corporate_code_validated")
    boolean codeValidated;

    @SerializedName("corporate_code")
    String code;

    public String getCode() {
        return code;
    }

    public boolean isCodeValidated() {
        return codeValidated;
    }
}
