package aroundwise.nepi.session;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.network.model.Mall;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;
import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 07/09/16.
 */
public class Session {
    private static User user;
    private static List<Mall> malls;
    private static HashMap<Long, Long> favoriteOfferIds;
    private static HashMap<Long, Long> userRewards;
    private static Reward onboardReward;

    public static User getUser() {
        if (user == null)
            user = new User();
        return user;
    }

    public static Reward getOnboardReward() {
        return onboardReward;
    }

    public static void setOnboardReward(Reward onboardReward) {
        Session.onboardReward = onboardReward;
    }

    public static void setUser(User user) {
        Session.user = user;
    }

    public static void saveUserToSharedPref(Context context) {
        MyApplication.getSharedPreferences(context).putObject(Constants.USER, user);
    }

    public static User readUserFromSharedPref(Context context) {
        ComplexPreferences complexPreferences = MyApplication.getSharedPreferences(context);
        setUser(complexPreferences.getObject(Constants.USER, User.class));
        return user;
    }

    public static List<Mall> getMalls() {
        if (malls == null)
            malls = new ArrayList<>();
        return malls;
    }

    public static void setUserMallFirst() {
        if (Session.getUser().getProfile() != null &&
                Session.getUser().getProfile().getMall() != null) {
            int poz = 0;
            for (int i = 0; i < malls.size(); i++) {
                if (malls.get(i).getName().equals(Session.getUser().getProfile().getMall().getName()))
                    poz = i;
            }
            if (poz != 0)
                switchMalls(poz);
        }
    }

    public static void setMalls(List<Mall> malls) {
        Session.malls = malls;
    }

    public static void switchMalls(int position) {
        Mall mall = malls.get(0);
        Mall mallSwitched = malls.get(position);
        malls.remove(position);
        malls.remove(0);
        malls.add(0, mallSwitched);
        malls.add(position, mall);
        Session.getUser().getProfile().setMall(mallSwitched);
    }

    public static HashMap<Long, Long> getFavoriteOfferIds() {
        if (favoriteOfferIds == null)
            favoriteOfferIds = new HashMap<>();
        return favoriteOfferIds;
    }

    public static void setFavoriteOfferIds(HashMap<Long, Long> favoriteOfferIds) {
        Session.favoriteOfferIds = favoriteOfferIds;
    }

    public static HashMap<Long, Long> getUserRewards() {
        if (userRewards == null)
            userRewards = new HashMap<>();
        return userRewards;
    }

    public static void logOut(Context context) {
        userRewards = null;
        favoriteOfferIds = null;
        malls = null;
        user = new User();
        user.setId(-1);
        ComplexPreferences complexPreferences = MyApplication.getSharedPreferences(context);
        complexPreferences.putObject("token", "");
        complexPreferences.putObject(Constants.TOKEN_SENT, false);
        complexPreferences.save();
        saveUserToSharedPref(context);

    }

}
