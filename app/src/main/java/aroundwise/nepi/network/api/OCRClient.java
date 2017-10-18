package aroundwise.nepi.network.api;

import aroundwise.nepi.network.requests.OCRRequest;
import aroundwise.nepi.network.responses.OCRResponse;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by irina on 12/15/2016.
 */
public interface OCRClient {

    @POST("/api/v1/userprofile/{id}/upload-receipt/")
    Observable<OCRResponse> scanReceiptCall(@Path("id") long userProfileId,
                                            @Body OCRRequest ocrRequest,
                                            @Header("Cookie") String sessionId);

}
