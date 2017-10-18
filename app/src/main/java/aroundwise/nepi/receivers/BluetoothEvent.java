package aroundwise.nepi.receivers;

/**
 * Created by mihai on 16/09/16.
 */
public class BluetoothEvent {
    BluetoothState state;

    public BluetoothEvent(BluetoothState state) {
        this.state = state;
    }

    public BluetoothState getState() {
        return state;
    }
}
