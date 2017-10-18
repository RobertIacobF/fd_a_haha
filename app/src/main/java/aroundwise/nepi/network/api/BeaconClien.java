package aroundwise.nepi.network.api;

import com.aroundwise.aroundwisesdk.model.http.beacon.GetBeaconIdResult;

import aroundwise.nepi.network.responses.BasicResponse;
import aroundwise.nepi.network.responses.BeaconScannerResponse;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by irina on 1/6/2017.
 */
public interface BeaconClien {

    @GET("/api/v1/beacon/?format=json")
    public Observable<BeaconScannerResponse> sendBeaconInfo(@Query("user_id") long userId,
                                                            @Query("session_id") String sessionId,
                                                            @Query("uuid") String uuid,
                                                            @Query("major") long major,
                                                            @Query("minor") long minor);
}
