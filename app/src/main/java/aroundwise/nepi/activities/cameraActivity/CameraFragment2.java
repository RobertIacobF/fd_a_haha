package aroundwise.nepi.activities.cameraActivity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.adapters.ViewPagerImageAdapter;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.eventbus.LogOutEvent;
import aroundwise.nepi.network.api.OCRClient;
import aroundwise.nepi.network.requests.OCRRequest;
import aroundwise.nepi.network.responses.OCRResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import aroundwise.nepi.util.views.CirclePageIndicator;
import aroundwise.nepi.util.views.CustomViewPager;
import aroundwise.nepi.util.views.SAutoBgButton;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class CameraFragment2 extends BaseFragment {

    @BindView(R.id.camera)
    CameraView camera;

    @BindView(R.id.btn_take_photo)
    SAutoBgButton btn_take_photo;

    @BindView(R.id.rl_photo)
    RelativeLayout rl_photo;

    @BindView(R.id.rl_take_picture)
    RelativeLayout rl_take_picture;

    @BindView(R.id.rl_viewpager)
    RelativeLayout rl_viewpager;

    @BindView(R.id.rl_finished)
    RelativeLayout rl_finished;

    @BindView(R.id.iv_capture)
    ImageView iv_capture;

    @BindView(R.id.rl_tips)
    RelativeLayout rl_tips;

    @BindView(R.id.viewpager)
    CustomViewPager viewpager;

    @BindView(R.id.top)
    TextView tvInfoPhoto;

    @BindView(R.id.viewpager_indicator)
    CirclePageIndicator view_pager_indicator;

    private Subscription subscription;

    static final String appDirectoryName = "aHa";
    private final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);

    private File newFile;
    private ProgressDialog progressDialog;
    public String imageBase64;

    public CameraFragment2() {
        // Required empty public constructor
    }

    public static CameraFragment2 newInstance() {
        CameraFragment2 cameraFragment2 = new CameraFragment2();
        return cameraFragment2;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_camera_fragment2;
    }

    @Override
    protected void setUpViews() {
        createAhaDirectory();
        initializeViewPager();
        displayTutorial();
        initializeCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (subscription != null)
            subscription.unsubscribe();
        camera.stop();
    }

    @OnClick(R.id.btn_cancel)
    public void cancel() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_take_photo)
    public void takePhoto() {
        showProgress();
        camera.captureImage();
        rl_finished.setVisibility(View.VISIBLE);
        rl_take_picture.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_tips)
    public void tips() {
        rl_tips.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in));
        rl_tips.setVisibility(View.VISIBLE);
        tvInfoPhoto.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btn_finished)
    public void finished() {
        sendReceiptToServer(imageBase64);
    }

    @OnClick(R.id.btn_lets_go)
    public void closetips() {
        rl_tips.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out));
        rl_tips.setVisibility(View.GONE);
        tvInfoPhoto.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_retake)
    public void retake() {
        camera.stop();
        camera.start();
        initializeCamera();
        rl_finished.setVisibility(View.GONE);
        rl_take_picture.setVisibility(View.VISIBLE);
        iv_capture.setImageDrawable(null);
        iv_capture.setVisibility(View.INVISIBLE);
        camera.setVisibility(View.VISIBLE);
        //textureView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_close_tips)
    public void closeTutorial() {
        rl_viewpager.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out));
        rl_viewpager.setVisibility(View.INVISIBLE);
        rl_photo.setVisibility(View.VISIBLE);
    }


    private void createAhaDirectory() {
        if (!imageRoot.exists())
            imageRoot.mkdir();
    }

    private void createFileToSaveImage() {
        newFile = new File(imageRoot, System.currentTimeMillis() + ".jpg");
    }

    private void initializeViewPager() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.date_1);
        images.add(R.drawable.indoaie_bonul_2);
        images.add(R.drawable.centreaza_3);
        images.add(R.drawable.centreaza_4);
        images.add(R.drawable.claritate_5);
        images.add(R.drawable.claritate_6);
        images.add(R.drawable.distanta_7);
        images.add(R.drawable.distanta_8);
        images.add(R.drawable.perspectiva_9);
        images.add(R.drawable.perspectiva_10);
        ViewPagerImageAdapter adapter = new ViewPagerImageAdapter(images, getActivity());
        viewpager.setAdapter(adapter);
        view_pager_indicator.setViewPager(viewpager);
    }

    private void initializeCamera() {
        createFileToSaveImage();
        camera.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(final byte[] jpeg) {
                super.onPictureTaken(jpeg);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap result = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
                        try {
                            Util.saveFile(jpeg, newFile, getActivity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_capture.setVisibility(View.VISIBLE);
                                MyApplication.instance.getPicasso()
                                        .load(newFile)
                                        .into(iv_capture);
                                camera.setVisibility(View.INVISIBLE);
                                /*MyApplication.instance.getPicasso()
                                        .load(result)
                                        .into(iv_capture);*/
                            }
                        });
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        result.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        String imageS = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                        imageBase64 = "data:image/png;base64," + imageS;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                            }
                        });
                    }
                });
                thread.start();
            }
        });

    }

    private void displayTutorial() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), Constants.PREFFERENCES, getActivity().MODE_PRIVATE);
        Boolean showTutorial = complexPreferences.getObject(Constants.SHOW_TUTORIAL, Boolean.class);
        if (showTutorial == null || showTutorial) {
            rl_viewpager.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in));
            rl_viewpager.setVisibility(View.VISIBLE);
            rl_photo.setVisibility(View.INVISIBLE);
            complexPreferences.putObject(Constants.SHOW_TUTORIAL, false);
        } else {
            rl_viewpager.setVisibility(View.INVISIBLE);
            rl_photo.setVisibility(View.VISIBLE);
        }
    }

    private void sendReceiptToServer(String imageBase64) {
        showProgress("Scanăm poza pentru a interpreta bonul." + "Acest proces poate dura pâna la 20 de secunde.");
        OCRClient ocrClient = ServiceGenerator.getServiceGenerator().createService(OCRClient.class);
        subscription = ocrClient.scanReceiptCall(Session.getUser().getUserProfileId(),
                new OCRRequest(imageBase64), Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OCRResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                EventBus.getDefault().post(new LogOutEvent());
                            }
                        }
                        if (e instanceof UnknownHostException) {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                            builder.setMessage("A apărut o eroare! Vă rugăm să verificați conexiunea la internet și să mai încercați o data!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onNext(OCRResponse ocrResponse) {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        builder.setMessage("Mulțumim! Revenim în maximum 24 de ore.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                })
                                .create()
                                .show();
                        hideProgressDialog();
                    }
                });
    }

    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        progressDialog.show();
        progressDialog.setContentView(R.layout.message_progress_dialog);
        TextView tvMessage = (TextView) progressDialog.findViewById(R.id.message);
        tvMessage.setText(message);
    }

    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.hide();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressDialog = null;
    }

    @Nullable
    public void hideProgress() {
        if (progressDialog != null)
            progressDialog.hide();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressDialog = null;
    }

    @Nullable
    public void showProgress() {
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
}
