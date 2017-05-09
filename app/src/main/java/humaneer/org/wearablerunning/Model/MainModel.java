package humaneer.org.wearablerunning.Model;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import humaneer.org.wearablerunning.Activity.MainActivity;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;

/**
 * Created by Minki on 2017-04-25.
 */

public class MainModel {

//    private ArrayList<String> labels;
    private LineDataSet dataSetGoal;
    private LineDataSet dataSetCurrent;
    private List<UserVO> items;

    private ArrayList<ILineDataSet> dataSets;

    public static final int INITIAL_VALUE = 1;

    public MainModel() {

        dataSets = new ArrayList<>();

        if(getTartget() != null) {
            Log.d(TAG, "### getTartget() in MainModel Constructor.");
            setDataGoal();
        }
        if(getUserInfo() != null) {
            Log.d(TAG, "### getUserInfo() in MainModel Constructor.");
            setDataCurrent();
        }

//        setUserInfoData();
    }

//    RealmResults<UserVO> getUserInfo() = getUserInfo();
//    RealmResults<UserVO> getTartget() = getTartget();

    public RealmResults<UserVO> getUserInfo() {
        return Realm.getInstance(MainActivity.Config).where(UserVO.class).greaterThan("_id", 10000).findAll();
    }

    public RealmResults<UserVO> getTartget() {
        return Realm.getInstance(MainActivity.Config).where(UserVO.class).between("_id", INITIAL_VALUE, 10000).findAll();
    }

    private void setDataGoal() {

        // X축 라벨 추가
//       labels = new ArrayList<String>();
//       for (int i=0;i<userInfo.size();i++)  // 0:  시작점
//            labels.add(i+"");

        List<Entry> entries = new ArrayList<Entry>();
        int idx = 0;
        for(UserVO data : getTartget()) {
            entries.add(new Entry(idx++, (float)data.getPercentage()));
        }
//        RealmLineDataSet<UserVO> set = new RealmLineDataSet<UserVO>(userInfo, "xValue", "yValue");
        dataSetGoal = new LineDataSet(entries, "Goal");
        dataSetGoal.setMode(LineDataSet.Mode.LINEAR);
//        dataSetGoal.setLabel("Goal");
        dataSetGoal.setDrawValues(false);

        dataSetGoal.setDrawCircleHole(false);
        dataSetGoal.setColor(ColorTemplate.rgb("#FF5722"));
        dataSetGoal.setCircleColor(ColorTemplate.rgb("#FF5722"));
        dataSetGoal.setLineWidth(1.8f);
        dataSetGoal.setCircleRadius(1.5f);
        dataSets.add(dataSetGoal);
    }

    private void setDataCurrent() {

        List<Entry> entries = new ArrayList<Entry>();
        int idx = 0;
        for(UserVO data : getUserInfo()) {
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
        dataSetCurrent.setCircleRadius(1.5f);
        dataSets.add(dataSetCurrent);
    }



    public LineDataSet getDataSetGoal() {

        return dataSetGoal;
    }

    public LineDataSet getDataSetCurrent() {

        return dataSetCurrent;
    }

    public LineData getLineDataSets() {
        return new LineData(dataSets);
    }

    public List<UserVO> getDefaultDateData() {
        return items;
    }

    public void setUserInfoData() {
        items = new ArrayList<UserVO>();

        ListIterator<UserVO> list = getUserInfo().listIterator();

        while(list.hasNext()) {

            UserVO temp = list.next();
            items.add(temp);
        }
    }


    public void setTargetUserInfoData() {
        items = new ArrayList<UserVO>();

        ListIterator<UserVO> list = getTartget().listIterator();

        while(list.hasNext()) {

            UserVO temp = list.next();
            items.add(temp);
        }
    }
}
