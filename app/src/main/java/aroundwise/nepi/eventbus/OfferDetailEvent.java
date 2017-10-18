package aroundwise.nepi.eventbus;


import aroundwise.nepi.network.model.Offer;

/**
 * Created by mihai on 13/09/16.
 */
public class OfferDetailEvent {
    private Offer offer;

    public OfferDetailEvent(Offer offer) {
        this.offer = offer;
    }

    public Offer getOffer() {
        return offer;
    }
}
