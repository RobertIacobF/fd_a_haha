package aroundwise.nepi.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.eventbus.RewardDetailEvent;
import aroundwise.nepi.network.model.Reward;
import de.greenrobot.event.EventBus;

/**
 * Created by irina on 1/5/2017.
 */
public class MyRewardAdapter extends SupportAnnotatedAdapter implements MyRewardAdapterBinder {

    @ViewType(
            layout = R.layout.my_reward_item,
            views = {
                    @ViewField(id = R.id.btn_action, type = Button.class, name = "btnAction"),
                    @ViewField(id = R.id.tv_title, type = TextView.class, name = "tvTitle"),
                    @ViewField(id = R.id.tv_subtitle, type = TextView.class, name = "tvSubtitle"),
                    @ViewField(id = R.id.iv_offer_logo, type = ImageView.class, name = "rewardStoreLogo"),
                    @ViewField(id = R.id.tv_offer_owner, type = TextView.class, name = "rewardStoreTitle"),
                    @ViewField(id = R.id.iv_offer_mall_logo, type = ImageView.class, name = "mallLogo"),
                    @ViewField(id = R.id.iv_offer_photo, type = ImageView.class, name = "rewardPhoto"),
                    @ViewField(id = R.id.rewardStateTv, type = TextView.class, name = "rewardState")
            }
    )
    public final int reward = 0;

    List<Reward> rewards;

    public MyRewardAdapter(Context context, List<Reward> rewards) {
        super(context);
        this.rewards = rewards;
    }

    @Override
    public int getItemCount() {
        return rewards != null ? rewards.size() : 0;
    }

    @Override
    public void bindViewHolder(MyRewardAdapterHolders.RewardViewHolder vh, int position) {

        final Reward reward = rewards.get(position);
        MyApplication.instance.getPicasso().load(reward.store.getLogo()).placeholder(R.mipmap.ic_launcher).into(vh.rewardStoreLogo);
        MyApplication.instance.getPicasso().load(reward.store.mall.getLogo()).placeholder(R.mipmap.ic_launcher).into(vh.mallLogo);
        MyApplication.instance.getPicasso().load(reward.rewardImages.get(0).getImage()).into(vh.rewardPhoto);
        vh.rewardStoreTitle.setText(reward.store.getName());
        vh.tvTitle.setText(reward.title);
        vh.tvSubtitle.setText(reward.description);
        if (reward.statusReward != null && reward.statusReward.toLowerCase().equals("pending")) {
            vh.rewardState.setActivated(false);
            vh.rewardState.setText("ÎN AȘTEPTARE");
        } else if (reward.statusReward != null && reward.statusReward.toLowerCase().equals("rejected")) {
            vh.rewardState.setActivated(false);
            vh.rewardState.setText("RESPINS");
        } else {
            vh.rewardState.setActivated(true);
            vh.rewardState.setText("PRIMIT");
        }
        vh.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new RewardDetailEvent(reward));
            }
        });


    }
}
