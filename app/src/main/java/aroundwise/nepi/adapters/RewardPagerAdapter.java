package aroundwise.nepi.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.eventbus.RewardDetailEvent;
import aroundwise.nepi.network.model.Image;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.testClasses.ModelRewardAdapter;
import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 05/09/16.
 */
public class RewardPagerAdapter extends PagerAdapter {

    private List<Reward> images;
    Context context;

    public RewardPagerAdapter(List<Reward> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final Reward reward = images.get(position);

        View view = LayoutInflater.from(context).inflate(R.layout.item_reward_adapter, null);
        ImageView iv_background = (ImageView) view.findViewById(R.id.iv_background);
        ImageView iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
        TextView tv_product = (TextView) view.findViewById(R.id.tv_product);
        TextView tv_points = (TextView) view.findViewById(R.id.tv_points_nb);
        for (Image image : reward.rewardImages) {
            if (image.isBanner()) {
                MyApplication.instance.getPicasso().load(image.getImage()).into(iv_background);
                break;
            }
        }
        Button button = (Button) view.findViewById(R.id.btn_image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new RewardDetailEvent(reward));
            }
        });
        MyApplication.instance.getPicasso().load(reward.store.getLogo()).into(iv_logo);
        tv_product.setText(reward.title);
        tv_points.setText(String.valueOf(reward.pointCost) + " PUNCTE");


        ((ViewPager) container).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
