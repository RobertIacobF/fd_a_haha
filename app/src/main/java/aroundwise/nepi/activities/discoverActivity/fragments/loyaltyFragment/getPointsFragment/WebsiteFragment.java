package aroundwise.nepi.activities.discoverActivity.fragments.loyaltyFragment.getPointsFragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.base.BaseFragment;
import aroundwise.nepi.eventbus.AnswerPollEvent;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class WebsiteFragment extends BaseFragment {

    @BindView(R.id.actionbar)
    CustomActionBar actionBar;


    public static WebsiteFragment newInstance() {
        WebsiteFragment websiteFragment = new WebsiteFragment();
        return websiteFragment;
    }


    public WebsiteFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_website;
    }

    @Override
    protected void setUpViews() {
        actionBar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DiscoverActivity) getActivity()).popFragment();
            }
        });
    }

    @OnClick(R.id.btn_website)
    public void goToWebsite() {
        String link = "http://promenada.ro/login/";
        switch (Session.getUser().getProfile().getMall().getId()) {
            case 8:
                link = "http://megamallbucuresti.ro/login/";
                break;

            case 9:
                link = "http://promenada.ro/login/";
                break;
        }

        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browseIntent);
    }

}
