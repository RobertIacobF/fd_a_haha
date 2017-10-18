package aroundwise.nepi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.network.model.Mall;

/**
 * Created by mihai on 29/08/16.
 */
public class SpinnerAdapter extends BaseAdapter {

    Context context;
    List<Mall> malls;
    String tabtitle;
    boolean store = false;
    boolean myprofile = false;
    int mallNameTextColorResource = 0;

    public SpinnerAdapter(Context context, List<Mall> malls, String tabtitle) {
        this.context = context;
        this.malls = malls;
        this.tabtitle = tabtitle;
    }

    public void setMallNameTextColorResource(int mallNameTextColorResource) {
        this.mallNameTextColorResource = mallNameTextColorResource;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public void setMyprofile(boolean myprofile) {
        this.myprofile = myprofile;
    }

    @Override
    public int getCount() {
        return malls.size();
    }

    @Override
    public Object getItem(int i) {
        return malls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (!store)
            view = LayoutInflater.from(context).inflate(R.layout.spinner_adapter, null);
        else
            view = LayoutInflater.from(context).inflate(R.layout.spinner_adapter_store, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_mall_logo);
        TextView tv = (TextView) view.findViewById(R.id.actionbar_title);
        TextView tv_arrow = (TextView) view.findViewById(R.id.tv_arrow_down);
        TextView tvTabTitle = (TextView) view.findViewById(R.id.tab_title);
        tvTabTitle.setText(tabtitle);
        MyApplication.instance.getPicasso().load(malls.get(i).getLogo()).into(iv);
        if (i == 0) {
            if (store)
                iv.setVisibility(View.INVISIBLE);
            tv_arrow.setText(Html.fromHtml("&#x25BC"));
            tvTabTitle.setVisibility(View.VISIBLE);
            if (myprofile) {
                tvTabTitle.setTextColor(Color.WHITE);
                tv.setTextColor(Color.parseColor("#a0989b"));
            }
        }
        tv.setText(malls.get(i).getName().toUpperCase());
        if (mallNameTextColorResource != 0) {
            tv.setTextColor(Color.parseColor("#80ffffff"));
        }
        return view;
    }

}
