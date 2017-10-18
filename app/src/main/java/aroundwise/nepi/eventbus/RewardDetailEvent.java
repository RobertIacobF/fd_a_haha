package aroundwise.nepi.eventbus;

import aroundwise.nepi.network.model.Reward;

/**
 * Created by mihai on 13/09/16.
 */
public class RewardDetailEvent {

    private Reward reward;

    public RewardDetailEvent(Reward reward) {
        this.reward = reward;
    }

    public Reward getReward() {
        return reward;
    }
}
