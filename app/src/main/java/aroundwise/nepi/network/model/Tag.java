package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by mihai on 20/09/16.
 */
@ParcelablePlease
public class Tag implements Parcelable {
    @SerializedName("id")
    long id;
    @SerializedName("name")
    String name;
    @SerializedName("resource_uri")
    String resource_uri;
    public static final Creator<Tag> CREATOR = new Creator() {
        public Tag createFromParcel(Parcel source) {
            Tag target = new Tag();
            TagParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    public Tag() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        TagParcelablePlease.writeToParcel(this, dest, flags);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource_uri() {
        return this.resource_uri;
    }

    public void setResource_uri(String resource_uri) {
        this.resource_uri = resource_uri;
    }

    public Tag(com.aroundwise.aroundwisesdk.model.http.shop.Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        this.resource_uri = tag.getResource_uri();
    }
}