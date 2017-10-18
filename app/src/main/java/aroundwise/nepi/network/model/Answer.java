package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by irina on 12/7/2016.
 */
@ParcelablePlease
public class Answer implements Parcelable{

    @SerializedName("answer")
    String answer;

    @SerializedName("id")
    String id;

    @SerializedName("poll")
    String poll;

    @SerializedName("resource_uri")
    String resourceUri;

    @SerializedName("user_answered")
    boolean userAnswered;

    protected Answer(Parcel in) {
        answer = in.readString();
        id = in.readString();
        poll = in.readString();
        resourceUri = in.readString();
        userAnswered = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(answer);
        dest.writeString(id);
        dest.writeString(poll);
        dest.writeString(resourceUri);
        dest.writeByte((byte) (userAnswered ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public String getAnswer() {
        return answer;
    }

    public String getId() {
        return id;
    }

    public String getPoll() {
        return poll;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public boolean isUserAnswered() {
        return userAnswered;
    }
}
