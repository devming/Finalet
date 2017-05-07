package humaneer.org.wearablerunning.Model;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import humaneer.org.wearablerunning.Activity.MainActivity;
import io.realm.RealmResults;

/**
 * Created by Minki on 2017-04-25.
 */

public class MainModel {

//    private ArrayList<String> labels;
    private LineDataSet dataSetGoal;
    private LineDataSet dataSetCurrent;
    private List<UserVO> items;

    public static final int INITIAL_VALUE = 100;

    public MainModel() {

//        setDataGoal();
        setDataCurrent();
        setDefaultDateData();
    }

    RealmResults<UserVO> userInfo = getUserInfo();
    RealmResults<UserVO> userTargetInfo = getTartget();

    public RealmResults<UserVO> getUserInfo() {
        return MainActivity.GetRealmObject().where(UserVO.class).findAll();
    }

    public RealmResults<UserVO> getTartget() {
        return MainActivity.GetRealmObject().where(UserVO.class).equalTo("_id", INITIAL_VALUE).findAll();
    }

    private void setDataGoal() {

        // X축 라벨 추가
//       labels = new ArrayList<String>();
//       for (int i=0;i<userInfo.size();i++)  // 0:  시작점
//            labels.add(i+"");

        List<Entry> entries = new ArrayList<Entry>();
        int idx = 0;
        for(UserVO data : userTargetInfo) {
            entries.add(new Entry(idx++, (float)data.getPercentage()));
        }
//        RealmLineDataSet<UserVO> set = new RealmLineDataSet<UserVO>(userInfo, "xValue", "yValue");
        dataSetGoal = new LineDataSet(entries, "Goal");
        dataSetGoal.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
//        dataSetGoal.setLabel("Goal");
        dataSetGoal.setDrawValues(false);

        dataSetGoal.setDrawCircleHole(false);
        dataSetGoal.setColor(ColorTemplate.rgb("#FF5722"));
        dataSetGoal.setCircleColor(ColorTemplate.rgb("#FF5722"));
        dataSetGoal.setLineWidth(1.8f);
        dataSetGoal.setCircleRadius(3.6f);

    }

    private void setDataCurrent() {

        List<Entry> entries = new ArrayList<Entry>();
        int idx = 0;
        for(UserVO data : userInfo) {
            entries.add(new Entry(idx++, (float)data.getPercentage()));
        }

        dataSetCurrent = new LineDataSet(entries, "Current");
        dataSetCurrent.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
//        dataSetGoal.setLabel("Goal");
        dataSetCurrent.setDrawValues(false);

        dataSetCurrent.setDrawCircleHole(false);
        dataSetCurrent.setColor(ColorTemplate.rgb("#2257FF"));
        dataSetCurrent.setCircleColor(ColorTemplate.rgb("#2257FF"));
        dataSetCurrent.setLineWidth(1.8f);
        dataSetCurrent.setCircleRadius(3.6f);
    }



    public LineDataSet getDataSetGoal() {

        return dataSetGoal;
    }

    public LineDataSet getDataSetCurrent() {

        return dataSetCurrent;
    }

    public List<UserVO> getDefaultDateData() {
        return items;
    }

    private void setDefaultDateData() {
        items = new ArrayList<UserVO>();

        ListIterator<UserVO> list = userInfo.listIterator();

        Log.d("### AAAAAAA ", "null !!");
        while(list.hasNext()) {

            UserVO temp = list.next();
            if(temp == null)
                Log.d("### items:", "null !!");
            else
                Log.d("### items:", temp.get_Id()+"");

            items.add(temp);
        }
        Log.d("### BBBBBBB ", "null !!");
    }

}
