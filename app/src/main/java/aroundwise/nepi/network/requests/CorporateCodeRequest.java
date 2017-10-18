package aroundwise.nepi.network.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irina on 2/3/2017.
 */
public class CorporateCodeRequest {

    @SerializedName("corporate_code")
    String corporateCode;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("gender")
    String gender;

    @SerializedName("birthdate")
    String birthDate;

    @SerializedName("phone_number")
    String phoneNumber;

    public CorporateCodeRequest(String corporateCode) {
        this.corporateCode = corporateCode;
    }


    public CorporateCodeRequest(String corporateCode, String firstName, String lastName, String gender, String birthDate, String phoneNumber) {
        this.corporateCode = corporateCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
