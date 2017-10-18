package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.getPointsFragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.view.View;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.cameraActivity.CameraActivity;
import aroundwise.nepi.activities.cameraActivity.CameraActivity2;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class PurchaseFragment extends BaseFragment {


    @BindView(R.id.actionbar)
    CustomActionBar actionBar;
    final int REQUEST = 100;


    public static PurchaseFragment newInstance() {
        PurchaseFragment purchaseFragment = new PurchaseFragment();
        return purchaseFragment;
    }

    public PurchaseFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_purchase;
    }

    @Override
    protected void setUpViews() {
        actionBar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).popFragment();
            }
        });
    }

    @OnClick(R.id.btn_scan_receipt)
    public void seeStores() {
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
