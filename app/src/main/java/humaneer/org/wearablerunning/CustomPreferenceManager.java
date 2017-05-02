package humaneer.org.wearablerunning;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Minki on 2017-05-02.
 */

public class CustomPreferenceManager {

    public static boolean isAlreadyRun(Context context) {

        SharedPreferences pref = context.getSharedPreferences("isfirst", MODE_PRIVATE);
        return pref.getBoolean("isfirst", false);   // 초기에 (앱실행 처음시) false
    }

    public static void setIsAlreadyRun(Context context, boolean first) {
        SharedPreferences pref = context.getSharedPreferences("isfirst", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isfirst", first);
        editor.commit();
    }
}
