package aroundwise.nepi.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by mihai on 26/08/16.
 */
public class ComplexPreferences {
    private static ComplexPreferences complexPreferences;
    private final Context context;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private static Gson GSON = new Gson();


    private ComplexPreferences(Context context, String namePreferences, int mode) {
        this.context = context;
        if (namePreferences == null || namePreferences.equals("")) {
            namePreferences = "noKey";
        }
        preferences = context.getSharedPreferences(namePreferences, mode);
        editor = preferences.edit();
    }

    public static ComplexPreferences getComplexPreferences(Context context,
                                                           String namePreferences, int mode) {
        if (complexPreferences == null) {
            complexPreferences = new ComplexPreferences(context,
                    namePreferences, mode);
        }
        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object is null");
        }
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.putString(key, GSON.toJson(object));
        editor.commit();
    }

    public void save() {
        editor.commit();
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {
        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        } else {
            try {
                return GSON.fromJson(gson, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object stored with key "
                        + key + " is instance of other class");
            }
        }
    }
}