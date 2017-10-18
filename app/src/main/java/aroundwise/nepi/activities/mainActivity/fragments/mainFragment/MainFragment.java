package aroundwise.nepi.activities.mainActivity.fragments.mainFragment;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.mainActivity.MainActivity;
import aroundwise.nepi.activities.mainActivity.fragments.loginFragment.LoginFragment;
import aroundwise.nepi.activities.mainActivity.fragments.registerFragment.RegisterFragment;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.fragmentInterfaces.ChangeFragmentInterface;
import aroundwise.nepi.network.api.LoginClient;
import aroundwise.nepi.network.requests.FacebookLoginRequest;
import aroundwise.nepi.network.responses.BasicResponse;
import aroundwise.nepi.network.responses.LoginResponse;
import aroundwise.nepi.network.responses.UserResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.ComplexPreferences;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.pdf_viewer)
    PDFView pdfViewer;

    @BindView(R.id.rl_pdf)
    RelativeLayout rlPdf;

    @BindView(R.id.fl_close_button_pdf)
    FrameLayout flCloseButtonPdf;

    @BindView(R.id.btn_close_pdf)
    Button btnClosePdf;


    @BindView(R.id.btn_facebook_sdk)
    LoginButton btn_facebook;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    CallbackManager callbackManager;
    String email, id, lastName, firstName, profileUrl;

    CompositeSubscription compositeSubscription;

    public static MainFragment newInstance(ChangeFragmentInterface changeFragmentInterface) {
        MainFragment fragment = new MainFragment();
        fragment.setChangeFragmentInterface(changeFragmentInterface);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void setUpViews() {
        compositeSubscription = new CompositeSubscription();
        callbackManager = CallbackManager.Factory.create();
        btn_facebook.setReadPermissions("email");
        btn_facebook.setFragment(this);
        btn_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(MainFragment.class.getSimpleName(), loginResult.getAccessToken().toString());
                getInfosFromFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Util.createSnackbar(getCoordinatorLayout(), "Login has been canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Util.createSnackbar(getCoordinatorLayout(), error.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed())
            compositeSubscription = new CompositeSubscription();
        if (AccessToken.getCurrentAccessToken() != null)
            LoginManager.getInstance().logOut();
        ((MainActivity) getActivity()).ivLogo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    private void getInfosFromFacebook(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (object != null) {
                                email = object.getString("email");
                                firstName = object.getString("first_name");
                                lastName = object.getString("last_name");
                                id = object.getString("id");
                                profileUrl = "https://graph.facebook.com/" + id + "/picture?type=large";
                                if (compositeSubscription == null || compositeSubscription.isUnsubscribed())
                                    compositeSubscription = new CompositeSubscription();
                                checkIfLoggedInWithFb(accessToken.getToken(), id);
                           /* ((MainActivity) getActivity()).changeFragment(RegisterFragment.
                                    newInstance(email, firstName, lastName, profileUrl));*/
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "A apărut o eroare. Vă rugăm să vă verificați conexiunea la internet și să încercați din nou.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Log.e(MainFragment.class.getSimpleName(), e.getMessage());
                        }
                    }
                });
        Bundle params = new Bundle();
        params.putString("fields", "first_name,last_name,email,id");
        request.setParameters(params);
        request.executeAsync();

    }

    @OnClick(R.id.btn_close_pdf)
    public void closePdf() {
        hidePdfViewer();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (changeFragmentInterface != null) {
            changeFragmentInterface.changeFragment(LoginFragment.newInstance(changeFragmentInterface));
        }
    }

    @OnClick(R.id.btn_facebook)
    public void facebookLogin() {
        progressBar.setVisibility(View.VISIBLE);
        if (AccessToken.getCurrentAccessToken() != null)
            LoginManager.getInstance().logOut();
        btn_facebook.performClick();
    }

    @OnClick(R.id.btn_sign_up)
    public void register() {
        ((MainActivity) getActivity()).changeFragment(RegisterFragment.newInstance());
    }

    @OnClick(R.id.btn_regulament)
    public void termsAndConditions() {
        String link = "http://aha.aroundwise.com/termeni-si-conditii.pdf";
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browseIntent);
        //displayPdf(link);
    }

    @OnClick(R.id.tv_terms_and_coniditions)
    public void politicaDeConfidentialitate() {
        String link = "http://aha.aroundwise.com/politica-de-confidentialitate.pdf";
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browseIntent);
        //displayPdf(link);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void displayPdf(String url) {
        rlPdf.setVisibility(View.VISIBLE);
        ObjectAnimator animatorButtonPdf = ObjectAnimator.ofFloat(flCloseButtonPdf, "translationY", -150f, 0f);
        ObjectAnimator animatorPdfViewer = ObjectAnimator.ofFloat(pdfViewer, "translationY", 1000f, 0f);
        animatorButtonPdf.setDuration(300);
        animatorPdfViewer.setDuration(300);
        animatorButtonPdf.start();
        animatorPdfViewer.start();
        pdfViewer.fromUri(Uri.parse(url)).enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .defaultPage(0)
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Log.e("Error", "error");
                    }
                })
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        Log.e("Completed", "" + nbPages);
                    }
                })
                .load();
    }

    private void hidePdfViewer() {
        ObjectAnimator animatorButtonPdf = ObjectAnimator.ofFloat(flCloseButtonPdf, "translationY", 0f, -150f);
        ObjectAnimator animatorPdfViewer = ObjectAnimator.ofFloat(pdfViewer, "translationY", 0f, 1000f);
        animatorButtonPdf.setDuration(300);
        animatorPdfViewer.setDuration(300);
        animatorButtonPdf.start();
        animatorPdfViewer.start();
        pdfViewer.recycle();
    }

    private void checkIfLoggedInWithFb(String token, String userId) {

        LoginClient loginClient = ServiceGenerator.getServiceGenerator().createService(LoginClient.class);
        compositeSubscription.add(loginClient.facebookLogin(new FacebookLoginRequest(token, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        Log.d(LoginFragment.class.getSimpleName(), loginResponse.toString());
                        if (loginResponse.getStatus() == 200 && loginResponse.getSessionId() != null) {
                            Session.getUser().setPhone(email);
                            Session.getUser().setSessionId(loginResponse.getSessionId());
                            Session.getUser().setUserProfileId(loginResponse.getUserProfileId());
                            Session.getUser().setId(loginResponse.getUserId());
                            getUserDetails();
                        } else {
                            changeFragmentInterface.changeFragment(RegisterFragment.newInstance(email, firstName, lastName, profileUrl));
                        }
                    }
                }));

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
                        Util.reinitializeShowTutorialOfCamera(getContext());
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

    private void loginIntent() {
        Intent intent = new Intent(getActivity(), DiscoverActivity.class);
        Bundle data = ((MainActivity) getActivity()).getData();
        if (data != null)
            intent.putExtra("bundle", data);
        startActivity(intent);
        getActivity().finish();
    }


}
