package humaneer.org.wearablerunning;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Minki on 2017-04-13.
 */

public class DataRecyclerViewAdapter extends RecyclerView.Adapter<DataRecyclerViewAdapter.ViewHolder>{


    List<MyItem> mItems = new ArrayList<MyItem>();;
//    OnItemClickListener mListener;

    @Override
    public DataRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_date, parent, false);
        Log.d("### Adaptor", "onCreateViewHolder");


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.weekdayTextView.setText(mItems.get(position).getDayOfWeek()+ "");
        holder.progressBar.setProgress(mItems.get(position).getPercentage());
        holder.percentageTextView.setText(mItems.get(position).getPercentage()+"");
//        holder.setItemData(mItems.get(position));
//        holder.setOnItemClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnItemClickListener {
        public void onItemClicked(ViewHolder holder, View view, MyItem data, int position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView percentageTextView;
        TextView weekdayTextView;
        ProgressBar progressBar;
        MyItem mData;
        OnItemClickListener mItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("### ViewHolder", "ViewHolder");

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

//        public void setItemData(MyItem data) {
//            mData = data;
//            weekdayTextView.setText(data.getDate());
//            percentageTextView.setText(data.getPercentage());
//        }
//
//        public void setOnItemClickListener(OnItemClickListener listener) {
//            mItemClickListener = listener;
//        }
    }

    public void add(List<MyItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

}
