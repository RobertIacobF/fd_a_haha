package aroundwise.nepi.beacon;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aroundwise.aroundwisecards.dialogbeacons.BluetoothInfoActivity;
import com.aroundwise.aroundwisecards.dialogbeacons.ContextualInfoActivity;
import com.aroundwise.aroundwisesdk.AroundwiseCallbacks;
import com.aroundwise.aroundwisesdk.model.http.beacon.ContextualBeacon;
import com.aroundwise.aroundwisesdk.model.http.beacon.GetBeaconResult;
import com.aroundwise.aroundwisesdk.utils.Logger;

import java.util.ArrayList;

/**
 * Created by mihai on 15/09/16.
 */
public class Callbacks implements AroundwiseCallbacks {

    @Override
    public void backgroundServiceCallback(GetBeaconResult beacon) {
        Log.e(Callbacks.class.getSimpleName(), "backgroundServiceCallback");
        if (beacon.getType().equals("Contextual")) {

        }
        if (beacon.getType().equals("walkin")) {

        }
    }

    @Override
    public void foregroundCallback(GetBeaconResult beacon, Context context) {
        if (beacon.getType().equals("contextual")) {
            ArrayList<ContextualBeacon> beaconList = new ArrayList<ContextualBeacon>();
            beaconList.add(new ContextualBeacon(beacon, System.currentTimeMillis()));

            Logger.d("callback contextual", beacon.getContextual().getTitle());
            Intent intent = new Intent(context, ContextualInfoActivity.class);
            intent.putParcelableArrayListExtra("beacon", beaconList);
            context.startActivity(intent);
        }
    }

    @Override
    public void enableBluetoothCallback(Context context) {
        Intent intent = new Intent(context, BluetoothInfoActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void showHaloCallback(Context context) {
    /*    if (context!=null){
            final ImageView b = (ImageView)((Activity)context).findViewById(R.id.showActiveNotificationsAnimation);
            if (b!=null) {
                b.post(new Runnable() {
                    public void run() {
                        Animation pulse = AnimationUtils.loadAnimation(b.getContext(), R.anim.pulse);
                        b.setVisibility(View.VISIBLE);
                        b.startAnimation(pulse);
                    }
                });
            }
        }*/
    }

    @Override
    public void hideHaloCallback(Context context) {
        /*if (context!=null){
            final ImageView b = (ImageView)((Activity)context).findViewById(R.id.showActiveNotificationsAnimation);
            if (b!=null) {
                b.post(new Runnable() {
                    public void run() {
                        b.setVisibility(View.GONE);
                        b.clearAnimation();
                    }
                });
            }
        }*/
    }
}