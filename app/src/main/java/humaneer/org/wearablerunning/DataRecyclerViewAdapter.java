package humaneer.org.wearablerunning;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import humaneer.org.wearablerunning.Model.UserVO;

/**
 * Created by Minki on 2017-04-13.
 */

public class DataRecyclerViewAdapter extends RecyclerView.Adapter<DataRecyclerViewAdapter.ViewHolder>{


    private List<UserVO> mItems = new ArrayList<UserVO>();
    private Context mContext;

    public Context getContext() {return mContext;}
//    OnItemClickListener mListener;

    public DataRecyclerViewAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public DataRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_date, parent, false);
        Log.d("### Adaptor", "onCreateViewHolder");


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int progress = (int)mItems.get(position).getPercentage();
        holder.weekdayTextView.setText(mItems.get(position).getDayOfWeek()+ "");
        holder.progressBar.setProgress(progress);
        holder.percentageTextView.setText(mItems.get(position).getPercentage()+"");
//        holder.setItemData(mItems.get(position));
//        holder.setOnItemClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnItemClickListener {
        public void onItemClicked(ViewHolder holder, View view, UserVO data, int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView percentageTextView;
        TextView weekdayTextView;
        ProgressBar progressBar;
        UserVO mData;
        OnItemClickListener mItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            weekdayTextView = (TextView) itemView.findViewById(R.id.weekdayTextView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            percentageTextView = (TextView) itemView.findViewById(R.id.percentageTextView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if(mItemClickListener != null) {
//                        mItemClickListener.onItemClicked(ViewHolder.this, v, mData, position);
//                    }
//                }
//            });
        }

//        public void setItemData(UserVO data) {
//            mData = data;
//            weekdayTextView.setText(data.getDate());
//            percentageTextView.setText(data.getPercentage());
//        }
//
//        public void setOnItemClickListener(OnItemClickListener listener) {
//            mItemClickListener = listener;
//        }
    }

    public void add(List<UserVO> items) {
        mItems = items;
        notifyDataSetChanged();
    }

}
