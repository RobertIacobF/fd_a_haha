package aroundwise.nepi;

import android.content.Context;
import android.content.Intent;

/**
 * Created by mihai on 26/08/16.
 */
public class IntentStarter {

    public void startActivity(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }


}
