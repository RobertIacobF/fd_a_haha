package aroundwise.nepi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Adonis on 5/26/2017.
 */
public final class NetworkUtils {

    private NetworkUtils() {
        throw new IllegalAccessError("This class should not be instantiated since only purpose is to check internet connection");
    }

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
