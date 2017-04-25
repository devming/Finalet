package humaneer.org.wearablerunning;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Minki on 2017-04-13.
 */

public class DataRecyclerViewAdapter extends RecyclerView.Adapter<DataRecyclerViewAdapter.ViewHolder>{

    List<MyItem> mItems = new ArrayList<MyItem>();
    OnItemClickListener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setItemData(mItems.get(position));
        holder.setOnItemClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface OnItemClickListener {
        public void onItemClicked(ViewHolder holder, View view, MyItem data, int position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView postingItemTextView;
        MyItem mData;
        OnItemClickListener mItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            postingItemTextView = (TextView) itemView.findViewById(R.id.percentageTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(mItemClickListener != null) {
                        mItemClickListener.onItemClicked(ViewHolder.this, v, mData, position);
                    }
                }
            });
        }

        public void setItemData(MyItem data) {
            mData = data;
            postingItemTextView.setText(data.getPercentage());
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mItemClickListener = listener;
        }
    }

}
