package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.network.api.UserClient;
import aroundwise.nepi.network.model.Mall;
import aroundwise.nepi.network.requests.UpdateMallRequest;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import de.greenrobot.event.EventBus;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mihai on 30/08/16.
 */
public class LoyaltyFragmentPresenter extends MvpBasePresenter<LoyaltyFragmentView> {

    private CompositeSubscription subscription;

    public LoyaltyFragmentPresenter() {
        subscription = new CompositeSubscription();
    }

    public Bitmap addGradient(Bitmap originalBitmap) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);

        canvas.drawBitmap(originalBitmap, 0, 0, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, 0, height, 0xB0b56abc, 0xB0dd4575, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawRect(0, 0, width, height, paint);

        return updatedBitmap;
    }

    public void changeMall(final Mall mall) {
        if (isViewAttached())
            getView().showProgressView();
        UserClient offerClient = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        subscription.add(offerClient.changeMall(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getId(), new UpdateMallRequest(mall.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Session.getUser().getProfile().setMall(mall);
                        EventBus.getDefault().post(new MallHasChangedEvent());
                        if (isViewAttached()) {
                            getView().mallsSwitched();
                            getView().hideProgressView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached())
                            getView().hideProgressView();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                    }
                }));
    }

}
