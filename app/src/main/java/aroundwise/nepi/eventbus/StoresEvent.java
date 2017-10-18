package aroundwise.nepi.eventbus;

/**
 * Created by mihai on 30/09/16.
 */
public class StoresEvent {
    private boolean walkIn;

    public StoresEvent(boolean walkIn) {
        this.walkIn = walkIn;
    }

    public boolean isWalkIn() {
        return walkIn;
    }
}
