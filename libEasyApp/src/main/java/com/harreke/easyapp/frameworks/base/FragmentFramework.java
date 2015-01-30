package com.harreke.easyapp.frameworks.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.enums.ActivityAnimation;
import com.harreke.easyapp.helpers.RequestHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;

import butterknife.ButterKnife;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Fragment框架
 */
public abstract class FragmentFramework extends Fragment implements IFramework, IToolbar {
    private static final String TAG = "FragmentFramework";
    private IFramework mActivityFramework = null;
    private IToolbar mActivityToolbar = null;
    private View mContentView;
    private RequestHelper mRequest = new RequestHelper();

    public FragmentFramework() {
    }

    /**
     * 初始化Bundle传参数据
     */
    protected abstract void acquireArguments(Bundle bundle);

    /**
     * 布局新增视图
     *
     * @param view
     *         视图
     * @param params
     *         布局参数
     */
    @Override
    public final void addContentView(View view, ViewGroup.LayoutParams params) {
        if (mContentView instanceof ViewGroup) {
            ((ViewGroup) mContentView).addView(view, params);
        }
    }

    @Override
    public void addToolbarItem(int id, int titleId, int imageId) {
        if (mActivityToolbar != null) {
            mActivityToolbar.addToolbarItem(id, titleId, imageId);
        }
    }

    @Override
    public void addToolbarViewItem(int id, int titleId, View view) {
        if (mActivityToolbar != null) {
            mActivityToolbar.addToolbarViewItem(id, titleId, view);
        }
    }

    /**
     * 取消正在执行的Http请求
     */
    @Override
    public final void cancelRequest() {
        mRequest.cancel();
    }

    @Override
    public void enableDefaultToolbarNavigation() {
        if (mActivityToolbar != null) {
            mActivityToolbar.enableDefaultToolbarNavigation();
        }
    }

    /**
     * 执行一个Http请求
     *
     * 注：同一时间只能执行一个请求，新增请求前会先取消正在执行的请求
     *
     * @param builder
     *         Http请求
     * @param callback
     *         Http请求回调
     */
    @Override
    public final void executeRequest(RequestBuilder builder, IRequestCallback<String> callback) {
        builder.print();
        mRequest.execute(getContext(), builder, callback);
    }

    @Override
    public View findViewById(int viewId) {
        return mContentView.findViewById(viewId);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    /**
     * 获得框架
     *
     * @return 框架
     */
    @Override
    public final IFramework getFramework() {
        return this;
    }

    /**
     * 隐藏Toast
     */
    @Override
    public final void hideToast() {
        if (mActivityFramework != null) {
            mActivityFramework.hideToast();
        }
    }

    @Override
    public void hideToolbar() {
        if (mActivityToolbar != null) {
            mActivityToolbar.hideToolbar();
        }
    }

    @Override
    public void hideToolbarItem(int id) {
        if (mActivityToolbar != null) {
            mActivityToolbar.hideToolbarItem(id);
        }
    }

    /**
     * 是否正在执行一个Http请求
     *
     * @return boolean
     */
    @Override
    public boolean isRequestExecuting() {
        return mRequest.isExecuting();
    }

    @Override
    public boolean isToolbarShowing() {
        return mActivityToolbar != null && mActivityToolbar.isToolbarShowing();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityFramework = (IFramework) activity;
        mActivityToolbar = (IToolbar) activity;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.inject(this, mContentView);
        acquireArguments(getArguments());
        establishCallbacks();
        enquiryViews();
        attachCallbacks();

        return mContentView;
    }

    @Override
    public void onDestroyView() {
        cancelRequest();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mActivityFramework = null;
        mActivityToolbar = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        postCreate();
        startAction();
    }

    protected void postCreate() {
    }

    @Override
    public void setToolbarNavigation(int imageId) {
        if (mActivityToolbar != null) {
            mActivityToolbar.setToolbarNavigation(imageId);
        }
    }

    @Override
    public void setToolbarTitle(String text) {
        if (mActivityToolbar != null) {
            mActivityToolbar.setToolbarTitle(text);
        }
    }

    @Override
    public void setToolbarTitle(int textId) {
        if (mActivityToolbar != null) {
            mActivityToolbar.setToolbarTitle(textId);
        }
    }

    /**
     * 显示Toast
     *
     * @param text
     *         文本
     */
    @Override
    public final void showToast(String text) {
        showToast(text, false);
    }

    /**
     * 显示Toast
     *
     * @param textId
     *         文本
     */
    @Override
    public final void showToast(int textId) {
        showToast(getString(textId));
    }

    /**
     * 显示Toast
     *
     * @param text
     *         文本
     * @param progress
     *         是否显示进度条
     */
    @Override
    public final void showToast(String text, boolean progress) {
        if (mActivityFramework != null) {
            mActivityFramework.showToast(text, progress);
        }
    }

    /**
     * 显示Toast
     *
     * @param textId
     *         文本Id
     * @param progress
     *         是否显示进度条
     */
    @Override
    public final void showToast(int textId, boolean progress) {
        showToast(getString(textId), progress);
    }

    @Override
    public void showToolbar() {
        if (mActivityToolbar != null) {
            mActivityToolbar.showToolbar();
        }
    }

    @Override
    public void showToolbarItem(int id) {
        if (mActivityToolbar != null) {
            mActivityToolbar.showToolbarItem(id);
        }
    }

    @Override
    public void start(Intent intent) {
        start(intent, ActivityAnimation.None);
    }

    @Override
    public void start(Intent intent, ActivityAnimation anim) {
        start(intent, -1, anim);
    }

    @Override
    public void start(Intent intent, int requestCode) {
        start(intent, requestCode, ActivityAnimation.None);
    }

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     * @param requestCode
     *         请求代码
     *
     *         如果需要回调，则设置requestCode为正整数；否则设为-1；
     * @param anim
     *         Intent切换动画
     *
     * @see com.harreke.easyapp.enums.ActivityAnimation
     */
    @Override
    public void start(Intent intent, int requestCode, ActivityAnimation anim) {
        if (mActivityFramework != null && intent != null) {
            hideToast();
            if (requestCode < 0) {
                startActivity(intent);
            } else {
                startActivityForResult(intent, requestCode);
            }
            if (anim != ActivityAnimation.Default) {
                mActivityFramework.getActivity().overridePendingTransition(anim.getEnterAnim(), anim.getExitAnim());
            }
        }
    }
}