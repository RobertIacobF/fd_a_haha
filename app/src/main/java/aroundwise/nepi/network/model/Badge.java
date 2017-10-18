package aroundwise.nepi.network.model;

/**
 * Created by irina on 12/6/2016.
 */
public class Badge {

    private String title;
    private int image;

    public Badge(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
