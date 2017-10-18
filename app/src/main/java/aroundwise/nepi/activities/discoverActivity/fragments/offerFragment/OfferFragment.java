package aroundwise.nepi.activities.discoverActivity.fragments.offerFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.adapters.EmptyAdapter;
import aroundwise.nepi.adapters.OfferAdapter;
import aroundwise.nepi.adapters.OfferTypeAdapter;
import aroundwise.nepi.adapters.OnLoadMoreListener;
import aroundwise.nepi.adapters.SpinnerAdapter;
import aroundwise.nepi.base.BaseMvpFragment;
import aroundwise.nepi.dialogs.FilterDialog;
import aroundwise.nepi.eventbus.BlurBackgroundEvent;
import aroundwise.nepi.eventbus.OfferDetailEvent;
import aroundwise.nepi.network.model.CoverOfferGroup;
import aroundwise.nepi.network.model.Offer;
import aroundwise.nepi.network.model.OfferType;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Blur;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.OnSwipeTouchListener;
import aroundwise.nepi.util.Util;
import aroundwise.nepi.util.views.CustomActionBar;
import aroundwise.nepi.util.views.CustomSpinner;
import butterknife.BindView;
import de.greenrobot.event.EventBus;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by mihai on 29/08/16.
 */
public class OfferFragment extends BaseMvpFragment<OfferFragmentView, OfferFragmentPresenter>
        implements OfferFragmentView {

    //region BindView

    @BindView(R.id.actionbar)
    CustomActionBar actionbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tabs_scroll_view)
    RecyclerView tabs_scroll_view;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.rlBackground)
    RelativeLayout rlBackground;

    //endregion

    private SpinnerAdapter spinnerAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager tabManager;
    private OfferAdapter adapter;
    private OfferTypeAdapter offerTypeAdapter;
    private boolean firstTimeUserSpinner = true;
    private int lastPozReturnedByLayoutManager = -1;
    private OnSwipeTouchListener onSwipeTouchListener;


    public static OfferFragment newInstance() {
        OfferFragment fragment = new OfferFragment();
        return fragment;
    }

    public static OfferFragment newInstance(Offer offer) {
        OfferFragment fragment = new OfferFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.OFFER, offer);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_offer;
    }

    @Override
    protected void setUpViews() {
        showProgress();
        if (getArguments() != null) {
            hideProgress();
            Offer offer = getArguments().getParcelable(Constants.OFFER);
            if (offer != null) {
                getArguments().putParcelable(Constants.OFFER, null);
                EventBus.getDefault().post(new OfferDetailEvent(offer));
            }
        }
        presenter.createCompositeSubscription();
        initializeSpinner();
        Session.setUserMallFirst();
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        actionbar.setBtnFilterOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideSoftKeyboard(getActivity());
                FilterDialog dialog = new FilterDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });

        onSwipeTouchListener = new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                if (offerTypeAdapter != null) {
                    int position = offerTypeAdapter.getPositionSelected();
                    if (position < offerTypeAdapter.getItemCount() - 1) {
                        position++;
                        tabs_scroll_view.findViewHolderForAdapterPosition(position).itemView.performClick();
                        tabs_scroll_view.scrollToPosition(position);
                    }
                }
            }

            @Override
            public void onSwipeRight() {
                if (offerTypeAdapter != null) {
                    int position = offerTypeAdapter.getPositionSelected();
                    if (position > 0) {
                        position--;
                        tabs_scroll_view.findViewHolderForAdapterPosition(position).itemView.performClick();
                        tabs_scroll_view.scrollToPosition(position);

                    }
                }
            }
        };
        recyclerView.setOnTouchListener(onSwipeTouchListener);
    }

    @Override
    public OfferFragmentPresenter createPresenter() {
        return new OfferFragmentPresenter();
    }

    @Override
    public void showOfferTypes(List<OfferType> offerTypeList) {
        offerTypeAdapter = new OfferTypeAdapter(getActivity(), offerTypeList);
        offerTypeAdapter.setPresenter(presenter);
        initializeOfferTypesRecylcer();
    }

    @Override
    public void displayOffers(List<Offer> offers, boolean showSliders) {
        //swipeLayout.setVisibility(View.GONE);
        if ((offers == null || offers.size() == 0) && !offerTypeAdapter.getCurrentOfferType().getName().equalsIgnoreCase("intrebari")) {
            EmptyAdapter adapter = new EmptyAdapter(getActivity(), "Momentan nu sunt " + offerTypeAdapter.getCurrentOfferType().getName().toLowerCase());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setBackgroundColor(Color.TRANSPARENT);
            recyclerView.setAdapter(adapter);
        } else {
            swipeLayout.setRefreshing(false);
            if (adapter == null) {
                adapter = new OfferAdapter(getActivity(), offers);
                if (offerTypeAdapter.getCurrentOfferType().getName().trim().toLowerCase().equals("intrebari")
                        || offerTypeAdapter.getPositionSelected() == 0)
                    adapter.setShowScanner(true);
                else
                    adapter.setShowScanner(false);
                adapter.setShouldShowSecondSlider(showSliders);
                adapter.setShouldShowSliders(showSliders);
                if (offerTypeAdapter.getCurrentOfferType().getName().equalsIgnoreCase("intrebari")) {
                    adapter.setIntrebariShown(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else {
                    adapter.setIntrebariShown(false);
                }
                initializeRecyclerView();
            } else {
                if (offerTypeAdapter.getCurrentOfferType().getName().trim().toLowerCase().equals("intrebari")
                        || offerTypeAdapter.getPositionSelected() == 0)
                    adapter.setShowScanner(true);
                else
                    adapter.setShowScanner(false);
                adapter.setShouldShowSecondSlider(showSliders);
                adapter.setShouldShowSliders(showSliders);
                adapter.setOffers(offers);
                if (offerTypeAdapter.getCurrentOfferType().getName().equalsIgnoreCase("intrebari")) {
                    adapter.setIntrebariShown(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else {
                    adapter.setIntrebariShown(false);
                }
                adapter.notifyDataSetChanged();
            }
        }


        hideProgress();
    }

    @Override
    public void displayData(List<Offer> offers, List<CoverOfferGroup> coverOffers, List<Offer> simpleFeaturedOffers) {
        swipeLayout.setRefreshing(false);
        if (adapter == null) {
            adapter = new OfferAdapter(getActivity(), offers);
            adapter.setCoverOfferGroups(coverOffers);
            adapter.setSecondSliderOffers(simpleFeaturedOffers);
            if (simpleFeaturedOffers != null && simpleFeaturedOffers.size() > 0) {
                adapter.setShouldShowSecondSlider(true);
            } else {
                adapter.setShouldShowSecondSlider(false);
            }
            if (coverOffers != null && coverOffers.size() > 0) {
                adapter.setShouldShowSliders(true);
            } else {
                adapter.setShouldShowSliders(false);
            }
            int lastSimpleOfferGroupSize = presenter.getSimpleOfferGroups().size() - 1;
            adapter.setHeadlineSecondSlider(presenter.getSimpleOfferGroups().get(lastSimpleOfferGroupSize).getGroupHeadline());
            adapter.setSecondHeadlineSecondSlider(presenter.getSimpleOfferGroups().get(lastSimpleOfferGroupSize).getGroupSubHeadline());
            if (offerTypeAdapter == null || offerTypeAdapter.getPositionSelected() == 0) {
                adapter.setShowScanner(true);
            }
            if (offerTypeAdapter != null && offerTypeAdapter.getCurrentOfferType().getName().equalsIgnoreCase("intrebari")) {
                adapter.setIntrebariShown(true);
            } else {
                adapter.setIntrebariShown(false);
            }
            initializeRecyclerView();
        } else {
            if (presenter.offers.get(presenter.offers.size() - 1) == null)
                presenter.offers.remove(presenter.offers.size() - 1);
            adapter.setOffers(offers);
            adapter.setCoverOfferGroups(coverOffers);
            adapter.setSecondSliderOffers(simpleFeaturedOffers);
            if (offerTypeAdapter == null || offerTypeAdapter.getPositionSelected() == 0) {
                adapter.setShowScanner(true);
            }
            if (simpleFeaturedOffers != null && simpleFeaturedOffers.size() > 0) {
                adapter.setShouldShowSecondSlider(true);
            } else {
                adapter.setShouldShowSecondSlider(false);
            }
            if (coverOffers != null && coverOffers.size() > 0) {
                adapter.setShouldShowSliders(true);
            } else {
                adapter.setShouldShowSliders(false);
            }
            if (offerTypeAdapter.getCurrentOfferType().getName().equalsIgnoreCase("intrebari")) {
                adapter.setIntrebariShown(true);
            } else {
                adapter.setIntrebariShown(false);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void displayOffersReceivedFromTypeChanged(List<Offer> offers) {
        adapter = new OfferAdapter(getActivity(), offers);
        adapter.setShouldShowSecondSlider(false);
        adapter.setShouldShowSliders(false);
        adapter.setShowScanner(offerTypeAdapter.getPositionSelected() == 0);
        if (offerTypeAdapter.getCurrentOfferType().getName().equalsIgnoreCase("intrebari")) {
            adapter.setIntrebariShown(true);
        }
        else {
            adapter.setIntrebariShown(false);
        }
        initializeRecyclerView();

    }

    @Override
    public void displayFeatureOffers(List<Offer> offers) {
        if (adapter != null) {
            adapter.setSecondSliderOffers(offers);
            adapter.setShouldShowSecondSlider(true);
            adapter.setShouldShowSliders(true);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProgressBar() {
        showProgress();
    }

    @Override
    public void hideProgressBar() {
        swipeLayout.setRefreshing(false);
        hideProgress();
    }

    @Override
    public void refreshOfferAdapter() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshDataWhenMallIsChanged() {
        swipeLayout.setRefreshing(true);
        refreshMallChangedList();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.createCompositeSubscription();
        OfferPresenter.getUserFavoriteOffers();
        if (adapter != null && adapter.getItemCount() != 0) {
            initializeRecyclerView();
            if (adapter.getPositionSelected() != -1
                    && gridLayoutManager != null)
                gridLayoutManager.scrollToPosition(adapter.getPositionSelected());
            adapter.notifyDataSetChanged();
            hideProgress();
        } else {
            presenter.getAllData();
        }
        if (offerTypeAdapter != null && offerTypeAdapter.getItemCount() != 0) {
            initializeOfferTypesRecylcer();
            tabManager.scrollToPosition(offerTypeAdapter.getPositionSelected());
            offerTypeAdapter.notifyDataSetChanged();
        } else
            presenter.getOffersType();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgress();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeSubscriptions();
    }

    private void initializeSpinner() {
        spinnerAdapter = new SpinnerAdapter(getActivity(), Session.getMalls(), getContext().getString(R.string.browse));
        actionbar.getSpinner().setAdapter(spinnerAdapter);
        actionbar.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (firstTimeUserSpinner)
                    firstTimeUserSpinner = false;
                else {
                    if (i != 0) {
                        presenter.changeMall(Session.getMalls().get(i));
                        if (offerTypeAdapter != null && offerTypeAdapter.getCurrentOfferType() != null)
                            if (offerTypeAdapter.getPositionSelected() == 0)
                                presenter.getAllData();
                            else
                                presenter.getOffersByType(offerTypeAdapter.getCurrentOfferType());
                        else
                            presenter.getAllData();
                        Session.switchMalls(i);
                    }
                    //spinnerAdapter.notifyDataSetChanged();
                    actionbar.getSpinner().setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        actionbar.getSpinner().setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                EventBus.getDefault().post(new BlurBackgroundEvent(true));
            }

            @Override
            public void onSpinnerClosed() {
                EventBus.getDefault().post(new BlurBackgroundEvent(false));
            }
        });
    }


    private void refreshList() {
        //swipeLayout.setVisibility(View.VISIBLE);
        if (offerTypeAdapter != null) {
            if (offerTypeAdapter.getPositionSelected() == 0) {
                presenter.getAllData();
            } else
                presenter.refreshList(offerTypeAdapter.getCurrentOfferType(), 0, 10);
        }
    }

    private void refreshMallChangedList() {
        //swipeLayout.setVisibility(View.VISIBLE);
        if (offerTypeAdapter != null)
            if (offerTypeAdapter.getPositionSelected() == 0) {
                presenter.getAllData();
            } else {
                presenter.getOffersByType(offerTypeAdapter.getCurrentOfferType());
            }

    }

    private void initializeRecyclerView() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int poz1 = adapter.isShouldShowSecondSlider() ? 1 : 0;
                int poz2 = adapter.isShouldShowSliders() ? 1 : 0;
                int poz3 = adapter.isShowScanner() ? 1 : 0;
                int pozMod = poz1 + poz2 + poz3;
                if (position == 0) {
                    lastPozReturnedByLayoutManager = 4;
                    return 4;
                }
                if (position == 1 && poz3 == 1) {
                    lastPozReturnedByLayoutManager = 4;
                    return 4;
                }
                if (position == 2 && (adapter.isShouldShowSecondSlider() ||
                        adapter.isShouldShowSliders())) {
                    lastPozReturnedByLayoutManager = 4;
                    return 4;
                }
                if (position == 3 && (adapter.isShouldShowSecondSlider() &&
                        adapter.isShouldShowSliders())) {
                    lastPozReturnedByLayoutManager = 4;
                    return 4;
                }
                if (lastPozReturnedByLayoutManager == 4
                        && (adapter.getItemCount() - 1) == position) {
                    lastPozReturnedByLayoutManager = 4;
                    return 4;
                }
                if ((position - pozMod) % 3 == 0) {
                    lastPozReturnedByLayoutManager = 4;
                    return 4;
                }
                if ((position - pozMod) % 3 == 1
                        && position == adapter.getItemCount() - 1) {
                    lastPozReturnedByLayoutManager = 4;
                    return 4;
                }
                lastPozReturnedByLayoutManager = 2;
                return 2;
            }
        });
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                int offset = presenter.getOffersList().size();
                presenter.getOffersList().add(null);
                adapter.notifyDataSetChanged();

                if (offerTypeAdapter == null)
                    presenter.requestOffers(offset, 10);
                else
                    presenter.getOffersByType(offerTypeAdapter.getCurrentOfferType(), offset, 10);
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setBackgroundColor(Color.TRANSPARENT);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisible = gridLayoutManager.findLastVisibleItemPosition();
                if (lastVisible != -1
                        && lastVisible == presenter.getOffersList().size() - 1
                        && lastVisible < presenter.getOffersList().size()
                        && presenter.getOffersList().get(lastVisible) != null
                        && presenter.getOffersMeta().next != null
                        && presenter.getOffersMeta().totalCount > presenter.getOffersMeta().offset) {
                    if (adapter.onLoadMoreListener != null)
                        adapter.onLoadMoreListener.onLoadMore();
                }
            }
        });

    }

    private void initializeOfferTypesRecylcer() {
        tabManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        tabs_scroll_view.setLayoutManager(tabManager);
        tabs_scroll_view.setItemAnimator(new DefaultItemAnimator());
        tabs_scroll_view.setAdapter(offerTypeAdapter);
    }
}
