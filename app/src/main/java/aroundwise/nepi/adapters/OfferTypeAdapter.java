package aroundwise.nepi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.fragments.offerFragment.OfferFragmentPresenter;
import aroundwise.nepi.network.model.OfferType;

/**
 * Created by mihai on 12/09/16.
 */
public class OfferTypeAdapter extends SupportAnnotatedAdapter implements OfferTypeAdapterBinder {


    @ViewType(
            layout = R.layout.item_offer_type,
            views =
                    {
                            @ViewField(id = R.id.tv_tab, type = TextView.class, name = "tv_tab"),
                            @ViewField(id = R.id.tab_line, type = View.class, name = "tab_line"),
                            @ViewField(id = R.id.btn_tab, type = Button.class, name = "btn_tab")
                    }
    )
    public static final int offer_type = 0;


    List<OfferType> offerTypes;
    Context context;
    int positionSelected = 0;
    OfferFragmentPresenter presenter;
    OfferType currentOfferType;

    public int getPositionSelected() {
        return positionSelected;
    }

    public OfferType getCurrentOfferType() {
        return currentOfferType;
    }

    public OfferTypeAdapter(Context context, List<OfferType> offerTypes) {
        super(context);
        this.offerTypes = offerTypes;
        this.context = context;
        currentOfferType = offerTypes.get(0);
    }

    public void setPresenter(OfferFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public int getItemCount() {
        return offerTypes.size();
    }

    @Override
    public void bindViewHolder(OfferTypeAdapterHolders.Offer_typeViewHolder vh, final int position) {
        vh.tv_tab.setText(offerTypes.get(position).getName());
        vh.tv_tab.setAllCaps(true);

        deselectTab(vh.tv_tab, vh.tab_line);
        if (position == positionSelected)
            selectTab(vh.tv_tab, vh.tab_line);

        vh.btn_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionSelected = position;
                notifyDataSetChanged();
                if (presenter != null) {
                    currentOfferType = offerTypes.get(position);
                    presenter.showProgressInView();
                    if (position == 0)
                        presenter.getAllData();
                    else
                        presenter.getOffersByType(offerTypes.get(positionSelected));
                }
            }
        });
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionSelected = position;
                notifyDataSetChanged();
                if (presenter != null) {
                    currentOfferType = offerTypes.get(position);
                    presenter.showProgressInView();
                    if (position == 0)
                        presenter.getAllData();
                    else
                        presenter.getOffersByType(offerTypes.get(positionSelected));
                }
            }
        });
    }

    private void deselectTab(TextView tv, View line) {
        int deselectedColor = ContextCompat.getColor(context, R.color.tab_non_selected_color);
        tv.setTextColor(deselectedColor);
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Light.otf"));
        line.setBackgroundColor(Color.TRANSPARENT);
    }

    private void selectTab(TextView tv, View line) {
        int selectedColor = ContextCompat.getColor(context, R.color.tab_selected_color);
        tv.setTextColor(selectedColor);
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Semibold.otf"));
        line.setBackgroundColor(selectedColor);
    }


}
