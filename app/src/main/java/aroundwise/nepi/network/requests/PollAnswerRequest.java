package aroundwise.nepi.network.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irina on 12/8/2016.
 */
public class PollAnswerRequest {
    @SerializedName("answer_id")
    int answerId;

    public PollAnswerRequest(int answerId) {
        this.answerId = answerId;
    }
}
