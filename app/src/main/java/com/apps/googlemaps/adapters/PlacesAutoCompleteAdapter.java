package com.apps.googlemaps.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.googlemaps.R;
import com.apps.googlemaps.models.MapLocation;

import java.util.ArrayList;

public class PlacesAutoCompleteAdapter extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.MyViewHolder> implements Filterable {

    private FragmentActivity mActivity;
    private ArrayList<MapLocation> mArrayList;
    private ArrayList<MapLocation> mArrayList1;
    private CallBackListener mListener;

    public interface CallBackListener {
        void onRecyclerViewItems(MapLocation place);
    }

    public PlacesAutoCompleteAdapter(FragmentActivity activity, ArrayList<MapLocation> arrayList, CallBackListener listener) {
        if (mArrayList != null) {
            mArrayList.clear();
            mArrayList1.clear();
        }
        this.mActivity = activity;
        this.mArrayList = arrayList;
        this.mArrayList1 = arrayList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MapLocation model = mArrayList.get(position);

        holder.itemPlace.setText(model.getPlace());
        holder.itemAddress.setText(model.getAddress());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onRecyclerViewItems(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mArrayList != null) ? mArrayList.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView itemPlace, itemAddress;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
            itemPlace = (TextView) itemView.findViewById(R.id.item_place);
            itemAddress = (TextView) itemView.findViewById(R.id.item_address);
        }
    }

    //http://programmingroot.com/android-recyclerview-search-filter-tutorial-with-example/
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()){
                    mArrayList = mArrayList1;
                } else {
                    ArrayList<MapLocation> filterList = new ArrayList<>();
                    for (MapLocation model : mArrayList1){
                        if (model.getPlace().toLowerCase().contains(charString.toLowerCase())){
                            filterList.add(model);
                        }
                    }
                    mArrayList = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mArrayList = (ArrayList<MapLocation>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
