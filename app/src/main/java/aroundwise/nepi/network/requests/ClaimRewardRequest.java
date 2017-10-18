package aroundwise.nepi.network.requests;

/**
 * Created by mihai on 15/09/16.
 */
public class ClaimRewardRequest {
    String reward;
    String user;

    public ClaimRewardRequest(String reward, String user) {
        this.reward = reward;
        this.user = user;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
