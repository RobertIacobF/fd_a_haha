package aroundwise.nepi.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import aroundwise.nepi.R;

/**
 * Created by irina on 11/29/2016.
 */
public class ViewPagerAdapterOnlyText extends PagerAdapter {

    List<String> texts;
    Context context;

    public ViewPagerAdapterOnlyText(Context context, List<String> texts) {
        this.context = context;
        this.texts = texts;
    }

    @Override
    public int getCount() {
        return texts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) layoutInflater.inflate(R.layout.item_view_pager_only_text, container, false);
        container.addView(layout);
        TextView tvText = (TextView) layout.findViewById(R.id.tvMiddleText);
        tvText.setText(texts.get(position));
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
