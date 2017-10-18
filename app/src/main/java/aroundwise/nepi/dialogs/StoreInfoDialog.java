package aroundwise.nepi.dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storeMapFragment.StoreMapFragment;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.LocationUtil;
import aroundwise.nepi.util.views.SAutoBgButton;

/**
 * Created by mihai on 31/08/16.
 */
public class StoreInfoDialog extends DialogFragment {

    Shop store;

    public static StoreInfoDialog newInstance(Shop store) {
        StoreInfoDialog dialog = new StoreInfoDialog();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SHOP, store);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = getArguments().getParcelable(Constants.SHOP);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_store_info, null);
        builder.setView(dialogView);
        SAutoBgButton btn_close = (SAutoBgButton) dialogView.findViewById(R.id.btn_close_dialog);
        SAutoBgButton btn_call = (SAutoBgButton) dialogView.findViewById(R.id.btn_phone_call);
        SAutoBgButton btn_location = (SAutoBgButton) dialogView.findViewById(R.id.btn_location);
        ImageView ivLogo = (ImageView) dialogView.findViewById(R.id.iv_store_logo);
        MyApplication.instance.getPicasso().load(store.getLogo()).into(ivLogo);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreInfoDialog.this.dismiss();
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (askForCallPermission())
                    call();

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).changeFragment(StoreMapFragment.newInstance(store, null));
                StoreInfoDialog.this.dismiss();
            }
        });
        TextView tvStoreName = (TextView) dialogView.findViewById(R.id.tv_store_name);
        tvStoreName.setText(store.getName());
        TextView tvStoreType = (TextView) dialogView.findViewById(R.id.tv_store_type);
        TextView tvStoreDescription = (TextView) dialogView.findViewById(R.id.tv_store_description);
        tvStoreDescription.setText(store.getDescription());
        TextView tvStoreAndMallName = (TextView) dialogView.findViewById(R.id.tv_store_mall);
        tvStoreAndMallName.setText(store.getName() + " " + Session.getMalls().get(0).getName());
        TextView tvStoreAddress = (TextView) dialogView.findViewById(R.id.tv_dialog_loc);
        tvStoreAddress.setText(store.getAddress());
        TextView tvPhone = (TextView) dialogView.findViewById(R.id.tv_dialog_phone);
        tvPhone.setText(store.getPhone());
        final TextView tvWebPage = (TextView) dialogView.findViewById(R.id.tv_dialog_calc);
        tvWebPage.setText(store.storeUrl);
        tvWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = tvWebPage.getText().toString();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        TextView tvKmToStore = (TextView) dialogView.findViewById(R.id.tv_dialog_km);
        if (store.getLcoations().size() > 0) {
            String km = "";
            Location location = new Location("");
            location.setLatitude(store.getLcoations().get(0).getLatitude());
            location.setLongitude(store.getLcoations().get(0).getLongitude());
            if (LocationUtil.getLocationUtil(getActivity()).getLastLocation() != null) {
                double distance = LocationUtil.getLocationUtil(getActivity()).getLastLocation().distanceTo(location);
                distance = distance / 1000;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(1);
                km = df.format(distance) + " " + getActivity().getString(R.string.km_away);
                tvKmToStore.setText(km);
            } else {
                tvKmToStore.setText("");
            }
        }
        tvKmToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).changeFragment(StoreMapFragment.newInstance(store, null));
                StoreInfoDialog.this.dismiss();
            }
        });

        return builder.create();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private boolean askForCallPermission() {
        int phonePerm = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (phonePerm != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, Constants.PERMISSION_CALL_PHONE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_CALL_PHONE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            call();
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + store.getPhone()));
        getActivity().startActivity(intent);
    }

    private String kmToStore(Shop store, Context context) {
        Location location = new Location("");
        location.setLatitude(store.latitude);
        location.setLongitude(store.longitude);
        Location lastLoc = LocationUtil.getLocationUtil(context).getLastLocation();
        if (lastLoc != null) {
            double distance = lastLoc.distanceTo(location);
            distance = distance / 1000;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(1);
            String km = df.format(distance) + " " + context.getString(R.string.km_away);
            return km;
        } else {
            return "";
        }

    }
}
