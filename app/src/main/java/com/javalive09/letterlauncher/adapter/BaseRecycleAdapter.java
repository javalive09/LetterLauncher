package com.javalive09.letterlauncher.adapter;

import java.util.List;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by peter on 2019/1/31
 */
public abstract class BaseRecycleAdapter<E, B extends ViewDataBinding>
        extends RecyclerView.Adapter<BaseRecycleAdapter.Holder> {

    private List<E> list;

    public BaseRecycleAdapter(List<E> list) {
        this.list = list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public void refresh(List<E> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    protected abstract int getItemLayoutId(int viewType);

    protected abstract void onBind(Holder<B> holder, E e, int position);

    @NonNull
    @Override
    public Holder<B> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        B b = DataBindingUtil.inflate(inflater, getItemLayoutId(viewType), viewGroup, false);
        return new Holder<>(b);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        E e = list.get(i);
        onBind(holder, e, i);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class Holder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

        public B dataBinding;

        public Holder(B b) {
            super(b.getRoot());
            this.dataBinding = b;
        }
    }

}
