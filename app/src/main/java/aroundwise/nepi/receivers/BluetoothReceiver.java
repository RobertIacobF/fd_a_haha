package aroundwise.nepi.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 16/09/16.
 */
public class BluetoothReceiver extends BroadcastReceiver {

    BluetoothCallback bluetoothCallback;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    if (bluetoothCallback != null)
                        bluetoothCallback.bluetoothIsOff();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    if (bluetoothCallback != null)
                        bluetoothCallback.bluetoothIsOff();
                    EventBus.getDefault().post(new BluetoothEvent(BluetoothState.TURNING_OFF));
                    break;
                case BluetoothAdapter.STATE_ON:
                    EventBus.getDefault().post(new BluetoothEvent(BluetoothState.ON));
                    if (bluetoothCallback != null)
                        bluetoothCallback.bluetoothIsOn();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    if (bluetoothCallback != null)
                        bluetoothCallback.turningOn();
                    break;
            }

        }
    }

    public void setBluetoothCallback(BluetoothCallback bluetoothCallback) {
        this.bluetoothCallback = bluetoothCallback;
    }
}
