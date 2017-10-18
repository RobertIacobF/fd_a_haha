package aroundwise.nepi.network.api;

import aroundwise.nepi.network.requests.FacebookLoginRequest;
import aroundwise.nepi.network.requests.LoginRequest;
import aroundwise.nepi.network.requests.UploadImageRequest;
import aroundwise.nepi.network.responses.BasicResponse;
import aroundwise.nepi.network.responses.LoginResponse;
import aroundwise.nepi.network.responses.UploadResponse;
import aroundwise.nepi.network.responses.UserResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mihai on 05/09/16.
 */
public interface LoginClient {
    @POST("api/v1/user/login/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<LoginResponse> login(@Body LoginRequest registerRequest);

    @GET("api/v1/user/{id}/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<UserResponse> userDetails(@Path("id") long id, @Header("cookie") String sessionid);


    @POST("api/v1/userprofile/{id}/change-image/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<UploadResponse> uploadImage(@Header("cookie") String sessionid,
                                                  @Path("id") long id, @Body UploadImageRequest request);


    @POST("api/v1/user/fblogin/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<LoginResponse> facebookLogin(@Body FacebookLoginRequest request);

}
