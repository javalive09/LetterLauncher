package com.javalive09.letterlauncher;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AppGroupAdapter extends RecyclerView.Adapter<AppGroupAdapter.Holder> {

    private List<AppGroup> groupDataList;

    AppGroupAdapter(List<AppGroup> groupDataList) {
        this.groupDataList = groupDataList;
    }

    public void refreshData(List<AppGroup> groupDataList) {
        this.groupDataList = groupDataList;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        AppGroup appGroup = groupDataList.get(position);
        holder.letter.setText(appGroup.letter);
        AppModelAdapter adapter = (AppModelAdapter) holder.gridView.getAdapter();
        if (adapter == null) {
            adapter = new AppModelAdapter();
            holder.gridView.setAdapter(adapter);
            Context context = holder.gridView.getContext();
            holder.gridView.setLayoutManager(new GridLayoutManager(context, 5));
        }
        adapter.refreshData(appGroup.appModelList);
    }

    @Override
    public int getItemCount() {
        return groupDataList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView letter;
        RecyclerView gridView;

        Holder(View itemView) {
            super(itemView);
            letter = (TextView) itemView.findViewById(R.id.letter);
            gridView = (RecyclerView) itemView.findViewById(R.id.grid_view);
        }
    }

}
