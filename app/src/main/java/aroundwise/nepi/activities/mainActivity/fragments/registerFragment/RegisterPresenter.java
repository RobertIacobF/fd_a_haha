package aroundwise.nepi.activities.mainActivity.fragments.registerFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aroundwise.nepi.activities.mainActivity.MainActivity;
import aroundwise.nepi.activities.onBoardActivity.OnBoardActivity;
import aroundwise.nepi.network.api.LoginClient;
import aroundwise.nepi.network.api.RegisterClient;
import aroundwise.nepi.network.api.RewardsClient;
import aroundwise.nepi.network.api.UserClient;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.network.requests.CorporateCodeRequest;
import aroundwise.nepi.network.requests.RegisterCorporateCode;
import aroundwise.nepi.network.requests.RegisterRequest;
import aroundwise.nepi.network.requests.SetCurrentRewardRequest;
import aroundwise.nepi.network.requests.UploadImageRequest;
import aroundwise.nepi.network.responses.MallResponse;
import aroundwise.nepi.network.responses.RegisterResponse;
import aroundwise.nepi.network.responses.SetCurrentRewardResponse;
import aroundwise.nepi.network.responses.UpdateProfileResponse;
import aroundwise.nepi.network.responses.UploadResponse;
import aroundwise.nepi.network.responses.UserResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mihai on 07/09/16.
 */
public class RegisterPresenter extends MvpBasePresenter<RegisterView> {

    Context context;
    String imagePath, image, imagePhoto;
    String corporateCode;

    CompositeSubscription compositeSubscription;

