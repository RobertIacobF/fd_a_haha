package aroundwise.nepi.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import aroundwise.nepi.MyApplication;

/**
 * Created by Adonis on 3/17/2017.
 */
public class SensorOrientationChangeNotifier {

    public final String TAG = getClass().getSimpleName();
    private ArrayList<WeakReference<SensorOrientationChangeNotifier.Listener>> mListeners = new ArrayList<WeakReference<SensorOrientationChangeNotifier.Listener>>(3);

    private int mOrientation = 0;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;

    private static SensorOrientationChangeNotifier mInstance;

    public static SensorOrientationChangeNotifier getInstance() {
        if (mInstance == null)
            mInstance = new SensorOrientationChangeNotifier();

        return mInstance;
    }

    private SensorOrientationChangeNotifier() {
        mSensorEventListener = new NotifierSensorEventListener();
        Context applicationContext = MyApplication.instance.getApplicationContext();
        mSensorManager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);

    }

    /**
     * Call on activity reset()
     */
    private void onResume() {
        mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Call on activity onPause()
     */
    private void onPause() {
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    private class NotifierSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            int newOrientation = mOrientation;
            if (x < 5 && x > -5 && y > 5)
                newOrientation = 0;
            else if (x < -5 && y < 5 && y > -5)
                newOrientation = 90;
            else if (x < 5 && x > -5 && y < -5)
                newOrientation = 180;
            else if (x > 5 && y < 5 && y > -5)
                newOrientation = 270;

            //Log.e(TAG,"mOrientation="+mOrientation+"   ["+event.values[0]+","+event.values[1]+","+event.values[2]+"]");
            if (mOrientation != newOrientation) {
                mOrientation = newOrientation;
                notifyListeners();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    public int getOrientation() {
        return mOrientation;
    }

    public interface Listener {
        void onOrientationChange(int orientation);
    }

    public void addListener(SensorOrientationChangeNotifier.Listener listener) {
        if (get(listener) == null) // prevent duplications
            mListeners.add(new WeakReference<SensorOrientationChangeNotifier.Listener>(listener));

        if (mListeners.size() == 1) {
            onResume(); // this is the first client
        }
    }

    public void remove(SensorOrientationChangeNotifier.Listener listener) {
        WeakReference<SensorOrientationChangeNotifier.Listener> listenerWR = get(listener);
        remove(listenerWR);
    }

    private void remove(WeakReference<SensorOrientationChangeNotifier.Listener> listenerWR) {
        if (listenerWR != null)
            mListeners.remove(listenerWR);

        if (mListeners.size() == 0) {
            onPause();
        }

    }

    private WeakReference<SensorOrientationChangeNotifier.Listener> get(SensorOrientationChangeNotifier.Listener listener) {
        for (WeakReference<SensorOrientationChangeNotifier.Listener> existingListener : mListeners)
            if (existingListener.get() == listener)
                return existingListener;
        return null;
    }

    private void notifyListeners() {
        ArrayList<WeakReference<SensorOrientationChangeNotifier.Listener>> deadLiksArr = new ArrayList<WeakReference<Listener>>();
        for (WeakReference<SensorOrientationChangeNotifier.Listener> wr : mListeners) {
            if (wr.get() == null)
                deadLiksArr.add(wr);
            else
                wr.get().onOrientationChange(mOrientation);
        }

        // remove dead references
        for (WeakReference<Listener> wr : deadLiksArr) {
            mListeners.remove(wr);
        }
    }

    public boolean isPortrait() {
        return mOrientation == 0 || mOrientation == 180;
    }

    public boolean isLandscape() {
        return !isPortrait();
    }

    public int getOrientationMode() {
        switch (mOrientation) {
            case 0:
                return 0;
            case 180:
                return 2;
            case 90:
                return 1;
            case 270:
                return 3;
            default:
                return 0;
        }
    }
}