package humaneer.org.wearablerunning.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

import humaneer.org.wearablerunning.DataRecyclerViewAdapter;
import humaneer.org.wearablerunning.Model.MainModel;
import humaneer.org.wearablerunning.MyXAxisValueFormatter;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.databinding.FragmentDataBinding;

import static android.content.ContentValues.TAG;

public class DataFragment extends Fragment {

    private FragmentDataBinding binding;
    private MainModel mainModel;

    private RecyclerView.LayoutManager mLayoutManager;
    private DataRecyclerViewAdapter adapter;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding = FragmentDataBinding.bind(getView());

        // RecyclerView에 LayoutManager 할당
        binding.recyclerViewDate.setLayoutManager(mLayoutManager);

        adapter = new DataRecyclerViewAdapter(getContext());

        binding.recyclerViewDate.setAdapter(adapter);

        mainModel = new MainModel();

        drawGraph();
//        drawGoalGraph();
//        if(CustomPreferenceManager.isAlreadyRun(getContext())) {
//        if(MainActivity.getIsGoalMode()) {  // Goal mode 일경우( 초기 목표 설정모드 )
//
//        } else {
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.fragment_data, container, false);

        Log.d("### DataFragment", "onCreateView");
        // RecyclerView Layout Manager 설정
        mLayoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);


        return view;
    }

    private void drawGraph() {

//            LineData lineData1 = new LineData(mainModel.getDataSetCurrent());
//            binding.dataChart.setData(lineData1);
//            binding.dataChart.invalidate();

        LineData lineData = mainModel.getLineDataSets();
//            LineData lineData = new LineData( mainModel.getDataSetCurrent());

        final ArrayList<String> strings = new ArrayList<>();

        SharedPreferences pref = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        int dayCount = pref.getInt("daycount", 404);

        // 저장된 값이 없으면 (목표 세팅이 안되어있는 상황)
        if(dayCount == 404) {
            return;
        }

        for(int i=1;i<=dayCount;i++){
            strings.add(i+"");
        }
        Log.d(TAG, "### dayCount: "+ dayCount);
        MyXAxisValueFormatter myXAxisValueFormatter = new MyXAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                Log.d(TAG, "### value: "+ value);
                return strings.get((int)value);
//                    return mainModel.getUserInfo().get((int) value).get_Id()+"";
            }
        };

        XAxis xAxis = binding.dataChart.getXAxis();
//            String []str = mainModel.getLabels();
//            xAxis.setValueFormatter(new MyXAxisValueFormatter(str));

        xAxis.setValueFormatter(myXAxisValueFormatter);

        binding.dataChart.setData(lineData);
        binding.dataChart.setDoubleTapToZoomEnabled(false);
        binding.dataChart.setScaleEnabled(false);
        binding.dataChart.setPinchZoom(false);
        binding.dataChart.invalidate();

        mainModel.setUserInfoData();

        adapter.add(mainModel.getDefaultDateData());
    }

}
