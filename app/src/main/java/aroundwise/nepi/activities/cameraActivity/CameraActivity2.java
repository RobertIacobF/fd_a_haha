package aroundwise.nepi.activities.cameraActivity;

import android.os.Bundle;

import aroundwise.nepi.R;
import aroundwise.nepi.base.BaseActivity;

/**
 * Created by Adonis on 4/18/2017.
 */
public class CameraActivity2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity_2);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment2.newInstance())
                    .commit();
        }
    }
}
