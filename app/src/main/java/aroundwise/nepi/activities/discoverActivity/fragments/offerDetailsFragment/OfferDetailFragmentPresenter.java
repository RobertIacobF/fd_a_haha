package aroundwise.nepi.activities.discoverActivity.fragments.offerDetailsFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.ByteArrayOutputStream;

/**
 * Created by mihai on 30/08/16.
 */
public class OfferDetailFragmentPresenter extends MvpBasePresenter<OfferDetailFragmentView> {

    @Nullable
    public Uri getBitmapUri(Bitmap bitmap, String title, Context context) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, title, null);
        if (path != null)
            return Uri.parse(path);
        return null;
    }
}
