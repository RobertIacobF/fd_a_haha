package aroundwise.nepi.network.api;

import aroundwise.nepi.network.requests.LikeOfferRequest;
import aroundwise.nepi.network.requests.PollAnswerRequest;
import aroundwise.nepi.network.responses.BasicResponse;
import aroundwise.nepi.network.responses.CoverOfferGroupResponse;
import aroundwise.nepi.network.responses.FavoriteOffersResponse;
import aroundwise.nepi.network.responses.LikeOfferResponse;
import aroundwise.nepi.network.responses.OfferResponse;
import aroundwise.nepi.network.responses.OfferTypeResponse;
import aroundwise.nepi.network.responses.SimpleOfferGroupResponse;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by mihai on 08/09/16.
 */
public interface OfferClient {
    @GET("/api/v1/offer/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<OfferResponse> getOffers(@Query("store__mall") int mallId, @Header("Cookie") String sessionId);


    @GET("/api/v1/offer/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<OfferResponse> getOffers(@Query("offset") int offset, @Query("limit") int limit,
                                               @Query("store__mall") int mallId, @Header("Cookie") String sessionId);

    @GET("/api/v1/offer/?format=json")
    public Observable<OfferResponse> getFeaturedOffers(@Query("is_featured") boolean isFeatured,
                                                       @Header("Cookie") String sessionId);

    @GET("/api/v1/offertype/")
    @Headers("Cache-Control: no-cache")
    public Observable<OfferTypeResponse> getOfferType();

    @POST("/api/v1/favorite/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<LikeOfferResponse> likeOffer(@Body LikeOfferRequest request, @Header("Cookie") String sessionId);

    @DELETE("/api/v1/favorite/{id}/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<ResponseBody> deleteLikedOffer(
            @Path("id") long identifier,
            @Header("Cookie") String sessionId);

    @GET("/api/v1/favorite/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<FavoriteOffersResponse> getUserFavoriteOffers(@Header("Cookie") String sessionId);

    @GET("/api/v1/favorite/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<FavoriteOffersResponse> getUserFavoriteOffers(@Header("Cookie") String sessionId,
                                                                    @Query("offer__store__mall") int mallId);

    @GET("/api/v1/offer/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<OfferResponse> getStoreOffer(@Query("store") long storeID,
                                                   @Header("Cookie") String sessionId);

    @GET("/api/v1/offer/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<OfferResponse> getOffersByType(@Query("offer_type__name") String offerType,
                                                     @Query("store__mall") int mallId,
                                                     @Header("Cookie") String sessionId);


    @GET("/api/v1/offer/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<OfferResponse> getOffersByType(@Query("offer_type__name") String offerType,
                                                     @Query("store__mall") int mallId,
                                                     @Query("offset") int offset, @Query("limit") int limit,
                                                     @Header("Cookie") String sessionId);

    @GET("/api/v1/offer/?format=json")
    @Headers("Cache-Control: no-cache")
    public Observable<OfferResponse> getOffersByType(@Query("offer_type__name") String offerType,
                                                     @Query("offset") int offset, @Query("limit") int limit);

    @POST
    @Headers("Cache-Control: no-cache")
    public Observable<BasicResponse> submitPollAnswer(@Body PollAnswerRequest pollAnswerRequest,
                                                      @Header("Cookie") String sesssionId,
                                                      @Url String url);

    @GET("/api/v1/coveroffergroup/?limit=10")
    public Observable<CoverOfferGroupResponse> getCoverOffersList(@Header("Cookie") String sessionId,
                                                                  @Query("mall") int mallId);

    @GET("/api/v1/offer/?limit=10")
    public Observable<OfferResponse> getCoverOffers(@Query("cover_group") int id,
                                                    @Header("cookie") String sessionId,
                                                    @Query("store__mall") int mallId);

    @GET("/api/v1/simpleoffergroup/?limit=10")
    public Observable<SimpleOfferGroupResponse> getSimpleOfferGroup(@Header("Cookie") String sessionId);

    @GET("/api/v1/offer/?limit=10")
    public Observable<OfferResponse> getSimpleGroup(@Query("simple_group") int id,
                                                    @Header("Cookie") String sessionId,
                                                    @Query("store__mall") int mallId);

}
