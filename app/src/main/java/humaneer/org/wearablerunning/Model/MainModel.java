package humaneer.org.wearablerunning.Model;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import humaneer.org.wearablerunning.Activity.MainActivity;
import humaneer.org.wearablerunning.R;
import io.realm.RealmResults;

/**
 * Created by Minki on 2017-04-25.
 */

public class MainModel {

    LineDataSet dataSetGoal;
    LineDataSet dataSetCurrent;
    List<UserVO> items;

    public MainModel() {

        setDefaultChartData();
        setDefaultDateData();
    }

    RealmResults<UserVO> userInfo = getUserInfo();

    public RealmResults<UserVO> getUserInfo() {
        return MainActivity.GetRealmObject().where(UserVO.class).findAll();
    }

    private void setDefaultChartData() {

        float []dataObjects = new float[3];
        dataObjects[0] = 1.5f;
        dataObjects[1] = 3.3f;
        dataObjects[2] = 2.6f;


        List<Entry> entries = new ArrayList<Entry>();
        for(float data : dataObjects) {
            entries.add(new Entry(data, data));
        }

        dataSetGoal = new LineDataSet(entries, "Goal");
        dataSetGoal.setColor(R.color.colorAccent);
        dataSetGoal.setValueTextColor(R.color.colorAccent);

//        dataSetCurrent = new LineDataSet(entries, "My Status");
//        dataSetCurrent.setColor(R.color.colorPrimary);
//        dataSetCurrent.setValueTextColor(R.color.colorPrimary);
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

        while(list.hasNext())
            items.add(list.next());
//        items.add(userInfo);

//        setDateList();

//        switch (Calendar.getInstance().DAY_OF_WEEK){
//            case Calendar.MONDAY: // MON
//                setDateList(0);
//                break;
//            case Calendar.TUESDAY: // TUE
//                setDateList(1);
//                break;
//            case Calendar.WEDNESDAY: // WED
//                setDateList(2);
//                break;
//            case Calendar.THURSDAY: // THU
//                setDateList(3);
//                break;
//            case Calendar.FRIDAY: // FRI
//                setDateList(4);
//                break;
//            case Calendar.SATURDAY: // SAT
//                setDateList(5);
//                break;
//            case Calendar.SUNDAY: // SUN
//                setDateList(6);
//                break;
//        }


//        // TODO: DB에서 날짜 데이터 불러오기
//        items = new ArrayList<UserVO>();
//        UserVO itemData = new UserVO();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
    }

    private void setDateList() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EEE", Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        int weekNum = calendar.getFirstDayOfWeek();

        Log.d("### TODAY", weekNum+"");

        UserVO itemData;
        itemData = new UserVO();
        calendar.add(Calendar.DATE, weekNum);
        itemData.setDate(dateFormat.format(calendar.getTime()));
        items.add(itemData);

    }

    private void setDateList1() {

        UserVO itemData;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EEE", Locale.ENGLISH);

        for(int i=3;i<10;i++) {
            int weekNum = i;
            if(i == 9)
                weekNum = 2;
            itemData = new UserVO();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, weekNum);
            itemData.setDate(dateFormat.format(calendar.getTime()));

            items.add(itemData);
        }
    }

}
