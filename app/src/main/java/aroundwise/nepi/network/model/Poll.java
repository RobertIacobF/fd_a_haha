package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

/**
 * Created by irina on 12/7/2016.
 */
@ParcelablePlease
public class  Poll implements Parcelable{

    @SerializedName("answers")
    List<Answer> answers;

    @SerializedName("points")
    int points;

    @SerializedName("question")
    String question;

    @SerializedName("resource_uri")
    String resourceUri;

    @SerializedName("user_answered")
    boolean userAnswered;

    protected Poll(Parcel in) {
        answers = in.createTypedArrayList(Answer.CREATOR);
        points = in.readInt();
        question = in.readString();
        resourceUri = in.readString();
        userAnswered = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(answers);
        dest.writeInt(points);
        dest.writeString(question);
        dest.writeString(resourceUri);
        dest.writeByte((byte) (userAnswered ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Poll> CREATOR = new Creator<Poll>() {
        @Override
        public Poll createFromParcel(Parcel in) {
            return new Poll(in);
        }

        @Override
        public Poll[] newArray(int size) {
            return new Poll[size];
        }
    };

    public List<Answer> getAnswers() {
        return answers;
    }

    public int getPoints() {
        return points;
    }

    public String getQuestion() {
        return question;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public boolean isUserAnswered() {
        return userAnswered;
    }
}
