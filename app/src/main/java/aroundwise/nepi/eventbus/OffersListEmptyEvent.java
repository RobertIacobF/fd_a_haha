package aroundwise.nepi.eventbus;

/**
 * Created by mihai on 28/09/16.
 */
public class OffersListEmptyEvent {
    String message;

    public OffersListEmptyEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
