package aroundwise.nepi.activities.tutorialActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.base.BaseActivity;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Util;
import butterknife.BindView;
import butterknife.OnClick;

public class TutorialActivity extends BaseActivity {

    @BindView(R.id.iv_discover)
    ImageView iv_discover;

    @BindView(R.id.tv_learn_more)
    TextView tv_learn_more;

    @BindView(R.id.iv_overlay)
    ImageView iv_overlay;

    @BindView(R.id.iv_icon)
    ImageView ivIcon;

    @BindView(R.id.tv_first)
    TextView tvFirst;

    @BindView(R.id.tv_second)
    TextView tvSecond;

    private List<Integer> images, icons;
    private List<String> first, second;
    private int currentImage = 0;
    private boolean learnMorePressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        initialize();
        MyApplication.instance.getPicasso().load(images.get(currentImage)).into(iv_discover);
        MyApplication.instance.getPicasso().load(icons.get(currentImage)).into(ivIcon);
        tvFirst.setText(first.get(currentImage));
        tvSecond.setText(second.get(currentImage));
    }

    private void initialize() {
        images = new ArrayList<>();
        images.add(R.drawable.discover_1);
        images.add(R.drawable.discover_2);
        images.add(R.drawable.discover_2_1);
        images.add(R.drawable.discover_2_2);
        images.add(R.drawable.discover_2_3);
        images.add(R.drawable.discover_3);
        images.add(R.drawable.onboard_new);
        images.add(R.drawable.discover_4);

        icons = new ArrayList<>();
        icons.add(R.drawable.icon_onboard_1);
        icons.add(R.drawable.icon_onboard_2_3);
        icons.add(R.drawable.icon_onboard_2_1);
        icons.add(R.drawable.icon_onboard_2_2);
        icons.add(R.drawable.icon_onboard_2_3);
        icons.add(R.drawable.icon_onboard_3);
        icons.add(R.drawable.icon_loyalty_purchase);
        icons.add(R.drawable.icon_onboard_4);
        icons.add(R.drawable.icon_onboard_5);

        first = new ArrayList<>();
        first.add(getString(R.string.onboard_first_1));
        first.add(getString(R.string.onboard_first_2));
        first.add(getString(R.string.onboard_first_2_1));
        first.add(getString(R.string.onboard_first_2_2));
        first.add(getString(R.string.onboard_first_2_3));
        first.add(getString(R.string.onboard_first_3));
        first.add(getString(R.string.onboard_first_3_1));
        first.add(getString(R.string.onboard_first_4));

        second = new ArrayList<>();
        second.add(getString(R.string.onboard_second_1));
        second.add(getString(R.string.onboard_second_2));
        second.add(getString(R.string.onboard_second_2_1));
        second.add(getString(R.string.onboard_second_2_2));
        second.add(getString(R.string.onboard_second_2_3));
        second.add(getString(R.string.onboard_second_3));
        second.add(getString(R.string.onboard_second_3_1));
        second.add(getString(R.string.onboard_second_4));

    }

    @OnClick(R.id.btn_next)
    public void next() {
        if (currentImage < images.size() - 1) {
            currentImage++;
            MyApplication.instance.getPicasso().load(images.get(currentImage)).into(iv_discover);
            MyApplication.instance.getPicasso().load(icons.get(currentImage)).into(ivIcon);
            tvFirst.setText(first.get(currentImage));
            tvSecond.setText(second.get(currentImage));
        } else {
            TutorialActivity.this.finish();
        }

        if (currentImage == 1) {
            currentImage = 4;
            learnMorePressed = false;
            tv_learn_more.setVisibility(View.VISIBLE);
        } else {
            tv_learn_more.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.tv_learn_more)
    public void showLearnMore() {
        learnMorePressed = true;
        currentImage = 2;
        MyApplication.instance.getPicasso().load(images.get(currentImage)).into(iv_discover);
        tv_learn_more.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (!previous())
            super.onBackPressed();
    }

    private boolean previous() {
        if (currentImage > 0) {
            if (!learnMorePressed) {
                if (currentImage == 5)
                    currentImage = 1;
                else if (currentImage == 4)
                    currentImage = 0;
                else
                    currentImage--;
            } else
                currentImage--;
            MyApplication.instance.getPicasso().load(images.get(currentImage)).into(iv_discover);
            MyApplication.instance.getPicasso().load(icons.get(currentImage)).into(ivIcon);
            tvFirst.setText(first.get(currentImage));
            tvSecond.setText(second.get(currentImage));
            tv_learn_more.setVisibility(View.INVISIBLE);
            if (currentImage == 1) {
                learnMorePressed = false;
                tv_learn_more.setVisibility(View.VISIBLE);
            }
            return true;
        } else
            return false;
    }
}
