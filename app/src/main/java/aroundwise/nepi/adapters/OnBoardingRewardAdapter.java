package aroundwise.nepi.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.eventbus.RewardDetailEvent;
import aroundwise.nepi.network.model.Reward;
import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 26/08/16.
 */


public class OnBoardingRewardAdapter extends SupportAnnotatedAdapter implements OnBoardingRewardAdapterBinder {


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
            layout = R.layout.item_store_reward,
            views =
                    {
                            @ViewField(id = R.id.iv_reward_selected, type = ImageView.class, name = "iv_reward_selected"),
                            @ViewField(id = R.id.iv_reward, type = ImageView.class, name = "iv_reward"),
                            @ViewField(id = R.id.tv_reward_name, type = TextView.class, name = "tv_reward_name"),
                            @ViewField(id = R.id.tv_points_value, type = TextView.class, name = "tv_points_value"),
                            @ViewField(id = R.id.btn_reward, type = Button.class, name = "btn_reward")
                    }
    )
    public static final int reward_store = 1;

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
                            @ViewField(id = R.id.btn_reward, type = Button.class, name = "btn_reward")
                    }
    )
    public static final int reward_2 = 3;

    Context context;
    boolean isChoosingReward;
    boolean isFromStore;
    boolean isItemChoosed = false;
    int itemSelected = -1;
    List<Reward> rewards;
    public OnLoadMoreListener onLoadMoreListener;
    boolean isInCadouriList = false;

    public void setInCadouriList(boolean inCadouriList) {
        isInCadouriList = inCadouriList;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public OnBoardingRewardAdapter(Context context, boolean isChoosingReward, boolean isFromStore) {
        super(context);
        this.context = context;
        this.isChoosingReward = isChoosingReward;
        this.isFromStore = isFromStore;
    }

    @Override
    public int getItemViewType(int position) {
        if (rewards.get(position) == null)
            return loadingRow;
        if (isFromStore)
            return reward_store;
        if (isInCadouriList)
            return reward_2;
        return reward;
    }

    @Override
    public int getItemCount() {
        return rewards == null ? 0 : rewards.size();
    }

    @Override
    public void bindViewHolder(OnBoardingRewardAdapterHolders.RewardViewHolder vh, final int position) {


        if (rewards.get(position).rewardImages.size() > 0)
            MyApplication.instance.getPicasso().load(rewards.get(position).rewardImages.get(0).getImage()).into(vh.iv_reward);
        vh.tv_points_value.setText(rewards.get(position).pointCost + "");
        vh.tv_reward_name.setText(rewards.get(position).title);


        if (isChoosingReward) {
            if (rewards.get(position).isSelected())
                vh.iv_reward_selected.setVisibility(View.VISIBLE);
            else
                vh.iv_reward_selected.setVisibility(View.GONE);
            vh.btn_reward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemSelected != position) {
                        for (int i = 0; i < rewards.size(); i++) {
                            rewards.get(i).setSelected(false);
                            if (i == position)
                                rewards.get(position).setSelected(true);
                        }
                        isItemChoosed = true;
                        itemSelected = position;
                        OnBoardingRewardAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        } else {
            vh.iv_reward_selected.setVisibility(View.GONE);
            vh.btn_reward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemSelected = position;
                    EventBus.getDefault().post(new RewardDetailEvent(rewards.get(position)));
                }
            });

        }
    }

    @Override
    public void bindViewHolder(OnBoardingRewardAdapterHolders.Reward_storeViewHolder vh, final int position) {
        if (rewards.get(position).rewardImages.size() > 0)
            MyApplication.instance.getPicasso().load(rewards.get(position).rewardImages.get(0).getImage()).into(vh.iv_reward);
        vh.tv_points_value.setText(String.valueOf(rewards.get(position).pointCost));
        vh.tv_reward_name.setText(rewards.get(position).title);
        vh.btn_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelected = position;
                EventBus.getDefault().post(new RewardDetailEvent(rewards.get(position)));
            }
        });
    }

    @Override
    public void bindViewHolder(OnBoardingRewardAdapterHolders.LoadingRowViewHolder vh, int position) {

    }

    @Override
    public void bindViewHolder(OnBoardingRewardAdapterHolders.Reward_2ViewHolder vh, final int position) {
        if (rewards.get(position).rewardImages.size() > 0)
            MyApplication.instance.getPicasso().load(rewards.get(position).rewardImages.get(0).getImage()).into(vh.iv_reward);
        vh.tv_points_value.setText(rewards.get(position).pointCost + "");
        vh.tv_reward_name.setText(rewards.get(position).title);


        if (isChoosingReward) {
            if (rewards.get(position).isSelected())
                vh.iv_reward_selected.setVisibility(View.VISIBLE);
            else
                vh.iv_reward_selected.setVisibility(View.GONE);
            vh.btn_reward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemSelected != position) {
                        for (int i = 0; i < rewards.size(); i++) {
                            rewards.get(i).setSelected(false);
                            if (i == position)
                                rewards.get(position).setSelected(true);
                        }
                        isItemChoosed = true;
                        itemSelected = position;
                        OnBoardingRewardAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        } else {
            vh.iv_reward_selected.setVisibility(View.GONE);
            vh.btn_reward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemSelected = position;
                    EventBus.getDefault().post(new RewardDetailEvent(rewards.get(position)));
                }
            });

        }
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
