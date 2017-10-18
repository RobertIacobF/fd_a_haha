package aroundwise.nepi.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import aroundwise.nepi.IntentStarter;
import aroundwise.nepi.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;

/**
 * Created by mihai on 24/08/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    EventBus eventBus;
    protected IntentStarter intentStarter;

    @Nullable
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Nullable
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;


    ProgressDialog progressDialog;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        //eventBus = EventBus.getDefault();
        //eventBus.register(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentStarter = new IntentStarter();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog = null;
    }

    @Nullable
    public void hideProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressDialog = null;
    }

    @Nullable
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            try {
                progressDialog.show();
            } catch (Exception e) {
                progressDialog.dismiss();
            }
            progressDialog.setContentView(R.layout.simple_progress_dialog);
        } else {
            progressDialog.show();
            progressDialog.setContentView(R.layout.simple_progress_dialog);
        }
    }

    @Nullable
    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}