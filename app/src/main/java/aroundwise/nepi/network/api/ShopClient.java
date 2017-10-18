package aroundwise.nepi.network.api;


import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.network.responses.ShopResponse;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mihai on 08/09/16.
 */
public interface ShopClient {

    @GET("/api/v1/store/search/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ShopResponse> searchStores(@Query("q") String query,
                                                 @Query("page") int page,
                                                 @Header("Cookie") String sessionId);

    @GET("/api/v1/store/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ShopResponse> getShops(@Query("mall") int mallId,
                                             @Query("offset") int offset,
                                             @Query("limit") int limit,
                                             @Header("Cookie") String sessionId);

    @GET("/api/v1/store/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ShopResponse> getShops(@Query("mall") int mallId,
                                             @Header("Cookie") String sessionId);

    @GET("/api/v1/store/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ShopResponse> getShopsOnlyWithWalkin(@Query("walkin_points__gt") long value,
                                                           @Query("mall") int mallId,
                                                           @Query("Cookie") String sessionId);

    @GET("/api/v1/store/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ShopResponse> getShopsOnlyWithWalkin(@Query("walkin_points__gt") long value,
                                                           @Query("offset") int offset,
                                                           @Query("limit") int limit,
                                                           @Query("mall") int mallId,
                                                           @Header("Cookie") String sessionId);

    @GET("/api/v1/store/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ShopResponse> getShopsOnlyWithPurchase(@Query("points_per_buy__gt") long value);

    @GET("/api/v1/store/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ShopResponse> getShopsOnlyWithPurchase(@Query("points_per_buy__gt") long value,
                                                             @Query("offset") int offset,
                                                             @Query("limit") int limit);

    @GET("/api/v1/store/maplocations?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ShopResponse> getShopsForMap();

    @GET("/api/v1/store/{id}/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<Shop> getShopDetails(@Path("id") long identifier,
                                           @Header("Cookie") String sessionId);

    @GET("/api/v1/store/search/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ShopResponse> searchShops(@Query("mall") int mallId,
                                                @Query("q") String searchQuery,
                                                @Header("Cookie") String sessionId);


}
