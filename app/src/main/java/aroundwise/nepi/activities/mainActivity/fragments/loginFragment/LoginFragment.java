package aroundwise.nepi.activities.mainActivity.fragments.loginFragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.mainActivity.MainActivity;
import aroundwise.nepi.activities.mainActivity.fragments.registerFragment.RegisterFragment;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.fragmentInterfaces.ChangeFragmentInterface;
import aroundwise.nepi.network.api.LoginClient;
import aroundwise.nepi.network.api.RegisterClient;
import aroundwise.nepi.network.requests.LoginRequest;
import aroundwise.nepi.network.responses.LoginResponse;
import aroundwise.nepi.network.responses.MallResponse;
import aroundwise.nepi.network.responses.UserResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Blur;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class LoginFragment extends BaseFragment {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.iv_close_button)
    ImageView iv_close_button;

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.iv_background)
    ImageView iv_background;

    Target target;
    Integer background;

    CompositeSubscription compositeSubscription;

    public static LoginFragment newInstance(ChangeFragmentInterface changeFragmentInterface) {
        LoginFragment fragment = new LoginFragment();
        fragment.setChangeFragmentInterface(changeFragmentInterface);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void onResume() {
        super.onResume();
        createCompositeSubscription();
        ((MainActivity) getActivity()).ivLogo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        removeSubscriptions();
    }

    @Override
    protected void setUpViews() {
        createCompositeSubscription();
        ((MainActivity) getActivity()).ivLogo.setVisibility(View.VISIBLE);
        int position = ((MainActivity) getActivity()).getViewpager().getCurrentItem();
        background = ((MainActivity) getActivity()).getImage(position);
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
          /*      Bitmap blurred = Blur.blurRenderScript(bitmap, 25, getActivity());//second parametre is radius
                iv_background.setImageBitmap(blurred)*/
                ;
                Blurry.with(getActivity()).radius(25).sampling(2).from(bitmap).into(iv_background);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d(LoginFragment.class.getSimpleName(), "Bitmap failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        MyApplication.instance.getPicasso().load(background).into(target);
    }

    @Override
    protected ChangeFragmentInterface getChangeFragmentInterface() {
        return super.getChangeFragmentInterface();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (checkPermissions(getActivity())) {
            Util.showProgressBar((MainActivity) getActivity());
            if (Session.getMalls() == null || Session.getMalls().size() == 0) {
                getMalls();
            } else {
                doLoginRequest();
            }
        }
    }

    @OnClick(R.id.tv_forgotpassword)
    public void forgotPass() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://megamallbucuresti.ro/login/"));
        startActivity(intent);
    }


    @OnClick(R.id.iv_close_button)
    public void back() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @OnClick(R.id.tv_create_account)
    public void createAccount() {
        changeFragmentInterface.changeFragment(RegisterFragment.newInstance());
    }

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


    private void loginIntent() {
        Intent intent = new Intent(getActivity(), DiscoverActivity.class);
        Bundle data = ((MainActivity) getActivity()).getData();
        if (data != null)
            intent.putExtra("bundle", data);
        startActivity(intent);
        getActivity().finish();
    }

    private void reinitializeShowTutorialOfCamera() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), Constants.PREFFERENCES, getActivity().MODE_PRIVATE);
        complexPreferences.putObject(Constants.SHOW_TUTORIAL, true);
    }

    private void doLoginRequest() {
        LoginClient client = ServiceGenerator.getServiceGenerator().createService(LoginClient.class);
        final String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String token = ComplexPreferences.getComplexPreferences(getActivity(), Constants.PREFFERENCES, Context.MODE_PRIVATE).getObject("token", String.class);
        compositeSubscription.add(client.login(new LoginRequest(token, email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onCompleted() {
                        getUserDetails();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.hideProgressBar((MainActivity) getActivity());
                        Util.createSnackbar(coordinatorLayout, "Nu s-a putut face conexiunea.\n Verificati conexiunea la net si datele introduse.");
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        Log.d(LoginFragment.class.getSimpleName(), loginResponse.toString());
                        if (loginResponse.getStatus() == 200) {
                            Session.getUser().setPhone(email);
                            Session.getUser().setSessionId(loginResponse.getSessionId());
                            Session.getUser().setUserProfileId(loginResponse.getUserProfileId());
                            Session.getUser().setId(loginResponse.getUserId());
                        }
                        if (loginResponse.getStatus() == 473) {
                            Util.createSnackbar(coordinatorLayout, "Combinația de număr și parolă nu este validă.");
                        }
                    }
                }));


    }

    private boolean checkPermissions(Context context) {
        String coarse = Manifest.permission.ACCESS_COARSE_LOCATION;
        String fine = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(context, coarse) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, coarse) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{coarse, fine}, Constants.PERMISSION_LOCATION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_LOCATION) {
            createCompositeSubscription();
            Util.showProgressBar((MainActivity) getActivity());
            if (Session.getMalls() == null || Session.getMalls().size() == 0) {
                getMalls();
            } else {
                doLoginRequest();
            }
        }

    }

    public void getUserDetails() {
        LoginClient loginClient = ServiceGenerator.getServiceGenerator().createService(LoginClient.class);
        compositeSubscription.add(loginClient.userDetails(Session.getUser().getId(), Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onCompleted() {
                        Session.saveUserToSharedPref(getActivity());
                        Util.hideProgressBar((MainActivity) getActivity());
                        reinitializeShowTutorialOfCamera();
                        loginIntent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LoginFragment.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        Session.getUser().setUserResponse(userResponse);
                    }
                }));
    }

    private void getMalls() {
        RegisterClient client = ServiceGenerator.getServiceGenerator().createService(RegisterClient.class);
        compositeSubscription.add(client.getMalls(10, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallResponse>() {
                    @Override
                    public void onCompleted() {
                        doLoginRequest();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.hideProgressBar((MainActivity) getActivity());
                        Util.createSnackbar(coordinatorLayout, "Nu s-a putut face conexiunea.\nVerificati conexiunea la net si datele introduse.");
                        Log.d(MainActivity.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(MallResponse mallResponse) {
                        Session.setMalls(mallResponse.getMalls());
                    }
                }));
    }
}
