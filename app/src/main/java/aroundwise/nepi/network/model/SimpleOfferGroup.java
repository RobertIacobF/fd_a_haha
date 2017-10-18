package aroundwise.nepi.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irina on 12/22/2016.
 */
public class SimpleOfferGroup {

    @SerializedName("group_headline")
    String groupHeadline;

    @SerializedName("group_sub_headline")
    String groupSubHeadline;

    @SerializedName("id")
    int id;

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

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
