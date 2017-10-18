package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by mihai on 06/09/16.
 */
@ParcelablePlease
public class UserProfile implements Parcelable {
    @SerializedName("birthdate")
    String birthDate;
    @SerializedName("current_reward")
    Reward currentReward;
    @SerializedName("gender")
    String gneder;
    @SerializedName("profile_id")
    long profileId;
    @SerializedName("id")
    long id;
    @SerializedName("phone")
    String phoneNumber;
    @SerializedName("image")
    String profileAvatar;
    @SerializedName("email")
    String email;
    @SerializedName("first_name")
    String firstName;
    @SerializedName("last_name")
    String lastName;
    @SerializedName("points")
    int points;
    @SerializedName("resource_uri")
    String resourceUri;
    @SerializedName("facebook_id")
    String facebookId;
    @SerializedName("facebook_pic_url")
    String facebookAvatar;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        UserProfileParcelablePlease.writeToParcel(this, dest, flags);
    }


    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        public UserProfile createFromParcel(Parcel source) {
            UserProfile target = new UserProfile();
            UserProfileParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };


    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Reward getCurrentReward() {
        return currentReward;
    }

    public void setCurrentReward(Reward currentReward) {
        this.currentReward = currentReward;
    }

    public String getGneder() {
        return gneder;
    }

    public void setGneder(String gneder) {
        this.gneder = gneder;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(String profileAvatar) {
        this.profileAvatar = profileAvatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFacebookAvatar() {
        return facebookAvatar;
    }

    public void setFacebookAvatar(String facebookAvatar) {
        this.facebookAvatar = facebookAvatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}