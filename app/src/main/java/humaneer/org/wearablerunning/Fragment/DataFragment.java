package humaneer.org.wearablerunning.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.LineData;

import humaneer.org.wearablerunning.CustomPreferenceManager;
import humaneer.org.wearablerunning.DataRecyclerViewAdapter;
import humaneer.org.wearablerunning.Model.MainModel;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.databinding.FragmentDataBinding;

public class DataFragment extends Fragment {

    private FragmentDataBinding binding;
    private MainModel mainModel;

    private RecyclerView.LayoutManager mLayoutManager;
    private DataRecyclerViewAdapter adapter;

    double distance = 0.0;
    int time = 0;
    double speed = 0.0;


    public DataFragment() {
        // Required empty public constructor
    }

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding = FragmentDataBinding.bind(getView());

        // RecyclerView에 LayoutManager 할당
        binding.recyclerViewDate.setLayoutManager(mLayoutManager);

        Log.d("### DataFragment", "onActivityCreated");
        adapter = new DataRecyclerViewAdapter(getContext());
        Log.d("### adapter null", "");

        binding.recyclerViewDate.setAdapter(adapter);

        if(CustomPreferenceManager.isAlreadyRun(getContext())) {
            mainModel = new MainModel();


            LineData lineData1 = new LineData(mainModel.getDataSetCurrent());
            binding.dataChart.setData(lineData1);
            binding.dataChart.invalidate();


            LineData lineData2 = new LineData(mainModel.getDataSetGoal());
            binding.dataChart.setData(lineData2);
            binding.dataChart.invalidate();


            adapter.add(mainModel.getDefaultDateData());
        }
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

}
