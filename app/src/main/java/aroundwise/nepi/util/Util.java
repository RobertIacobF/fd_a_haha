package aroundwise.nepi.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaScannerConnection;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import aroundwise.nepi.base.BaseActivity;
import aroundwise.nepi.base.BaseMvpActivity;

/**
 * Created by mihai on 29/08/16.
 */
public class Util {

    public static void showSoftKeyboard(Context context, View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }


    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }

    }

    public static void reinitializeShowTutorialOfCamera(Context context) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, Constants.PREFFERENCES, context.MODE_PRIVATE);
        complexPreferences.putObject(Constants.SHOW_TUTORIAL, true);
    }

    public static void saveFile(byte[] bytes, File file, Context context) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(bytes);
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
        } finally {
            if (null != output) {
                output.close();
            }
        }
    }


    public static void createSnackbar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public static void showProgressBar(BaseActivity activity) {
        activity.showProgress();
    }


    public static void hideProgressBar(BaseActivity activity) {
        activity.hideProgress();
    }

    public static void showProgressBar(BaseMvpActivity activity) {
        activity.showProgress();
    }


    public static void hideProgressBar(BaseMvpActivity activity) {
        activity.hideProgress();
    }

    public static void printKeyHash(Context context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("aroundwise.nepi", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }

    public static void setCapitalizeTextWatcher(final EditText editText) {
        final TextWatcher textWatcher = new TextWatcher() {

            int mStart = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStart = start + count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Use WordUtils.capitalizeFully if you only want the first letter of each word to be capitalized
                String capitalizedText = editText.getText().toString();
                if (capitalizedText.length() == 1)
                    capitalizedText = capitalizedText.toUpperCase();
                if (!capitalizedText.equals(editText.getText().toString())) {
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            editText.setSelection(mStart);
                            editText.removeTextChangedListener(this);
                        }
                    });
                    editText.setText(capitalizedText);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);
    }
}
