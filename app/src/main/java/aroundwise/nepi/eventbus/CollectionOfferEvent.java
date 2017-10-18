package aroundwise.nepi.eventbus;

/**
 * Created by irina on 1/4/2017.
 */
public class CollectionOfferEvent {

    int id;
    String title;
    boolean simple;

    public CollectionOfferEvent(int id, String title, boolean simple) {
        this.id = id;
        this.title = title;
        this.simple = simple;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public boolean isSimple() {
        return simple;
    }
}
