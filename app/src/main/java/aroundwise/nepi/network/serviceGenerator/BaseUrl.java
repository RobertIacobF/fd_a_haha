package aroundwise.nepi.network.serviceGenerator;

import aroundwise.nepi.util.Constants;

/**
 * Created by mihai on 05/09/16.
 */
public class BaseUrl {
    private static String BASE_URL = Constants.PROMENADA_URL;

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }
}
