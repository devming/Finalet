package humaneer.org.wearablerunning.Fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import humaneer.org.wearablerunning.MyItem;
import humaneer.org.wearablerunning.R;

public class DataFragment extends Fragment {

    TextView textviewDistance;
    TextView textviewTime;
    TextView textviewAvgSpeed;

    double distance = 0.0;
    int time = 0;
    double speed = 0.0;


    public DataFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.fragment_data, container, false);

        MyItem myItem = new MyItem();
//        String dateWeek = myItem.


        float []dataObjects = new float[3];
        dataObjects[0] = 1.5f;
        dataObjects[1] = 3.3f;
        dataObjects[2] = 2.6f;


        List<Entry> entries = new ArrayList<Entry>();
        for(float data : dataObjects) {
            entries.add(new Entry(data, data));
        }

        LineChart chart = (LineChart) view.findViewById(R.id.dataChart);

        LineDataSet dataSetGoal = new LineDataSet(entries, "Goal");
        dataSetGoal.setColor(R.color.colorAccent);
        dataSetGoal.setValueTextColor(R.color.colorAccent);
        LineDataSet dataSetCurrent = new LineDataSet(entries, "My Status");
        dataSetCurrent.setColor(R.color.colorPrimary);
        dataSetCurrent.setValueTextColor(R.color.colorPrimary);


        LineData lineData = new LineData(dataSetCurrent);
        chart.setData(lineData);
        chart.invalidate();

//        textviewDistance = (TextView) view.findViewById(R.id.textview_distance);
//        textviewTime = (TextView) view.findViewById(R.id.textview_time);
//        textviewAvgSpeed = (TextView) view.findViewById(R.id.textview_speed);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
