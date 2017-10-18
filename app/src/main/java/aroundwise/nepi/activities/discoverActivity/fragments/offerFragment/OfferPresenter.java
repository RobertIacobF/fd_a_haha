package aroundwise.nepi.activities.discoverActivity.fragments.offerFragment;


import android.util.Log;

import java.util.List;

import aroundwise.nepi.eventbus.LogOutEvent;
import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.eventbus.WalletChangeEvent;
import aroundwise.nepi.network.api.OfferClient;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.requests.LikeOfferRequest;
import aroundwise.nepi.network.requests.PollAnswerRequest;
import aroundwise.nepi.network.responses.BasicResponse;
import aroundwise.nepi.network.responses.FavoriteOffersResponse;
import aroundwise.nepi.network.responses.LikeOfferResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.session.User;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.DateUtil;
import de.greenrobot.event.EventBus;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mihai on 12/09/16.
 */
public final class OfferPresenter {

    private OfferPresenter() {
        throw new IllegalAccessError("You should not create a class member of OfferPresenter since all methods are static!");
    }

    public static void likeOffer(final Offer offer, User user) {
        if (!Session.getFavoriteOfferIds().containsKey(offer.getId())) {
            Session.getFavoriteOfferIds().put(offer.getId(), null);
            OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
            offerClient.likeOffer(new LikeOfferRequest(offer.getResourceUri(), user.getResourceUri(), DateUtil.getCurrentDateAsString()),
                    Constants.SESSIONID + Session.getUser().getSessionId())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<LikeOfferResponse>() {
                        @Override
                        public void onCompleted() {
                            //OfferPresenter.getUserFavoriteOffers();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof HttpException) {
                                if (((HttpException) e).code() == 401) {
                                    EventBus.getDefault().post(new LogOutEvent());
                                }
                            }
                        }

                        @Override
                        public void onNext(LikeOfferResponse likeOfferResponse) {
                            if (likeOfferResponse.getStatus() == 200)
                                Session.getFavoriteOfferIds().put(
                                        offer.getId(),
                                        likeOfferResponse.getFavouriteId());
                        }
                    });
        } else
            Log.e(OfferPresenter.class.getSimpleName(), "Item is already liked");
    }

    public static void dislikeOffer(final Offer offer) {
        Long favId = Session.getFavoriteOfferIds().get(offer.getId());
        if (favId != null) {
            OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
            offerClient.deleteLikedOffer(favId, Constants.SESSIONID + Session.getUser().getSessionId())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof HttpException) {
                                if (((HttpException) e).code() == 401) {
                                    EventBus.getDefault().post(new LogOutEvent());
                                }
                            }
                        }

                        @Override
                        public void onNext(ResponseBody deleteFavoriteOfferResponse) {
                            Session.getFavoriteOfferIds().remove(offer.getId());
                        }
                    });
        } else
            Log.e(OfferPresenter.class.getSimpleName(), "Offer is not favorite");
    }

    public static void getUserFavoriteOffers() {
        OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        offerClient.getUserFavoriteOffers(Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<FavoriteOffersResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(FavoriteOffersResponse favoriteOffersResponse) {
                        for (int i = 0; i < favoriteOffersResponse.getOffers().size(); i++)
                            Session.getFavoriteOfferIds().put(
                                    favoriteOffersResponse.getOffers().get(i).getOffer().getId(),
                                    favoriteOffersResponse.getOffers().get(i).getId());
                    }
                });
    }

    public static void setLikedOffers(List<Offer> offers) {
        if (Session.getFavoriteOfferIds() != null && offers != null)
            for (int i = 0; i < offers.size(); i++) {
                if (offers.get(i) != null)
                    if (Session.getFavoriteOfferIds().containsKey(offers.get(i).getId()))
                        offers.get(i).setLike(true);
            }
    }

    public static void submitPollAnswer(String pollUrl, int answerId, final int points) {
        OfferClient offerClient = ServiceGenerator.getServiceGenerator().createService(OfferClient.class);
        offerClient.submitPollAnswer(new PollAnswerRequest(answerId),
                Constants.SESSIONID + Session.getUser().getSessionId(), pollUrl + Constants.POLL_RESPONSE_END_URL)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new WalletChangeEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                    }

                    @Override
                    public void onNext(BasicResponse basicResponse) {
                        Log.i(OfferPresenter.class.getSimpleName(), basicResponse.toString());
                        if (basicResponse.getStatus() == 200) {
                            Session.getUser().getProfile().addPoints(points);
                        }
                    }
                });
    }

}
