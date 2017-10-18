package aroundwise.nepi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.eventbus.RewardDetailEvent;
import aroundwise.nepi.network.model.Reward;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.views.CirclePageIndicator;
import aroundwise.nepi.util.views.CustomViewPager;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by irina on 1/31/2017.
 */
public class RewardsAdapter extends SupportAnnotatedAdapter implements RewardsAdapterBinder {

    @ViewType(
            layout = R.layout.item_reward,
            views =
                    {
                            @ViewField(id = R.id.iv_reward_selected, type = ImageView.class, name = "iv_reward_selected"),
                            @ViewField(id = R.id.iv_reward, type = ImageView.class, name = "iv_reward"),
                            @ViewField(id = R.id.tv_reward_name, type = TextView.class, name = "tv_reward_name"),
                            @ViewField(id = R.id.tv_points_value, type = TextView.class, name = "tv_points_value"),
                            @ViewField(id = R.id.btn_reward, type = Button.class, name = "btn_reward")
                    }
    )
    public static final int reward = 0;

    @ViewType(
            layout = R.layout.item_reward_new,
            views =
                    {
                            @ViewField(id = R.id.iv_reward_selected, type = ImageView.class, name = "iv_reward_selected"),
                            @ViewField(id = R.id.iv_reward, type = ImageView.class, name = "iv_reward"),
                            @ViewField(id = R.id.tv_reward_name, type = TextView.class, name = "tv_reward_name"),
                            @ViewField(id = R.id.tv_points_value, type = TextView.class, name = "tv_points_value"),
                            @ViewField(id = R.id.btn_reward, type = Button.class, name = "btn_reward"),
                            @ViewField(id = R.id.iv_offer_logo, type = ImageView.class, name = "ivStoreLogo"),
                            @ViewField(id = R.id.tv_offer_owner, type = TextView.class, name = "tvStoreName"),
                            @ViewField(id = R.id.iv_offer_mall_logo, type = ImageView.class, name = "ivMallLogo")
                    }
    )
    public static final int rewardNew = 1;

    @ViewType(
            layout = R.layout.item_loading_row,
            views =
                    {
                            @ViewField(id = R.id.progressbar, type = ProgressBar.class, name = "progressbar"),
                    }
    )
    public static final int loadingRow = 2;

    @ViewType(
            layout = R.layout.item_reward_2,
            views =
                    {
                            @ViewField(id = R.id.iv_reward_selected, type = ImageView.class, name = "iv_reward_selected"),
                            @ViewField(id = R.id.iv_reward, type = ImageView.class, name = "iv_reward"),
                            @ViewField(id = R.id.tv_reward_name, type = TextView.class, name = "tv_reward_name"),
                            @ViewField(id = R.id.tv_points_value, type = TextView.class, name = "tv_points_value"),
                            @ViewField(id = R.id.btn_reward, type = Button.class, name = "btn_reward"),
                            @ViewField(id = R.id.ivCorporateCode, type = CircleImageView.class, name = "ivCorporateCode")
                    }
    )
    public static final int reward_2 = 3;

    @ViewType(
            layout = R.layout.item_slider_rewards,
            views =
                    {
                            @ViewField(id = R.id.rl, type = RelativeLayout.class, name = "rl"),
                            @ViewField(id = R.id.viewpager, type = CustomViewPager.class, name = "viewPager"),
                            @ViewField(id = R.id.viewpager_indicator, type = CirclePageIndicator.class, name = "pageIndicator")
                    }
    )
    public static final int slider = 4;

