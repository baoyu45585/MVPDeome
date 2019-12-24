package com.hisunfly.one.component.refresh;

import android.support.annotation.IntDef;

/**
 * description:刷新控件类型
 *
 * @author: jinyuef
 * @Create Time:2018/4/28
 */
public interface RefreshType {
    /**
     * 无刷新控件
     */
    int NONE = 1;
    /**
     * 刷新与加载更多
     */
    int REFRESH_AND_LOAD_MORE = 2;
    /**
     * 只有刷新
     */
    int REFRESH_ONLY = 3;
    /**
     * 只有加载更多
     */
    int LOAD_MORE_ONLY = 4;

    @IntDef(value = {RefreshType.NONE,RefreshType.REFRESH_AND_LOAD_MORE,RefreshType.REFRESH_ONLY,RefreshType.LOAD_MORE_ONLY})
     @interface Val{

    }
}
