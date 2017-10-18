package aroundwise.nepi.activities.cameraActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.adapters.ViewPagerImageAdapter;
import aroundwise.nepi.base.BaseActivity;
import aroundwise.nepi.network.api.OCRClient;
import aroundwise.nepi.network.requests.OCRRequest;
import aroundwise.nepi.network.responses.OCRResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.SensorOrientationChangeNotifier;
import aroundwise.nepi.util.views.CirclePageIndicator;
import aroundwise.nepi.util.views.CustomViewPager;
import aroundwise.nepi.util.views.SAutoBgButton;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CameraActivity extends BaseActivity implements SensorOrientationChangeNotifier.Listener {

    @BindView(R.id.content)
    TextureView textureView;

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


    final int CAMERAREQUEST = 99;
    final int STORAGEREQUEST = 999;

    private ProgressDialog progressDialog;
    Context context;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    String imageBase64;
    String TAG = CameraActivity.class.getSimpleName();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;

    Boolean showTutorial;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    static final String appDirectoryName = "aHa";
    static final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);


    int orientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (askForPermissions()) {
            initialize();
        } else {
            Toast.makeText(CameraActivity.this, "Nu putem folosi camera fara acordul d-voastra!", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
        rl_finished.setVisibility(View.GONE);
        rl_take_picture.setVisibility(View.VISIBLE);
        iv_capture.setVisibility(View.INVISIBLE);
        textureView.setVisibility(View.VISIBLE);
        SensorOrientationChangeNotifier.getInstance().addListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        //closeCamera();
        stopBackgroundThread();
        SensorOrientationChangeNotifier.getInstance().remove(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        stopBackgroundThread();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initialize() {
        context = this;
        assert textureView != null;
        if (askForCamerPermssion()) {
            initializeCamera();
        }
    }

    private boolean askForPermissions() {
        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, STORAGEREQUEST);
            return false;
        }
        return true;
    }

    private void initializeCamera() {
        textureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                //open your camera here
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // Transform you image captured size according to the surface width and height
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        };
        stateCallback = new CameraDevice.StateCallback() {

            @Override
            public void onOpened(CameraDevice camera) {
                //This is called when the camera is open
                Log.e(TAG, "onOpened");
                cameraDevice = camera;
                createCameraPreview();
            }

            @Override
            public void onDisconnected(CameraDevice camera) {
                cameraDevice.close();
            }

            @Override
            public void onError(CameraDevice camera, int error) {
                if (camera != null && cameraDevice != null)
                    cameraDevice.close();
                cameraDevice = null;
            }
        };

        textureView.setSurfaceTextureListener(textureListener);
        initializeViewPager();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, Constants.PREFFERENCES, MODE_PRIVATE);
        showTutorial = complexPreferences.getObject(Constants.SHOW_TUTORIAL, Boolean.class);
        if (showTutorial == null) {
            rl_viewpager.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_in));
            rl_viewpager.setVisibility(View.VISIBLE);
            rl_photo.setVisibility(View.INVISIBLE);
            complexPreferences.putObject(Constants.SHOW_TUTORIAL, true);
        } else {
            rl_viewpager.setVisibility(View.INVISIBLE);
            rl_photo.setVisibility(View.VISIBLE);
        }

    }


    private boolean askForCamerPermssion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
            }, CAMERAREQUEST);
            return false;
        }
        return true;
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
        ViewPagerImageAdapter adapter = new ViewPagerImageAdapter(images, this);
        viewpager.setAdapter(adapter);
        view_pager_indicator.setViewPager(viewpager);
    }

    @OnClick(R.id.btn_cancel)
    public void cancel() {
        closeCamera();
        this.finish();
    }

    @OnClick(R.id.btn_tips)
    public void tips() {
        rl_tips.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_in));
        rl_tips.setVisibility(View.VISIBLE);
        tvInfoPhoto.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btn_finished)
    public void finished() {
        sendReceiptToServer(imageBase64);
        /*Toast.makeText(CameraActivity.this, "Scanam poza pentru a interpreta bonul. " +
                "Acest proces poate dura pana la 20 secunde.", Toast.LENGTH_SHORT).show();*/
    }

    @OnClick(R.id.btn_lets_go)
    public void closetips() {
        rl_tips.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_out));
        rl_tips.setVisibility(View.GONE);
        tvInfoPhoto.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_retake)
    public void retake() {
        takeAnotherPhoto();
        //iv_capture.setVisibility(View.INVISIBLE);
        //textureView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_close_tips)
    public void closeTutorial() {
        rl_viewpager.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_out));
        rl_viewpager.setVisibility(View.INVISIBLE);
        rl_photo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeCamera();
    }

    private void takeAnotherPhoto() {
        rl_finished.setVisibility(View.GONE);
        rl_take_picture.setVisibility(View.VISIBLE);
        closeCamera();
        openCamera();
    }

    TextureView.SurfaceTextureListener textureListener;

    private CameraDevice.StateCallback stateCallback;

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else
            mBackgroundHandler = null;
    }

    protected void takePicture() {
        if (null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            int deviceOrientation = getWindowManager().getDefaultDisplay().getRotation();
            final int currentOrientation = orientation;
            deviceOrientation = (deviceOrientation + 45) / 90 * 90;
            int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360;

            /*Integer orientationValue = 0;
            if (currentOrientation >= 0 && currentOrientation <= 90)
                orientationValue = ORIENTATIONS.get(0);
            else if (currentOrientation > 90 && currentOrientation <= 180)
                orientationValue = ORIENTATIONS.get(1);
            else if (currentOrientation > 180 && currentOrientation <= 270)
                orientationValue = ORIENTATIONS.get(2);
            else if (currentOrientation > 270 && currentOrientation < 360)
                orientationValue = ORIENTATIONS.get(3);*/

            //captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.valueAt(SensorOrientationChangeNotifier.getInstance().getOrientationMode()));
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, 180);
            if (!imageRoot.exists())
                imageRoot.mkdir();
            if (file == null)
                file = new File(imageRoot, System.currentTimeMillis() + ".jpg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();

                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        bitmap = getResizedBitmap(bitmap, 1500, 1500);
                        save(bytes);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                        String imageS = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                        imageBase64 = "data:image/png;base64," + imageS;
                        CameraActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        CameraActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                            }
                        });
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                        MediaScannerConnection.scanFile(CameraActivity.this, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    //createCameraPreview();

                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
            changeBottom();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void sendReceiptToServer(String imageBase64) {
        showProgress("Scanăm poza pentru a interpreta bonul." + "Acest proces poate dura pâna la 20 de secunde.");
        OCRClient ocrClient = ServiceGenerator.getServiceGenerator().createService(OCRClient.class);
        ocrClient.scanReceiptCall(Session.getUser().getUserProfileId(),
                new OCRRequest(imageBase64), Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OCRResponse>() {
                    @Override
                    public void call(OCRResponse ocrResponse) {
                        Log.i(CameraActivity.class.getSimpleName(), ocrResponse.toString());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(CameraActivity.class.getSimpleName(), throwable.getMessage());
                        Toast.makeText(CameraActivity.this, "A apărut o eroare. Vă rugăm șă mai trimiteți o data!", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Toast.makeText(CameraActivity.this, "Mulțumim! Revenim în maximum 24 de ore.", Toast.LENGTH_LONG).show();
                        takeAnotherPhoto();
                        hideProgressDialog();
                    }
                });
    }

    protected void createCameraPreview() {
        try {

            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            if (texture != null) {
                texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
                Surface surface = new Surface(texture);
                captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                captureRequestBuilder.addTarget(surface);
                cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                        //The camera is already closed
                        if (null == cameraDevice) {
                            return;
                        }
                        // When the session is ready, we start displaying the preview.
                        cameraCaptureSessions = cameraCaptureSession;
                        updatePreview();
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    }
                }, null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        if (askForCamerPermssion()) {
            CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            Log.e(TAG, "is camera open");
            try {
                cameraId = manager.getCameraIdList()[0];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                assert map != null;
                imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
                manager.openCamera(cameraId, stateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "openCamera X");
        }
    }

    protected void updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    private void changeBottom() {
        rl_take_picture.setVisibility(View.INVISIBLE);
        rl_finished.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_take_photo)
    public void takePhoto() {
        showProgress();
        takePicture();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERAREQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            initializeCamera();

        if (requestCode == STORAGEREQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initialize();
        }
    }

    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        progressDialog.show();
        progressDialog.setContentView(R.layout.message_progress_dialog);
        TextView tvMessage = (TextView) progressDialog.findViewById(R.id.message);
        tvMessage.setText(message);
    }

    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.hide();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressDialog = null;
    }

    @Override
    public void onOrientationChange(int orientation) {

    }
}
