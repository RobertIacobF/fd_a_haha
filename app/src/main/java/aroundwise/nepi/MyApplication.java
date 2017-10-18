package aroundwise.nepi;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.aroundwise.aroundwisesdk.AroundWiseSDK;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;
import java.util.HashMap;

import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.beacon.Callbacks;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mihai on 26/08/16.
 */
public class MyApplication extends Application {

    private static ComplexPreferences sharedPreferences;
    public static MyApplication instance;
    AroundWiseSDK aroundWiseSDK;
    private Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        sharedPreferences = ComplexPreferences.getComplexPreferences(getBaseContext(), Constants.PREFFERENCES, MODE_PRIVATE);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ProximaNova-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        Callbacks c = new Callbacks();
        instance = this;
        HashMap<String, String> clientData = new HashMap<>();
        clientData.put("key_placeholder", "value_placeholder");
        picasso = Picasso.with(getApplicationContext());
        aroundWiseSDK = new AroundWiseSDK(getBaseContext(), this, c, false, "https://promenada.aroundwise.com", clientData);
        aroundWiseSDK.setMainActivity(DiscoverActivity.class);
        aroundWiseSDK.bindBackgroundService();

    }

    public Picasso getPicasso() {
        return picasso;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static ComplexPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = ComplexPreferences.getComplexPreferences(context, Constants.PREFFERENCES, MODE_PRIVATE);
        }
        return sharedPreferences;
    }
}
