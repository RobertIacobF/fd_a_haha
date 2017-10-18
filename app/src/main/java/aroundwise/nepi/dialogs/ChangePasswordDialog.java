package aroundwise.nepi.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import aroundwise.nepi.R;
import aroundwise.nepi.network.api.UserClient;
import aroundwise.nepi.network.requests.ChangePasswordRequest;
import aroundwise.nepi.network.responses.ChangePasswordResponse;
import aroundwise.nepi.network.serviceGenerator.ServiceGenerator;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.Util;
import aroundwise.nepi.util.views.SAutoBgButton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mihai on 07/09/16.
 */
public class ChangePasswordDialog extends DialogFragment {

    EditText etOldPassword;
    EditText etNewPassword;
    CoordinatorLayout coordinatorLayout;

    public void setCoordinatorLayout(CoordinatorLayout coordinatorLayout) {
        this.coordinatorLayout = coordinatorLayout;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);
        SAutoBgButton btn_close = (SAutoBgButton) dialogView.findViewById(R.id.btn_close_dialog);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordDialog.this.dismiss();
            }
        });
        SAutoBgButton btnChPassword = (SAutoBgButton) dialogView.findViewById(R.id.btn_ch_passowrd);
        btnChPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfEditTextEmpty(etOldPassword)
                        && checkIfEditTextEmpty(etNewPassword)) {
                    Util.hideSoftKeyboard(getActivity());
                    changePasswordCall(etOldPassword.getText().toString(),
                            etNewPassword.getText().toString());
                }
            }
        });

        etOldPassword = (EditText) dialogView.findViewById(R.id.tv_password);
        etNewPassword = (EditText) dialogView.findViewById(R.id.tv_confirm);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private boolean checkIfEditTextEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Field must not be empty");
            return false;
        } else
            editText.setError(null);
        return true;
    }

    private void changePasswordCall(String oldPassword, String newPassword) {
        UserClient userClient = ServiceGenerator.getServiceGenerator().createService(UserClient.class);
        userClient.changePassword(new ChangePasswordRequest(oldPassword, newPassword, newPassword),
                Constants.SESSIONID + Session.getUser().getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChangePasswordResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ChangePasswordDialog.class.getSimpleName(), e.getMessage());
                        if (coordinatorLayout != null)
                            Util.createSnackbar(coordinatorLayout, "A apărut o eroare. Vă rugăm încercați din nou!");
                    }

                    @Override
                    public void onNext(ChangePasswordResponse changePasswordResponse) {
                        if (changePasswordResponse.getStatus() == 200)
                            if (coordinatorLayout != null)
                                Util.createSnackbar(coordinatorLayout, "Parola a fost schimbată cu success");
                    }
                });
    }

}