    Context context;
    boolean isItemChoosed = false;
    int isBannerItem = 0;
    int itemSelected = -1;
    List<Reward> rewards;
    List<Reward> bannerRewards;
    public OnLoadMoreListener onLoadMoreListener;


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setBannerRewards(List<Reward> bannerRewards) {
        this.bannerRewards = bannerRewards;
        if (bannerRewards.size() > 0)
            isBannerItem = 1;
        else
            isBannerItem = 0;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public RewardsAdapter(Context context, List<Reward> rewards, List<Reward> bannerRewards) {
        super(context);
        this.context = context;
        this.rewards = rewards;
        this.bannerRewards = bannerRewards;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && bannerRewards != null
                && bannerRewards.size() > 0)
            return slider;
        if (rewards.get(position - isBannerItem) == null)
            return loadingRow;
        return rewardNew;
    }

    @Override
    public int getItemCount() {
        if (bannerRewards != null
                && bannerRewards.size() > 0)
            isBannerItem = 1;
        return rewards == null ? 0 : rewards.size() + isBannerItem;
    }

    @Override
    public void bindViewHolder(RewardsAdapterHolders.RewardNewViewHolder vh, final int position) {
        final Reward reward = rewards.get(position - isBannerItem);
        if (reward.rewardImages.size() > 0)
            MyApplication.instance.getPicasso().load(reward.rewardImages.get(0).getImage()).into(vh.iv_reward);
        vh.tv_points_value.setText(reward.pointCost + "");
        vh.tv_reward_name.setText(reward.title);
        vh.iv_reward_selected.setVisibility(View.GONE);
        vh.btn_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelected = position;
                EventBus.getDefault().post(new RewardDetailEvent(reward));
            }
        });
        MyApplication.instance.getPicasso().load(reward.store.mall.getLogo()).into(vh.ivMallLogo);
        MyApplication.instance.getPicasso().load(reward.store.getLogo()).into(vh.ivStoreLogo);
        vh.tvStoreName.setText(reward.store.getName());
    }

    @Override
    public void bindViewHolder(RewardsAdapterHolders.RewardViewHolder vh, final int position) {
        final Reward reward = rewards.get(position - isBannerItem);
        if (reward.rewardImages.size() > 0)
            MyApplication.instance.getPicasso().load(reward.rewardImages.get(0).getImage()).into(vh.iv_reward);
        vh.tv_points_value.setText(reward.pointCost + "");
        vh.tv_reward_name.setText(reward.title);
        vh.iv_reward_selected.setVisibility(View.GONE);
        vh.btn_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelected = position;
                EventBus.getDefault().post(new RewardDetailEvent(reward));
            }
        });


    }

    @Override
    public void bindViewHolder(RewardsAdapterHolders.LoadingRowViewHolder vh, int position) {

    }

    @Override
    public void bindViewHolder(RewardsAdapterHolders.Reward_2ViewHolder vh, final int position) {
        final Reward reward = rewards.get(position - isBannerItem);
        if (reward.rewardImages.size() > 0)
            MyApplication.instance.getPicasso().load(reward.rewardImages.get(0).getImage()).into(vh.iv_reward);
        vh.tv_points_value.setText(reward.pointCost + "");
        vh.tv_reward_name.setText(reward.title);
        vh.iv_reward_selected.setVisibility(View.GONE);
        vh.btn_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelected = position;
                EventBus.getDefault().post(new RewardDetailEvent(reward));
            }
        });
        if (reward.isPremium) {
            vh.ivCorporateCode.setVisibility(View.VISIBLE);
            MyApplication.instance.getPicasso().load(Session.getUser().getProfile().getCorporateLogo()).into(vh.ivCorporateCode);
        }

    }

    @Override
    public void bindViewHolder(RewardsAdapterHolders.SliderViewHolder vh, int position) {
        RewardPagerAdapter adapter = new RewardPagerAdapter(bannerRewards, context);
        vh.viewPager.setAdapter(adapter);
        vh.pageIndicator.setViewPager(vh.viewPager);
    }


    public int getItemSelected() {
        return itemSelected;
    }

    @Nullable
    public Reward getSelectedReward() {
        if (itemSelected != -1)
            return rewards.get(itemSelected);
        else
            return null;
    }

    public boolean isItemChoosed() {
        return isItemChoosed;
    }


}
