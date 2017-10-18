package aroundwise.nepi.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import aroundwise.nepi.R;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;

/**
 * Created by mihai on 19/09/16.
 */
public class GCMRegistationIntentService extends IntentService {

    //Constants for success and errors
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    public GCMRegistationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM() {
        Intent registrationComplete = null;
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.w("GCMRegIntentService", "token:" + token);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(), Constants.PREFFERENCES, MODE_PRIVATE);
            String oldToken = complexPreferences.getObject("token", String.class);
            if (oldToken == null || oldToken.equals(token)) {
                complexPreferences.putObject(Constants.TOKEN_SENT, false);
            }
            complexPreferences.putObject("token", token);
            complexPreferences.save();
        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        //Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
