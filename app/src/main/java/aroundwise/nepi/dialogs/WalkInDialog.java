package aroundwise.nepi.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aroundwise.nepi.MyApplication;
import aroundwise.nepi.R;
import aroundwise.nepi.eventbus.MallHasChangedEvent;
import aroundwise.nepi.eventbus.ReviewEvent;
import aroundwise.nepi.network.model.Shop;
import aroundwise.nepi.session.Session;
import aroundwise.nepi.util.Constants;
import aroundwise.nepi.util.views.SAutoBgButton;
import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 26/09/16.
 */
public class WalkInDialog extends DialogFragment {

    TextView tvPoints;
    RelativeLayout rlContent;
    FrameLayout flConffetii;
    ImageView mallLogo;
    ImageView storeLogo;

    int points;
    String logoUrl;
    String mallLogoUrl;

    public static WalkInDialog newInstance(int points, String mallLogoUrl) {
        WalkInDialog walkInDialog = new WalkInDialog();
        Bundle args = new Bundle();
        args.putString("mallLogoUrl", mallLogoUrl);
        args.putInt("points", points);
        walkInDialog.setArguments(args);
        return walkInDialog;
    }

    public static WalkInDialog newInstance(String logoUrl, int pointsReceived, String mallLogoUrl) {
        WalkInDialog walkInDialog = new WalkInDialog();
        Bundle args = new Bundle();
        args.putInt("points", pointsReceived);
        args.putString("mallLogoUrl", mallLogoUrl);
        args.putString("logoUrl", logoUrl);
        walkInDialog.setArguments(args);
        return walkInDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        points = getArguments().getInt("points");
        logoUrl = getArguments().getString("logoUrl", "");
        mallLogoUrl = getArguments().getString("mallLogoUrl", "");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyCustomTheme);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.layout_walk_in_reward, null);
        builder.setView(dialogView);
        rlContent = (RelativeLayout) dialogView.findViewById(R.id.rl);
        tvPoints = (TextView) dialogView.findViewById(R.id.tv_points);
        flConffetii = (FrameLayout) dialogView.findViewById(R.id.fl_confetii);
        mallLogo = (ImageView) dialogView.findViewById(R.id.store_logo);
        storeLogo = (ImageView) dialogView.findViewById(R.id.dialog_contextual_iv_logo);
        if (logoUrl != null && !logoUrl.isEmpty())
            MyApplication.instance.getPicasso().load(logoUrl).placeholder(R.mipmap.launcher).into(storeLogo);
        if (mallLogoUrl != null && !mallLogoUrl.isEmpty())
            MyApplication.instance.getPicasso().load(mallLogoUrl).fit().into(mallLogo);
        else
            MyApplication.instance.getPicasso().load(Session.getMalls().get(0).getLogo()).fit().into(mallLogo);
        tvPoints.setText(String.valueOf(points));
        SAutoBgButton btnThankYou = (SAutoBgButton) dialogView.findViewById(R.id.btn_thank_you);
        btnThankYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MallHasChangedEvent());
                EventBus.getDefault().post(new ReviewEvent());
                WalkInDialog.this.dismiss();
            }
        });
        rlContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                confetti();
            }
        });

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void confetti() {
        final List<Bitmap> confettis = createConfettis();
        final int numConfetti = confettis.size();
        ConfettoGenerator confettoGenerator = new ConfettoGenerator() {
            @Override
            public Confetto generateConfetto(Random random) {
                final Bitmap bitmap = confettis.get(random.nextInt(numConfetti));
                return new BitmapConfetto(bitmap);
            }
        };

        ConfettiSource confettiSource = new ConfettiSource(0, 0, rlContent.getHeight(), rlContent.getWidth());
        new ConfettiManager(getContext(), confettoGenerator, confettiSource, rlContent)
                .setEmissionDuration(ConfettiManager.INFINITE_DURATION)
                .setEmissionRate(10)
                .setVelocityX(50)
                .setVelocityY(50)
                .setRotationalVelocity(180, 180)
                .animate();
    }

    private List<Bitmap> createConfettis() {
        List<Bitmap> confettis = new ArrayList<>();
        confettis.add(BitmapFactory.decodeResource(getResources(), R.drawable.confetti_blue));
        confettis.add(BitmapFactory.decodeResource(getResources(), R.drawable.confetti_dark_purple));
        confettis.add(BitmapFactory.decodeResource(getResources(), R.drawable.confetti_green));
        confettis.add(BitmapFactory.decodeResource(getResources(), R.drawable.confetti_light_blue));
        confettis.add(BitmapFactory.decodeResource(getResources(), R.drawable.confetti_purple));
        confettis.add(BitmapFactory.decodeResource(getResources(), R.drawable.confetti_yellow));
        return confettis;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


}
