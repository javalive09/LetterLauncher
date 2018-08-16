package com.javalive09.letterlauncher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AppGroupRecyclerView extends RecyclerView {

    private boolean isScrollEnable = true;

    public AppGroupRecyclerView(@NonNull Context context) {
        super(context);
    }

    public AppGroupRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppGroupRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollEnable(boolean enable) {
        this.isScrollEnable = enable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isScrollEnable) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

}
