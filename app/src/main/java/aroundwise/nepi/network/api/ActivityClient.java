package aroundwise.nepi.network.api;


import com.aroundwise.aroundwisesdk.model.http.beacon.GetPointsRequest;
import com.aroundwise.aroundwisesdk.model.http.beacon.GetPointsResult;

import aroundwise.nepi.network.responses.ActivityResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mihai on 23/09/16.
 */
public interface ActivityClient {

    @GET("/api/v1/activity/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ActivityResponse> getUserActivity(
            @Header("Cookie") String sessionId);

    @GET("/api/v1/activity/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ActivityResponse> getAllActivities();

    @GET("/api/v1/activity?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ActivityResponse> getUserActivity(
            @Query("offset") int offset, @Query("limit") int limit, @Header("Cookie") String sessionId);

    @POST("/api/v1/activity/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<GetPointsResult> getPoints(@Body GetPointsRequest request,
                          @Header("Cookie") String sessionId);

}
