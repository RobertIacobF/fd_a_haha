package aroundwise.nepi.network.requests;

/**
 * Created by mihai on 07/09/16.
 */
public class UploadImageRequest {
    String image;

    public UploadImageRequest(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
