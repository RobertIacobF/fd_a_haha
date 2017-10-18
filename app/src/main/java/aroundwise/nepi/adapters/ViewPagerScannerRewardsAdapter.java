package aroundwise.nepi.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.session.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by irina on 2/28/2017.
 */
public class ViewPagerScannerRewardsAdapter extends PagerAdapter {

    private List<Reward> rewards;
    Context context;

    public ViewPagerScannerRewardsAdapter(List<Reward> rewards, Context context) {
        this.rewards = rewards;
        this.context = context;
    }


    @Override
    public int getCount() {
        return rewards.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_scanner_viewpager_reward, null);
        Reward reward = rewards.get(position);
        CircleImageView circleImageView = (CircleImageView) v.findViewById(R.id.iv_reward);
        TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
        TextView tvPuncte = (TextView) v.findViewById(R.id.tv_puncte);
        DonutProgress donutProgress = (DonutProgress) v.findViewById(R.id.reward_progress);
        if (reward.rewardImages != null && reward.rewardImages.size() > 0)
            MyApplication.instance.getPicasso().load(reward.rewardImages.get(0).getImage()).into(circleImageView);
        tvTitle.setText(reward.title);
        tvPuncte.setText(String.valueOf(reward.pointCost));
        donutProgress.setMax(reward.pointCost);
        donutProgress.setProgress(Session.getUser().getProfile().getPoints());
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}