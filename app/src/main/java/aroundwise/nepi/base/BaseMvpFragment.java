package aroundwise.nepi.base;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpView;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mihai on 29/08/16.
 */
public abstract class BaseMvpFragment<V extends MvpView, P extends MvpBasePresenter<V>> extends MvpFragment<V, P> {


    @Nullable
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @Nullable
    @BindView(R.id.layout_progress)
    RelativeLayout layout_progress;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgress();
    }

    @Nullable
    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    @Nullable
    protected void showProgress() {
        /*if (layout_progress != null)
            layout_progress.setVisibility(View.VISIBLE);*/
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
    protected void hideProgress() {
       /* if (layout_progress != null)
            layout_progress.setVisibility(View.GONE);*/
        if (progressDialog != null)
            progressDialog.dismiss();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressDialog = null;
    }

    protected abstract int getLayout();

    protected abstract void setUpViews();


}
