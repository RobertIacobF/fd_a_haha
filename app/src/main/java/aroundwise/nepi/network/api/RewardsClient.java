package aroundwise.nepi.network.api;

import aroundwise.nepi.network.model.UserReward;
import aroundwise.nepi.network.requests.ClaimRewardRequest;
import aroundwise.nepi.network.requests.DeleteUserReward;
import aroundwise.nepi.network.requests.SetCurrentRewardRequest;
import aroundwise.nepi.network.responses.RewardResponse;
import aroundwise.nepi.network.responses.SetCurrentRewardResponse;
import aroundwise.nepi.network.responses.UserRewardResponse;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mihai on 06/09/16.
 */
public interface RewardsClient {

    @GET("api/v1/reward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<RewardResponse> getRewards(@Header("Cookie") String sessionId, @Query("offset") int offset, @Query("limit") int limit);

    @GET("api/v1/reward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<RewardResponse> getRewards(@Header("Cookie") String sessionId,
                                                 @Query("offset") int offset,
                                                 @Query("limit") int limit,
                                                 @Query("store__mall") int mallId);

    @GET("api/v1/reward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<RewardResponse> getRewards(@Header("Cookie") String sessionId,
                                                 @Query("offset") int offset,
                                                 @Query("limit") int limit,
                                                 @Query("store__mall") int mallId,
                                                 @Query("point_cost__gt") int pointCostGreaterThan);

    @GET("api/v1/reward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<RewardResponse> getRewardsWithoutPoints(@Header("Cookie") String sessionId,
                                                 @Query("offset") int offset,
                                                 @Query("limit") int limit,
                                                 @Query("store__mall") int mallId,
                                                 @Query("point_cost") int pointCostGreaterThan);

    @GET("/api/v1/reward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<RewardResponse> getRewards();

    @GET("/api/v1/reward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<RewardResponse> getRewards(@Query("offset") int offset, @Query("limit") int limit);

    @GET("/api/v1/reward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<RewardResponse> getRewards(@Query("offset") int offset, @Query("limit") int limit, @Query("point_cost__gt") int pointsValueGreaterThan);

    @GET("/api/v1/reward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<RewardResponse> getRewardsWithNoPoints(@Query("offset") int offset, @Query("limit") int limit, @Query("point_cost") int pointsValueGreaterThan);

    @GET("/api/v1/reward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<RewardResponse> getStoreRewards(@Query("store") long storeUri);

    @GET("/api/v1/userreward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<UserRewardResponse> getUserRewards(@Header("Cookie") String sessionId,
                                                         @Query("reward__store__mall") int mallId);

    @PUT("/api/v1/userprofile/{id}/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<SetCurrentRewardResponse> setCurrentReward(
            @Header("Cookie") String sessionId,
            @Path("id") long identifier,
            @Body SetCurrentRewardRequest request);


    @POST("/api/v1/userreward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<UserReward> claimReward(@Body ClaimRewardRequest request,
                                              @Header("Cookie") String sessionId);

    @PUT("/api/v1/userreward/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ResponseBody> removeUserReward(@Body DeleteUserReward request,
                                                     @Header("Cookie") String sessionId);


}
