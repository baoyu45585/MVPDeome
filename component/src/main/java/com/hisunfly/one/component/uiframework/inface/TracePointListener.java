package com.hisunfly.one.component.uiframework.inface;

import java.util.HashMap;

/**
 * description:资源位点击，方便不是直接点击事件，如：1.滑动；2.需要先判断登录等的方法调用
 *
 * @author: 罗明颖
 * @Create Time:2018/9/13
 */

public interface TracePointListener {
    /**
     * 获取页面埋点需要的 id
     * @return
     */
    String getTraceTargetID();

    /**
     * 资源位点击切面方法
     * @param parameter
     */
    void onTracePoint(HashMap<Integer, String> parameter);
}
