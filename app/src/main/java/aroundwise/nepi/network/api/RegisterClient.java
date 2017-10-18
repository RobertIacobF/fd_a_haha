package aroundwise.nepi.network.api;

import aroundwise.nepi.network.requests.RegisterRequest;
import aroundwise.nepi.network.responses.MallResponse;
import aroundwise.nepi.network.responses.RegisterResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mihai on 05/09/16.
 */
public interface RegisterClient {

    @POST("api/v1/user/register/")
    @Headers("Cache-Control: no-cache")
    public Observable<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @GET("api/v1/mall/")
    @Headers("Cache-Control: no-cache")
    public Observable<MallResponse> getMalls(@Query("limit") int limit, @Query("offset") int offset);


}
