package aroundwise.nepi.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by mihai on 07/09/16.
 */
@ParcelablePlease
public class Mall implements Parcelable {
    String resource_uri;
    String description;
    int id;
    String logo;
    String name;

    public Mall(String resource_uri, String description, int id, String logo, String name) {
        this.resource_uri = resource_uri;
        this.description = description;
        this.id = id;
        this.logo = logo;
        this.name = name;
    }

    protected Mall(Parcel in) {
        resource_uri = in.readString();
        description = in.readString();
        id = in.readInt();
        logo = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resource_uri);
        dest.writeString(description);
        dest.writeInt(id);
        dest.writeString(logo);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mall> CREATOR = new Creator<Mall>() {
        @Override
        public Mall createFromParcel(Parcel in) {
            return new Mall(in);
        }

        @Override
        public Mall[] newArray(int size) {
            return new Mall[size];
        }
    };

    public String getResource_uri() {
        return resource_uri;
    }

    public void setResource_uri(String resource_uri) {
        this.resource_uri = resource_uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Mall{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

