package com.javalive09.letterlauncher.adapter;

import java.util.List;

import com.javalive09.letterlauncher.BR;
import com.javalive09.letterlauncher.R;
import com.javalive09.letterlauncher.databinding.ListItemLayoutBinding;
import com.javalive09.letterlauncher.mode.AppGroup;
import com.javalive09.letterlauncher.mode.AppModel;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by peter on 2019/2/13
 */
public class AppGroupsAdapter extends BaseRecycleAdapter<AppGroup, ListItemLayoutBinding> {

    public AppGroupsAdapter(List<AppGroup> list) {
        super(list);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.list_item_layout;
    }

    @Override
    protected void onBind(Holder<ListItemLayoutBinding> holder, AppGroup appGroup, int position) {
        holder.dataBinding.setVariable(BR.appGroup, appGroup);
        AppAdapter adapter = (AppAdapter) holder.dataBinding.gridView.getAdapter();
        List<AppModel> appModelList = appGroup.getAppModelList();
        if(adapter == null) {
            adapter = new AppAdapter(appModelList);
            holder.dataBinding.gridView.setAdapter(adapter);
            holder.dataBinding.gridView
                    .setLayoutManager(new GridLayoutManager(holder.dataBinding.gridView.getContext(), 5));
        }
        adapter.refresh(appModelList);
        holder.dataBinding.executePendingBindings();
    }

}
