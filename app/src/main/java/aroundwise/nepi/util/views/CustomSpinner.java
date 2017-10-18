package aroundwise.nepi.util.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by irina on 1/10/2017.
 */
public class CustomSpinner extends Spinner {

    private OnSpinnerEventsListener mListener;
    private boolean mOpenInitiated = false;


    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
    }

    public void setSpinnerEventsListener(OnSpinnerEventsListener onSpinnerEventsListener) {
        mListener = onSpinnerEventsListener;
    }

    public void performClosedEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerClosed();
        }
    }

    @Override
    public boolean performClick() {
        mOpenInitiated = true;
        if (mListener != null) {
            mListener.onSpinnerOpened();
        }
        return super.performClick();
    }

    public boolean hasBeenOpened() {
        return mOpenInitiated;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasBeenOpened() && hasWindowFocus) {
            performClosedEvent();
        }
    }

    public interface OnSpinnerEventsListener {

        void onSpinnerOpened();

        void onSpinnerClosed();

    }
}
