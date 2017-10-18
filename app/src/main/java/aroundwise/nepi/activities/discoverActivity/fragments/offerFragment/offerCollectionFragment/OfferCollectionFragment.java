package aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.offerCollectionFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferPresenter;
import aroundwise.nepi.adapters.CollectionOfferAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.util.views.CustomActionBar;
import butterknife.BindView;


public class OfferCollectionFragment extends BaseMvpFragment<OfferCollectionView, OfferCollectionPresenter>
        implements OfferCollectionView {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.actionbar)
    CustomActionBar actionBar;

    CollectionOfferAdapter adapter;

    int coverId;
    String title;
    boolean simpleOffers;

    public OfferCollectionFragment() {
        // Required empty public constructor
    }

    public static OfferCollectionFragment newInstance(int id, String title, boolean simpleOffers) {
        OfferCollectionFragment fragment = new OfferCollectionFragment();
        Bundle args = new Bundle();
        args.putInt("cover_id", id);
        args.putString("title", title);
        args.putBoolean("simple_offers", simpleOffers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleOffers = getArguments().getBoolean("simple_offers", false);
        title = getArguments().getString("title");
        if (!simpleOffers) {
            coverId = getArguments().getInt("cover_id");
        }

    }

    @Override
    public OfferCollectionPresenter createPresenter() {
        return new OfferCollectionPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_offer_collection;
    }

    @Override
    protected void setUpViews() {
        if (simpleOffers) {
            presenter.getSimpleOffers();
        } else {
            presenter.getCoverOfferGroupOffers(coverId);
        }
        actionBar.setActionBarTitle(title);
        actionBar.setTitleTextSize(12);
        actionBar.setBtnBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiscoverActivity) getActivity()).popFragment();
            }
        });
    }

    @Override
    public void displayOffers(List<Offer> offers) {
        OfferPresenter.setLikedOffers(offers);
        adapter = new CollectionOfferAdapter(getContext(), offers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void hideProgressBar() {
        hideProgress();
    }

    @Override
    public void showProgressBar() {
        showProgress();
    }
}
