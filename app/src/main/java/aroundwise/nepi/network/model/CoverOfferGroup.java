package aroundwise.nepi.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irina on 12/21/2016.
 */
public class CoverOfferGroup {

    @SerializedName("group_headline")
    String groupHeadline;

    @SerializedName("group_sub_headline")
    String groupSubHeadline;

    @SerializedName("id")
    int id;

    @SerializedName("image")
    String image;

    @SerializedName("resource_uri")
    String resourceUri;

    public String getGroupHeadline() {
        return groupHeadline;
    }

    public void setGroupHeadline(String groupHeadline) {
        this.groupHeadline = groupHeadline;
    }

    public String getGroupSubHeadline() {
        return groupSubHeadline;
    }

    public void setGroupSubHeadline(String groupSubHeadline) {
        this.groupSubHeadline = groupSubHeadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
