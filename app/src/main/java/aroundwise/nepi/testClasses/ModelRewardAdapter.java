package aroundwise.nepi.testClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mihai on 05/09/16.
 */
public class ModelRewardAdapter implements Parcelable {

    int drawable_back;
    int drawable_logo;
    String product;
    String points;

    public ModelRewardAdapter(int drawable_back, int drawable_logo, String product, String points) {
        this.drawable_back = drawable_back;
        this.drawable_logo = drawable_logo;
        this.product = product;
        this.points = points;
    }

    protected ModelRewardAdapter(Parcel in) {
        drawable_back = in.readInt();
        drawable_logo = in.readInt();
        product = in.readString();
        points = in.readString();
    }

    public static final Creator<ModelRewardAdapter> CREATOR = new Creator<ModelRewardAdapter>() {
        @Override
        public ModelRewardAdapter createFromParcel(Parcel in) {
            return new ModelRewardAdapter(in);
        }

        @Override
        public ModelRewardAdapter[] newArray(int size) {
            return new ModelRewardAdapter[size];
        }
    };

    public int getDrawable_back() {
        return drawable_back;
    }

    public int getDrawable_logo() {
        return drawable_logo;
    }

    public String getProduct() {
        return product;
    }

    public String getPoints() {
        return points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(drawable_back);
        parcel.writeInt(drawable_logo);
        parcel.writeString(product);
        parcel.writeString(points);
    }
}
