package aroundwise.nepi.activities.mainActivity.fragments.registerFragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.mainActivity.MainActivity;
import aroundwise.nepi.activities.mainActivity.fragments.loginFragment.LoginFragment;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Blur;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import aroundwise.nepi.util.views.SAutoBgButton;
import aroundwise.nepi.util.views.SAutoImageView;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

/**
 * A simple {@link Fragment} subclass.
 */


public class RegisterFragment extends BaseMvpFragment<RegisterView, RegisterPresenter> implements RegisterView {

    //region BindView

    @BindView(R.id.scrollview)
    ScrollView scrollView;

    @BindView(R.id.btn_upload_photo)
    SAutoBgButton btn_photo;

    @BindView(R.id.btn_close_2)
    SAutoImageView btn_close_2;

    @BindView(R.id.btn_close)
    SAutoBgButton btn_close;

    @BindView(R.id.layout_register2)
    RelativeLayout layout_2;

    @BindView(R.id.layout_mall_promenada)
    RelativeLayout l_mall_promenada;

    @BindView(R.id.layout_mega_mall)
    RelativeLayout l_mega_mall;

    @BindView(R.id.iv_megamall)
    ImageView iv_megamall;

    @BindView(R.id.iv_mall_promenada)
    ImageView iv_promenada;

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_surname)
    EditText et_surname;

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.iv_background)
    ImageView iv_background;

    @BindView(R.id.iv_profile)
    CircleImageView iv_profile;

    @BindView(R.id.layout_progress)
    RelativeLayout layout_progress;

    @BindView(R.id.et_code_corporate)
    EditText etCodeCorporate;

    @BindView(R.id.cb_legit)
    CheckBox cbLegit;

    @BindView(R.id.cb_terms)
    CheckBox cbTerms;

    @BindView(R.id.view_shadow_top)
    View shadowTop;

    //endregion

    boolean isPart2Visibile = false;
    private int mall_selected = 0;
    String corporateCode;

    String name, surname, email, password, phoneNo;
    Target target;
    Integer background;
    Target imageTarget;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    public static RegisterFragment newInstance(String email, String firstName, String lastName
            , String profileUrl) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle params = new Bundle();
        params.putString("email", email);
        params.putString("firstName", firstName);
        params.putString("lastName", lastName);
        params.putString("profileUrl", profileUrl);
        fragment.setArguments(params);
        return fragment;
    }

    public RegisterFragment() {

    }

    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter(getActivity());
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_register;
    }

    @Override

    protected void setUpViews() {
        imageTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                iv_profile.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                presenter.imagePhoto = "data:image/png;base64," + image;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Util.setCapitalizeTextWatcher(et_surname);
        Util.setCapitalizeTextWatcher(et_name);

        if (getArguments() != null) {
            if (getArguments().containsKey("email")) {
                String email = getArguments().getString("email");
                et_email.setText(email);
            }
            if (getArguments().containsKey("firstName")) {
                String firstName = getArguments().getString("firstName");
                et_surname.setText(firstName);
            }
            if (getArguments().containsKey("lastName")) {
                String lastName = getArguments().getString("lastName");
                et_name.setText(lastName);
            }
            if (getArguments().containsKey("profileUrl")) {
                String profileUrl = getArguments().getString("profileUrl");
                MyApplication.instance.getPicasso().load(profileUrl).into(imageTarget);
                btn_photo.setVisibility(View.INVISIBLE);
            }
        }

        int position = ((MainActivity) getActivity()).getViewpager().getCurrentItem();
        background = ((MainActivity) getActivity()).getImage(position);
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
             /*   Bitmap blurred = Blur.blurRenderScript(bitmap, 25, getActivity());//second parametre is radius
                iv_background.setImageBitmap(blurred);*/
                Blurry.with(getActivity()).radius(25).sampling(2).from(bitmap).into(iv_background);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.e(RegisterFragment.class.getSimpleName(), "Bitmap failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        MyApplication.instance.getPicasso().load(background).into(target);
        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                } else {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
            }
        });
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Util.hideSoftKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });
        et_name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent != null &&
                        (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER ||
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT)) {
                    et_surname.requestFocus();
                    return false;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.btn_next)
    public void next() {
        if (checkData()) {
            scrollView.setVisibility(View.INVISIBLE);
            layout_2.setVisibility(View.VISIBLE);
            isPart2Visibile = true;
            btn_close.setVisibility(View.INVISIBLE);
            btn_photo.setVisibility(View.INVISIBLE);
            btn_close_2.setVisibility(View.VISIBLE);
            shadowTop.setVisibility(View.VISIBLE);
            iv_profile.setVisibility(View.INVISIBLE);
            corporateCode = etCodeCorporate.getText().toString();
            presenter.setCorporateCode(corporateCode);
        }
    }

    @OnClick(R.id.btn_register)
    public void register() {
        if (mall_selected != 0 && presenter != null) {
            Util.reinitializeShowTutorialOfCamera(getActivity());
            showProgress();
            if (Session.getMalls() == null || Session.getMalls().size() == 0) {
                presenter.getMalls();
            } else {
                String token = ComplexPreferences.getComplexPreferences(getActivity(), Constants.PREFFERENCES, Context.MODE_PRIVATE).getObject("token", String.class);
                presenter.register(false, password, surname, name, email, mall_selected, token, phoneNo);
            }
        } else
            Toast.makeText(getActivity(), "Alegeți un mall vă rog", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.layout_mall_promenada)
    public void clickOnMallPromenada() {
        changeBackgroundSelected(l_mall_promenada);
        changeBackgroundTransparent(l_mega_mall);
        iv_promenada.setVisibility(View.VISIBLE);
        iv_megamall.setVisibility(View.INVISIBLE);
        mall_selected = 9;
    }

    @OnClick(R.id.layout_mega_mall)
    public void clickOnMegaMall() {
        changeBackgroundSelected(l_mega_mall);
        changeBackgroundTransparent(l_mall_promenada);
        iv_promenada.setVisibility(View.INVISIBLE);
        iv_megamall.setVisibility(View.VISIBLE);
        mall_selected = 8;
    }

    @OnClick(R.id.btn_close)
    public void close() {
        getActivity().getSupportFragmentManager().popBackStack();
        Util.hideSoftKeyboard(getActivity());
    }

    @OnClick(R.id.btn_close_2)
    public void back() {
        scrollView.setVisibility(View.VISIBLE);
        layout_2.setVisibility(View.INVISIBLE);
        isPart2Visibile = false;
        btn_close.setVisibility(View.VISIBLE);
        //btn_photo.setVisibility(View.VISIBLE);
        btn_close_2.setVisibility(View.INVISIBLE);
        shadowTop.setVisibility(View.INVISIBLE);
        iv_profile.setVisibility(View.VISIBLE);
        Util.hideSoftKeyboard(getActivity());
    }


    @OnClick(R.id.btn_upload_photo)
    public void uploadPhoto() {
        if (askForPermssions())
            dispatchPickPictureIntent();
    }

    @OnClick(R.id.iv_profile)
    public void uploadPhotoIV() {
        if (askForPermssions())
            dispatchPickPictureIntent();
    }

    @OnClick(R.id.tv_tap_to_login)
    public void tapToLogIn() {
        ((MainActivity) getActivity()).changeFragment(LoginFragment.newInstance(((MainActivity) getActivity())));
    }

    @OnClick(R.id.tv_terms)
    public void termsAndConditions() {
        String link = "http://aha.aroundwise.com/terms/";
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browseIntent);
    }

    @OnClick(R.id.tv_politica)
    public void politicaDeConfidentialitate() {
        String link = "http://aha.aroundwise.com/confidentialitate/";
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browseIntent);
    }


    private void changeBackgroundTransparent(View view) {
        view.setBackground(ContextCompat.getDrawable(getActivity(), android.R.color.transparent));
    }

    private void changeBackgroundSelected(View view) {
        view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.overlay_choose_mall));
    }


    private void dispatchPickPictureIntent() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                Constants.REQUEST_IMAGE_PIC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_IMAGE_PIC && resultCode == getActivity().RESULT_OK) {
            presenter.setPicFromGallery(data);
            btn_photo.setVisibility(View.INVISIBLE);
        }
    }

    private boolean askForPermssions() {
        int readPerm = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readPerm != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ_STORAGE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && requestCode == Constants.PERMISSION_READ_STORAGE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            dispatchPickPictureIntent();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.createCompositeSubscription();
        ((MainActivity) getActivity()).ivLogo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setProfileImage(Uri image) {
        MyApplication.instance.getPicasso().load(image).fit().into(iv_profile);
    }

    @Override
    public int getImageViewWidth() {
        return iv_profile.getWidth();
    }

    @Override
    public int getImageViewHeight() {
        return iv_profile.getHeight();
    }

    private boolean checkData() {
        if ((presenter.getImagePhoto() == null || presenter.getImagePhoto().equals(""))) {
            Toast.makeText(getActivity(), "Poza de profil este obligatorie. Vă rugăm încărcați o poza.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (presenter.checkEditTextEmpty(et_name))
            name = et_name.getText().toString();
        else {
            scrollView.smoothScrollTo(0, et_name.getBottom());
            return false;
        }
        if (presenter.checkEditTextEmpty(et_surname))
            surname = et_surname.getText().toString();
        else {
            scrollView.smoothScrollTo(0, et_surname.getBottom());
            return false;
        }
        if (presenter.checkEditTextEmpty(et_email) && presenter.isEmailValid(et_email))
            email = et_email.getText().toString();
        else {
            scrollView.smoothScrollTo(0, et_email.getBottom());
            return false;
        }
        if (presenter.checkPhoneNumber(et_phone)) {
            phoneNo = et_phone.getText().toString();
        } else {
            scrollView.smoothScrollTo(0, et_phone.getBottom());
            return false;
        }
        if (presenter.checkPassword(et_password.getText().toString()))
            password = et_password.getText().toString();
        else {
            scrollView.smoothScrollTo(0, et_password.getBottom());
            return false;
        }
        if (!cbLegit.isChecked()) {
            cbLegit.setError("Obligatoriu");
            scrollView.smoothScrollTo(0, cbLegit.getBottom());
            return false;
        } else
            cbLegit.setError(null);
        if (!cbTerms.isChecked()) {
            cbTerms.setError("Obligatoriu");
            scrollView.smoothScrollTo(0, cbTerms.getBottom());
            return false;
        } else cbTerms.setError(null);
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeSubscriptions();
    }

    @Override
    public void serverError(String message) {
        Util.createSnackbar(getCoordinatorLayout(), message);
        hideProgress();
    }

    @Override
    public void showAlertError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Atenție")
                .setMessage(error)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    @Override
    public void hideProgressBar() {
        hideProgress();
    }

    @Override
    public void showProgressBar() {
        showProgress();
    }

    @Override
    public void registerAfterMallsDownloaded() {
        String token = ComplexPreferences.getComplexPreferences(getActivity(), Constants.PREFFERENCES, Context.MODE_PRIVATE).getObject("token", String.class);
        presenter.register(false, password, surname, name, email, mall_selected, token, phoneNo);
    }
}
