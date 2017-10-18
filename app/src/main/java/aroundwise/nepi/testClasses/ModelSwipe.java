package aroundwise.nepi.testClasses;

/**
 * Created by mihai on 01/09/16.
 */
public class ModelSwipe {

    String drawable;
    String store;
    String drawable_logo;

    public ModelSwipe(String drawable, String store, String drawable_logo) {
        this.drawable = drawable;
        this.store = store;
        this.drawable_logo = drawable_logo;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDrawable_logo() {
        return drawable_logo;
    }

    public void setDrawable_logo(String drawable_logo) {
        this.drawable_logo = drawable_logo;
    }
}
