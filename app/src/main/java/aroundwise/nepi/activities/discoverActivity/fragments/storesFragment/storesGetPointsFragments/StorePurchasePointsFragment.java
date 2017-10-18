package aroundwise.nepi.activities.discoverActivity.fragments.storesFragment.storesGetPointsFragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.activities.cameraActivity.CameraActivity;
import aroundwise.nepi.activities.cameraActivity.CameraActivity2;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;
import butterknife.OnClick;


public class StorePurchasePointsFragment extends BaseFragment {

    @BindView(R.id.actionbar)
    CustomActionBar actionBar;

    @BindView(R.id.tv_spend_points_number)
    TextView tvSpendPointsNumber;

    @BindView(R.id.tv_get_points)
    TextView tvGetPoints;

    @BindView(R.id.iv_store_logo)
    ImageView ivStoreLogo;

    Shop store;

    final int REQUEST = 100;

    public static StorePurchasePointsFragment newInstance(Shop store) {
        StorePurchasePointsFragment fragment = new StorePurchasePointsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SHOP, store);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = getArguments().getParcelable(Constants.SHOP);
    }

    public StorePurchasePointsFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_store_purchase_points;
    }

    @Override
    protected void setUpViews() {
        actionBar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).popFragment();
            }
        });
        tvGetPoints.setText(String.valueOf(store.getPointsPerBuy()) + "%");
        MyApplication.instance.getPicasso().load(store.getLogo()).into(ivStoreLogo);
    }

    @OnClick(R.id.btn_scan_receipt)
    public void scanreceipt() {
        if (askForPermissions()) {
            Intent intent = new Intent(getActivity(), CameraActivity2.class);
            intent.putExtra(Constants.SHOW_TUTORIAL, true);
            startActivity(intent);
        }
    }

    private boolean askForPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST);
            return false;
        }
        return true;
    }
}
