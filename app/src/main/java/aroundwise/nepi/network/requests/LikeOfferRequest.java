package aroundwise.nepi.network.requests;

/**
 * Created by mihai on 12/09/16.
 */
public class LikeOfferRequest {

    public String offer;
    public String user;
    public String date;

    public LikeOfferRequest(String offer, String user, String created) {
        this.offer = offer;
        this.user = user;
        this.date = created;
    }
}

