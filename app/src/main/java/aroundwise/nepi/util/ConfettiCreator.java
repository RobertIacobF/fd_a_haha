package aroundwise.nepi.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;

import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aroundwise.nepi.R;

/**
 * Created by mihai on 21/10/16.
 */
public class ConfettiCreator {

    public void confetti(Context context, ViewGroup v, long duration, int emissionRate) {
        final List<Bitmap> confettis = createConfettis(context);
        final int numConfetti = confettis.size();
        ConfettoGenerator confettoGenerator = new ConfettoGenerator() {
            @Override
            public Confetto generateConfetto(Random random) {
                final Bitmap bitmap = confettis.get(random.nextInt(numConfetti));
                return new BitmapConfetto(bitmap);
            }
        };

        ConfettiSource confettiSource = new ConfettiSource(0, 0, v.getHeight(), v.getWidth());
        new ConfettiManager(context, confettoGenerator, confettiSource, v)
                .setEmissionDuration(duration)
                .setEmissionRate(emissionRate)
                .setVelocityX(50)
                .setVelocityY(50)
                .setRotationalVelocity(180, 180)
                .animate();
    }

    private List<Bitmap> createConfettis(Context context) {
        List<Bitmap> confettis = new ArrayList<>();
        confettis.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.confetti_blue));
        confettis.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.confetti_dark_purple));
        confettis.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.confetti_green));
        confettis.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.confetti_light_blue));
        confettis.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.confetti_purple));
        confettis.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.confetti_yellow));
        return confettis;
    }
}
