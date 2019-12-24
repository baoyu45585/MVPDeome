//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.uiframework.inface;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class SpaceItemDecoration extends ItemDecoration {
    private final int space;
    private boolean mIsIncludeLeft = true;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int space, boolean isIncludeLeft) {
        this.space = space;
        this.mIsIncludeLeft = isIncludeLeft;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        if (this.mIsIncludeLeft && parent.getChildPosition(view) == 0) {
            outRect.left = this.space;
        }

        outRect.right = this.space;
    }
}
