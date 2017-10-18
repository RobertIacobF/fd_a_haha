package aroundwise.nepi.network.api;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import aroundwise.nepi.network.requests.ChangePasswordRequest;
import aroundwise.nepi.network.requests.ChangeUserProfileRequest;
import aroundwise.nepi.network.requests.CorporateCodeRequest;
import aroundwise.nepi.network.requests.RegisterCorporateCode;
import aroundwise.nepi.network.requests.TokenRequest;
import aroundwise.nepi.network.requests.UpdateMallRequest;
import aroundwise.nepi.network.responses.BasicResponse;
import aroundwise.nepi.network.responses.ChangePasswordResponse;
import aroundwise.nepi.network.responses.UpdateProfileResponse;
import aroundwise.nepi.network.responses.WalletResponse;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mihai on 15/09/16.
 */
public interface UserClient {

    @PUT("/api/v1/userprofile/{id}/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ResponseBody> changeMall(
            @Header("Cookie") String sessionId,
            @Path("id") long identifier,
            @Body UpdateMallRequest request);

    @POST("/api/v1/user/set-password/?format=json ")
    @Headers("Cache-Control: no-cache")
    public Observable<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request,
                                                             @Header("Cookie") String sessionId);

    @PUT("/api/v1/user/{id}/?format=json ")
    @Headers("Cache-Control: no-cache")
    public Observable<UpdateProfileResponse> updateUserProfile(
            @Path("id") long identifier,
            @Body ChangeUserProfileRequest request,
            @Header("Cookie") String sessionId);

    @PUT("/api/v1/userprofile/{id}/?format=json ")
    @Headers("Cache-Control: no-cache")
    public Observable<UpdateProfileResponse> updateCoupon(
            @Path("id") long identifier,
            @Body CorporateCodeRequest corporateRequest,
            @Header("Cookie") String sessionId);

    @PUT("/api/v1/userprofile/{id}/?format=json ")
    @Headers("Cache-Control: no-cache")
    public Observable<UpdateProfileResponse> registerCoupon(
            @Path("id") long identifier,
            @Body RegisterCorporateCode corporateRequest,
            @Header("Cookie") String sessionId);

    @PUT("/api/v1/userprofile/{id}/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<UpdateProfileResponse> resendToken(
            @Path("id") long identifier,
            @Body TokenRequest tokenRequest,
            @Header("Cookie") String sessionId);


    @GET("/api/v1/wallet/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<WalletResponse> wallet(
            @Header("Cookie") String sessionId,
            @Query("mall") long identifier);
}
