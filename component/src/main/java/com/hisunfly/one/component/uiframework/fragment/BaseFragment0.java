//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.uiframework.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.Field;

public class BaseFragment0 extends RxFragment {
    protected LayoutInflater inflater;
    private View contentView;
    private Context context;
    private ViewGroup container;

    public BaseFragment0() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity().getApplicationContext();
    }

    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        this.onCreateView(savedInstanceState);
        return this.contentView == null ? super.onCreateView(inflater, container, savedInstanceState) : this.contentView;
    }

    protected void onCreateView(Bundle savedInstanceState) {
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.contentView = null;
        this.container = null;
        this.inflater = null;
    }

    public Context getApplicationContext() {
        return this.context;
    }

    public void setContentView(int layoutResID) {
        this.setContentView((ViewGroup)this.inflater.inflate(layoutResID, this.container, false));
    }

    public void setContentView(View view) {
        this.contentView = view;
    }

    public View getContentView0() {
        return this.contentView;
    }

    public <T extends View> T findViewById(int id) {
        return this.contentView != null ? (T) this.contentView.findViewById(id) : null;
    }

    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, (Object)null);
        } catch (NoSuchFieldException var2) {
            throw new RuntimeException(var2);
        } catch (IllegalAccessException var3) {
            throw new RuntimeException(var3);
        }
    }
}
