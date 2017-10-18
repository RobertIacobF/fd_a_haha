package aroundwise.nepi.activities.discoverActivity.fragments.profileFragment.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;


import java.io.ByteArrayOutputStream;
import java.util.Map;

import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.eventbus.OnProfileChangeEvent;
import aroundwise.nepi.network.api.LoginClient;
import aroundwise.nepi.network.api.UserClient;
import aroundwise.nepi.network.requests.ChangeUserProfileRequest;
import aroundwise.nepi.network.requests.CorporateCodeRequest;
import aroundwise.nepi.network.requests.UploadImageRequest;
import aroundwise.nepi.network.responses.UpdateProfileResponse;
import aroundwise.nepi.network.responses.UploadResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mihai on 22/09/16.
 */
public class ProfileSettingsPresenter extends MvpBasePresenter<ProfileSettingsView> {

    private String imagePhoto, imagePath, image;


    public void changeUserProfile(final String firstName, final String lastName) {
        UserClient client = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        client.updateUserProfile(Session.getUser().getId(),
                new ChangeUserProfileRequest(firstName, lastName),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateProfileResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached())
                            uploadImage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ProfileSettingsPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached()) {
                            getView().displaySnackbarMessage("Something went wrong!\nPlease try again later!");
                            getView().hideProgressView();
                        }
                    }

                    @Override
                    public void onNext(UpdateProfileResponse updateProfileResponse) {
                        if (isViewAttached())
                            getView().displaySnackbarMessage("Changes successfully made.");
                        Session.getUser().setFirst_name(firstName);
                        Session.getUser().setLast_name(lastName);

                    }
                });
    }

    public void updateCouponCode(final String code, final String firstName, final String lastName,
                                 final String gender, final String date, final String phoneNumber) {
        UserClient client = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        String cupon = code.equals(Session.getUser().getProfile().getCorporateCode()) ? "" : code;
        client.updateCoupon(Session.getUser().getUserProfileId(),
                new CorporateCodeRequest(cupon, firstName, lastName, gender, date, phoneNumber),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateProfileResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached())
                            uploadImage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ProfileSettingsPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached()) {
                            getView().displaySnackbarMessage("Eroare. Incercați mai târziu!");
                            getView().hideProgressView();
                        }
                    }

                    @Override
                    public void onNext(UpdateProfileResponse updateProfileResponse) {
                        if (isViewAttached()) {
                            if (updateProfileResponse.getMessage() != null) {
                                if (!updateProfileResponse.isCodeValidated())
                                    getView().displaySnackbarMessage(code + " nu este valid!");
                                else
                                    getView().displaySnackbarMessage(code + " a fost validat!");
                            }
                        }
                        Session.getUser().setFirst_name(firstName);
                        Session.getUser().setLast_name(lastName);
                        Session.getUser().getProfile().setBirthdate(date);
                        Session.getUser().getProfile().setGender(gender);
                        Session.getUser().getProfile().setPhoneNumber(phoneNumber);


                    }
                });
    }

    public void updateCouponCode(final String code) {
        if (isViewAttached())
            getView().showProgressBar();
        UserClient client = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        client.updateCoupon(Session.getUser().getUserProfileId(),
                new CorporateCodeRequest(code),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateProfileResponse>() {
                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new OnProfileChangeEvent());
                        if (isViewAttached())
                            getView().hideProgressView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ProfileSettingsPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached()) {
                            getView().displaySnackbarMessage("Something went wrong!\nPlease try again later!");
                            getView().hideProgressView();
                        }
                    }

                    @Override
                    public void onNext(UpdateProfileResponse updateProfileResponse) {
                        if (isViewAttached()) {
                            if (!updateProfileResponse.isCodeValidated())
                                getView().displaySnackbarMessage(code + " nu este valid!");
                            else
                                getView().displaySnackbarMessage(code + " a fost validat!");

                        }
                    }
                });
    }

    public void uploadImage() {
        if (imagePhoto != null && !imagePhoto.isEmpty()) {
            LoginClient client = ServiceGenerator.getServiceGenerator().createService(LoginClient.class);
            client.uploadImage(Constants.SESSIONID + Session.getUser().getSessionId(),
                    Session.getUser().getId(), new UploadImageRequest(imagePhoto))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UploadResponse>() {
                        @Override
                        public void onCompleted() {
                            EventBus.getDefault().post(new OnProfileChangeEvent());
                            if (isViewAttached()) {
                                getView().hideProgressView();
                                EventBus.getDefault().post(new MallHasChangedEvent());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(ProfileSettingsPresenter.class.getSimpleName(), e.getMessage());
                            if (isViewAttached())
                                getView().hideProgressView();
                        }

                        @Override
                        public void onNext(UploadResponse basicResponse) {

                        }
                    });
        } else {
            if (isViewAttached()) {
                getView().hideProgressView();
                getView().finishActivity();
            }
        }
    }


    public void createBase64(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Get the cursor
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        // Move to first row
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            try {
                ExifInterface ei = new ExifInterface(imagePath);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Bitmap rotatedBitmap = bitmap;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateBitmap(bitmap, 90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateBitmap(bitmap, 180);
                        break;
                    // etc.
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                imagePhoto = "data:image/png;base64," + image;

            } catch (Exception e) {
                Toast.makeText(context, "Poza nu s-a putut încărca. Încercați din nou!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Poza nu s-a putut încărca. Încercați din nou!", Toast.LENGTH_SHORT).show();
        }
    }


    private Bitmap rotateBitmap(Bitmap source, int rotation) {
        Matrix matrix = new Matrix();
        matrix.setRotate(rotation, source.getWidth() / 2, source.getHeight() / 2);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}
