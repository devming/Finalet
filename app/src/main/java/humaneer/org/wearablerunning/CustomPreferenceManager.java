package humaneer.org.wearablerunning;

import android.content.Context;
import android.content.SharedPreferences;

import humaneer.org.wearablerunning.Model.UserVO;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Minki on 2017-05-02.
 */

public class CustomPreferenceManager {

//    public static boolean isAlreadyRun(Context context) {
//
//        SharedPreferences pref = context.getSharedPreferences("isfirst", MODE_PRIVATE);
//        return pref.getBoolean("isfirst", false);   // 초기에 (앱실행 처음시) false
//    }
//
//    public static void setIsAlreadyRun(Context context, boolean first) {
//        SharedPreferences pref = context.getSharedPreferences("isfirst", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putBoolean("isfirst", first);
//        editor.apply();
//    }

    public static void setGoal(Context context, long id, double percentage) {

        // id값 - 날짜
        SharedPreferences prefId = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editorId = prefId.edit();
        editorId.putLong(UserVO.PERCENTAGE, id);
        editorId.apply();

        // percentage
        SharedPreferences prefPercentage = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editorPercentage = prefPercentage.edit();
        editorPercentage.putFloat(UserVO.PERCENTAGE, (float)percentage);
        editorPercentage.apply();


        // f(x) = ax^2 + p;

    }

    public static void setDayCount (Context context, int dayCount) {
        SharedPreferences prefDayCount= context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editorDayCount= prefDayCount.edit();
        editorDayCount.putInt("daycount", dayCount);
        editorDayCount.apply();
    }
}
