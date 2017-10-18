package aroundwise.nepi.eventbus;

/**
 * Created by irina on 12/14/2016.
 */
public class AnswerPollEvent {
    int points;
    String logo;

    public AnswerPollEvent(String logo, int points) {
        this.logo = logo;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String getLogo() {
        return logo;
    }
}
