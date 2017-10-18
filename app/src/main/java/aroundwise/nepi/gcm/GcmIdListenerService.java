package aroundwise.nepi.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by mihai on 19/09/16.
 */
public class GcmIdListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, GCMRegistationIntentService.class);
        startService(intent);

        //send token to app server
    }
}
