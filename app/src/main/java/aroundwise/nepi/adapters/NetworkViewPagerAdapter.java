package aroundwise.nepi.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.network.model.Image;

/**
 * Created by mihai on 09/09/16.
 */
public class NetworkViewPagerAdapter extends PagerAdapter {
    private List<Image> images;
    Context context;

    public NetworkViewPagerAdapter(List<Image> images, Context context) {
        this.images = images;
        this.context = context;
    }


    @Override
    public int getCount() {
        return images == null ? 0 : images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = new ImageView(context);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        MyApplication.instance.getPicasso().load(images.get(position).getImage()).fit().centerCrop().placeholder(R.drawable.placeholder).into(iv);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ((ViewPager) container).addView(iv, 0);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