    public void removeSubscriptions() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    public void createCompositeSubscription() {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed())
            compositeSubscription = new CompositeSubscription();
    }

    public RegisterPresenter(Context context) {
        this.context = context;
        createCompositeSubscription();
    }

    public void getMalls() {
        RegisterClient client = ServiceGenerator.getServiceGenerator().createService(RegisterClient.class);
        compositeSubscription.add(client.getMalls(10, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached())
                            getView().registerAfterMallsDownloaded();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideProgressBar();
                            getView().serverError("Verificati conexiunea la internet");
                        }
                        Log.d(MainActivity.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(MallResponse mallResponse) {
                        Session.setMalls(mallResponse.getMalls());
                    }
                }));
    }

    public void register(boolean isManager, String password, String surname, String name, String email, int mall, String token, String phoneNuber) {
        registerCall(isManager, password, surname, name, email, mall, token, phoneNuber);
    }

    public void registerCall(boolean isManager, final String password, String surname, String name, final String email, int mall, String token, String phoneNumber) {
        RegisterClient client = ServiceGenerator.getServiceGenerator().createService(RegisterClient.class);

        compositeSubscription.add(client.register(new RegisterRequest(email, isManager, password, surname, name, email, mall, token, phoneNumber))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterResponse>() {
                    @Override
                    public void onCompleted() {
                        //doLoginRequest(email, password);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(RegisterPresenter.class.getSimpleName(), e.getMessage());
                        if (e instanceof HttpException) {
                            HttpException error = (HttpException) e;
                            if (isViewAttached())
                                if (error.code() == 403)
                                    getView().serverError("User deja inregistrat cu aceste date");
                                else
                                    getView().serverError("Verificati conexiunea la internet");

                        }
                        if (isViewAttached())
                            getView().hideProgressBar();

                    }

                    @Override
                    public void onNext(RegisterResponse registerResponse) {
                        if (registerResponse.getStatus() == 472) {

                        } else {
                            Session.getUser().setId(registerResponse.getUserId());
                            Session.getUser().setSessionId(registerResponse.getSessionId());
                            if (imagePhoto != null && !imagePhoto.equals(""))
                                uploadImage();
                            else
                                getUserDetails();
                        }
                    }
                }));
    }


    public void setPicFromGallery(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if (isViewAttached())
            getView().setProfileImage(selectedImage);
        // Get the cursor
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        // Move to first row
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
            if (imagePath == null) {
                Toast.makeText(context, "Could not select photo!", Toast.LENGTH_SHORT);
                return;
            }

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
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                imagePhoto = "data:image/png;base64," + image;
                //Logger.d(imagePhoto);


            } catch (Exception e) {
                Toast.makeText(context, "Could not select photo!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Bitmap rotateBitmap(Bitmap source, int rotation) {
        Matrix matrix = new Matrix();
        matrix.setRotate(rotation, source.getWidth() / 2, source.getHeight() / 2);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    private void uploadImage() {
        LoginClient client = ServiceGenerator.getServiceGenerator().createService(LoginClient.class);
        compositeSubscription.add(client.uploadImage(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getId(), new UploadImageRequest(imagePhoto))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UploadResponse>() {
                    @Override
                    public void onCompleted() {
                        getUserDetails();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(RegisterPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached())
                            getView().hideProgressBar();
                    }

                    @Override
                    public void onNext(UploadResponse basicResponse) {

                    }
                }));
    }

    private void onboard() {
        Intent intent = new Intent(context, OnBoardActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public boolean checkEditTextEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setError("Camp obligatoriu!");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    public boolean checkPhoneNumber(EditText etPhone) {
        if (etPhone.getText().toString().length() != 10) {
            etPhone.setError("Nuamrul de telefon trebuie sa fie de 10 caractere");
            return false;
        }
        etPhone.setError(null);
        return true;
    }

    public boolean checkPassword(String password) {
        if (password.length() < 8) {
            if (isViewAttached()) {
                getView().showAlertError("Parola trebuie sa fie de minim 8 caractere!");
            }
            return false;
        }
        return true;
    }

    private void getUserDetails() {
        LoginClient loginClient = ServiceGenerator.getServiceGenerator().createService(LoginClient.class);
        compositeSubscription.add(loginClient.userDetails(Session.getUser().getId(), Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        Session.getUser().setUserResponse(userResponse);
                        if (corporateCode != null && !corporateCode.isEmpty()) {
                            updateCouponCode(corporateCode);
                        } else {
                            Session.getUser().getProfile().setCorporate_code("");
                            rewardOrOnBoard();
                        }
                    }
                }));
    }

    public void updateCurrentReward(final Reward reward) {
        String resourceUri = "";
        if (reward != null)
            resourceUri = reward.resource_uri;
        RewardsClient client = ServiceGenerator.getServiceGenerator().createService(RewardsClient.class);
        compositeSubscription.add(client.setCurrentReward(Constants.SESSIONID + Session.getUser().getSessionId(),
                Session.getUser().getProfile().getId(), new SetCurrentRewardRequest(resourceUri))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SetCurrentRewardResponse>() {
                    @Override
                    public void onCompleted() {
                        Session.getUser().getProfile().setCurrent_reward(reward);
                        Session.saveUserToSharedPref(context);
                        onboard();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(RegisterPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(SetCurrentRewardResponse setCurrentRewardResponse) {
                        //  Logger.d(setCurrentRewardResponse.getMessage());
                    }
                }));
    }

    public boolean isEmailValid(EditText editText) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = editText.getText().toString();

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        if (!isValid)
            editText.setError("Please provide a valid email!");
        else
            editText.setError(null);
        return isValid;
    }

    public void updateCouponCode(String code) {
        UserClient client = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        compositeSubscription.add(client.registerCoupon(Session.getUser().getUserProfileId(),
                new RegisterCorporateCode(code, 1),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateProfileResponse>() {
                    @Override
                    public void onCompleted() {
                        rewardOrOnBoard();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(RegisterPresenter.class.getSimpleName(), e.getMessage());
                        if (isViewAttached()) {
                        }
                    }

                    @Override
                    public void onNext(UpdateProfileResponse updateProfileResponse) {
                        Session.getUser().getProfile().setCorporate_code(updateProfileResponse.getCode() != null ? updateProfileResponse.getCode() : "");
                        Session.getUser().getProfile().setCorporate_code_validated(updateProfileResponse.isCodeValidated());
                    }
                }));
    }

    private void rewardOrOnBoard() {
        if (Session.getOnboardReward() != null)
            updateCurrentReward(Session.getOnboardReward());
        else {
            Session.saveUserToSharedPref(context);
            onboard();
        }
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImagePhoto() {
        return imagePhoto;
    }

    public void setCorporateCode(String corporateCode) {
        this.corporateCode = corporateCode;
    }
}
