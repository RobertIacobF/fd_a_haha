package aroundwise.nepi.network.requests;

import com.google.gson.annotations.SerializedName;


public class RegisterCorporateCode {

    @SerializedName("corporate_code")
    String corporateCode;

    @SerializedName("is_promo")
    int isPromo;
    public RegisterCorporateCode(String corporateCode) {
        this.corporateCode = corporateCode;
    }

    public RegisterCorporateCode(String corporateCode, int isPromo) {
        this.corporateCode = corporateCode;
        this.isPromo = isPromo;
    }
}
