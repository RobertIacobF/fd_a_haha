package aroundwise.nepi.util;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import aroundwise.nepi.R;

/**
 * Created by mihai on 04/10/16.
 */
public class SlideAnimationUtil {

    public static void slideInFromLeft(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.slide_from_left);
    }

    public static void slideInFromRight(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.slide_from_right);
    }

    public static void slideOutToLeft(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.slide_to_left);
    }

    public static void slideOutFromRight(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.slide_to_right);
    }

    public static void runSimpleAnimation(Context context, View view, int animationId) {
        view.startAnimation(AnimationUtils.loadAnimation(context, animationId));
    }

}
