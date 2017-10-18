package aroundwise.nepi.eventbus;

/**
 * Created by Adonis on 3/8/2017.
 */
public class Notificationevent {
    String storeLogo;
    String mallLogo;
    int points;

    public Notificationevent(String storeLogo, String mallLogo, int points) {
        this.storeLogo = storeLogo;
        this.mallLogo = mallLogo;
        this.points = points;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public String getMallLogo() {
        return mallLogo;
    }

    public int getPoints() {
        return points;
    }
}
