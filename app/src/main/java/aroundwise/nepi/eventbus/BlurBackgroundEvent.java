package aroundwise.nepi.eventbus;

/**
 * Created by irina on 1/10/2017.
 */
public class BlurBackgroundEvent {
    private boolean blur;

    public BlurBackgroundEvent(boolean blur) {
        this.blur = blur;
    }

    public boolean isBlur() {
        return blur;
    }
}
