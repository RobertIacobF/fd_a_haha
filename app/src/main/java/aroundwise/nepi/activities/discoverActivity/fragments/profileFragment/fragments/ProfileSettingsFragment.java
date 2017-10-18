package aroundwise.nepi.activities.discoverActivity.fragments.profileFragment.fragments;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.mainActivity.MainActivity;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.dialogs.ChangePasswordDialog;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;
import butterknife.OnClick;


public class ProfileSettingsFragment extends BaseMvpFragment<ProfileSettingsView, ProfileSettingsPresenter>
        implements ProfileSettingsView {

    @BindView(R.id.et_datepicker)
    EditText etDatePicker;

    @BindView(R.id.btn_male)
    Button btn_male;

    @BindView(R.id.btn_female)
    Button btn_female;

    @BindView(R.id.actionbar)
    CustomActionBar actionbar;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.tv_first_name)
    EditText et_first_name;

    @BindView(R.id.tv_last_name)
    EditText et_last_name;

    @BindView(R.id.tv_email)
    EditText et_email;

    @BindView(R.id.et_coupon)
    EditText etCoupon;

    @BindView(R.id.et_phone)
    EditText etPhone;

    private DatePickerDialog datePicker;
    private boolean modifiedMade = false;
    String gender;
    private SimpleDateFormat dateFormatter;
    private TextWatcher textWatcher;

    public static ProfileSettingsFragment newInstance() {
        ProfileSettingsFragment fragment = new ProfileSettingsFragment();
        return fragment;
    }


    public ProfileSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public ProfileSettingsPresenter createPresenter() {
        return new ProfileSettingsPresenter();
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_profile_settings;
    }

    @Override
    protected void setUpViews() {
        setup();
        dateFormatter = new SimpleDateFormat("yyy-MM-dd");
        actionbar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideSoftKeyboard(getActivity());
                if (modifiedMade) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error")
                            .setTitle("Ai făcut modificări și nu ai salvat. Ești sigur?")
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((DiscoverActivity) getActivity()).popFragment();
                                }
                            })
                            .setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                } else
                    ((DiscoverActivity) getActivity()).popFragment();
            }
        });
        actionbar.getOkButton().setText("Salvează");
        actionbar.setOnOkbtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar date = Calendar.getInstance();
                date.set(i, i1, i2);
                etDatePicker.setText(dateFormatter.format(date.getTime()));
                modifiedMade = true;
            }
        }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        etDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });
        etDatePicker.setInputType(InputType.TYPE_NULL);
        etDatePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    if (!datePicker.isShowing()) {
                        Util.hideSoftKeyboard(getActivity());
                        datePicker.show();
                    }
            }
        });
        if (Session.getUser().getProfile().getGender() != null) {
            if (Session.getUser().getProfile().getGender().equalsIgnoreCase(Constants.MALE)) {
                clickMale();
                modifiedMade = false;
            } else if (Session.getUser().getProfile().getGender().equalsIgnoreCase(Constants.FEMALE)) {
                clickFemale();
                modifiedMade = false;
            }
        } else {
            clickMale();
        }
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                modifiedMade = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etPhone.addTextChangedListener(textWatcher);
        et_email.addTextChangedListener(textWatcher);
        et_last_name.addTextChangedListener(textWatcher);
        et_first_name.addTextChangedListener(textWatcher);
        etCoupon.addTextChangedListener(textWatcher);

    }

    @OnClick(R.id.btn_adauga)
    public void adaugaCodCorporate() {
        String coupon = etCoupon.getText().toString();
        presenter.updateCouponCode(coupon);
    }

    @OnClick(R.id.btn_ch_passowrd)
    public void chPassword() {
        ChangePasswordDialog dialog = new ChangePasswordDialog();
        dialog.setCoordinatorLayout(getCoordinatorLayout());
        dialog.show(getActivity().getSupportFragmentManager(), "tag");
    }

    @OnClick(R.id.btn_male)
    public void clickMale() {
        btn_male.setSelected(true);
        btn_male.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        btn_female.setSelected(false);
        btn_female.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        gender = Constants.MALE;
        modifiedMade = true;
    }

    @OnClick(R.id.btn_female)
    public void clickFemale() {
        btn_female.setSelected(true);
        btn_female.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        btn_male.setSelected(false);
        btn_male.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        gender = Constants.FEMALE;
        modifiedMade = true;
    }

    @OnClick(R.id.btn_retake)
    public void retake() {
        if (askForPermssions())
            dispatchPickPictureIntent();
    }

    @OnClick(R.id.btn_log_out)
    public void logOut() {
        Session.logOut(getActivity());
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    //TODO check whick fields were changed
    @OnClick(R.id.btn_next)
    public void saveChanges() {
        showProgress();
        Util.hideSoftKeyboard(getActivity());
        String firstName = et_first_name.getText().toString();
        String lastName = et_last_name.getText().toString();
        String coupon = etCoupon.getText().toString();
        String date = etDatePicker.getText().toString();
        String phoneNumber = etPhone.getText().toString();
        if (date.equals(""))
            date = null;
        presenter.updateCouponCode(coupon, firstName, lastName, gender, date, phoneNumber);

    }

    @OnClick(R.id.tv_politica)
    public void termsAndConditions() {
        String link = "http://aha.aroundwise.com/terms/";
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browseIntent);
    }

    @OnClick(R.id.tv_terms_and_coniditions)
    public void politicaDeConfidentialitate() {
        String link = "http://aha.aroundwise.com/confidentialitate/";
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browseIntent);
    }

    @Override
    public void displaySnackbarMessage(String message) {
        hideProgress();
        Util.createSnackbar(getCoordinatorLayout(), message);
    }

    @Override
    public void finishActivity() {
        Session.saveUserToSharedPref(getActivity());
        ((DiscoverActivity) getActivity()).popFragment();
    }

    @Override
    public void hideProgressView() {
        hideProgress();
    }

    @Override
    public void showProgressBar() {
        showProgress();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_IMAGE_PIC && resultCode == getActivity().RESULT_OK) {
            modifiedMade = true;
            Uri selectedImage = data.getData();
            MyApplication.instance.getPicasso().load(selectedImage).fit().centerCrop().into(iv_avatar);
            presenter.createBase64(data, getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_READ_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchPickPictureIntent();
        }
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

    private boolean askForPermssions() {
        int readPerm = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readPerm != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ_STORAGE);
            return false;
        }
        return true;
    }

    private void setup() {
        if (Session.getUser().getProfile().getPhoneNumber() != null)
            etPhone.setText(Session.getUser().getProfile().getPhoneNumber());
        et_email.setText(Session.getUser().getPhone());
        et_last_name.setText(Session.getUser().getLast_name());
        et_first_name.setText(Session.getUser().getFirst_name());
        etDatePicker.setText(Session.getUser().getProfile().getBirthdate());
        if (!Session.getUser().getProfile().getCorporateCode().equals("false"))
            etCoupon.setText(Session.getUser().getProfile().getCorporateCode());
        if (Session.getUser().getProfile() != null)
            MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getImage()).fit().into(iv_avatar);
    }


}
