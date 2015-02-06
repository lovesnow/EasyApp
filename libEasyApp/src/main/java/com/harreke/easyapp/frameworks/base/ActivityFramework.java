package com.harreke.easyapp.frameworks.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.R;
import com.harreke.easyapp.helpers.ConnectionHelper;
import com.harreke.easyapp.helpers.RequestHelper;
import com.harreke.easyapp.helpers.ToastHelper;
import com.harreke.easyapp.listeners.OnTransitionListener;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.utils.NetUtil;
import com.harreke.easyapp.widgets.transitions.TransitionLayout;
import com.harreke.easyapp.widgets.transitions.TransitionOptions;

import butterknife.ButterKnife;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Activity框架
 */
public abstract class ActivityFramework extends ActionBarActivity
        implements IFramework, IToolbar, Toolbar.OnMenuItemClickListener {
    private static final String TAG = "ActivityFramework";
    private Runnable mBackPressedRunnable = new Runnable() {
        @Override
        public void run() {
            onBackPressed();
        }
    };
    private BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectionHelper.checkConnection(context);
        }
    };
    private Handler mHandler = new Handler();
    private Menu mMenu;
    private View.OnClickListener mOnNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onToolbarNavigationClick();
        }
    };
    private OnTransitionListener mOnTransitionListener = new OnTransitionListener() {
        @Override
        public void onEnter() {
            startAction();
        }

        @Override
        public void onExit() {
            exit();
        }
    };
    private RequestHelper mRequest = new RequestHelper();
    private ToastHelper mToastHelper;
    private Toolbar mToolbar;
    private TransitionLayout mTransitionLayout = null;
    private TransitionOptions mTransitionOptions;

    /**
     * 初始化Activity传参数据
     */
    protected abstract void acquireArguments(Intent intent);

    @Override
    public void addToolbarItem(int id, int titleId, int imageId) {
        MenuItem item = mMenu.add(0, id, id, titleId);

        item.setIcon(imageId);

        if (Build.VERSION.SDK_INT > 11) {
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    @Override
    public void addToolbarViewItem(int id, int titleId, View view) {
        MenuItem item;

        if (Build.VERSION.SDK_INT >= 11) {
            item = mMenu.add(0, id, id, titleId);
            item.setActionView(view);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            Log.e(TAG, "Sdk version too low to call this method!");
        }
    }

    private void attachToolbar(int toolbarSolidId) {
        mToolbar = (Toolbar) findViewById(toolbarSolidId);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(mOnNavigationClickListener);
            mToolbar.setOnMenuItemClickListener(this);
        }
    }

    private void attachTransitionLayout(TransitionLayout transitionLayout) {
        mTransitionLayout = transitionLayout;
    }

    //    /**
    //     * 退出Activity
    //     */
    //    public final void exit(ActivityAnimation animation) {
    //        finish();
    //        if (animation != ActivityAnimation.Default) {
    //            overridePendingTransition(animation.getEnterAnim(), animation.getExitAnim());
    //        }
    //    }

    /**
     * 取消正在执行的Http请求
     */
    @Override
    public void cancelRequest() {
        mRequest.cancel();
    }

    /**
     * 初始化Activity配置信息
     *
     * 如设置屏幕样式、屏幕亮度、是否全屏、过渡动画等
     */
    protected void configActivity() {
    }

    protected abstract void createMenu();

    @Override
    public void enableDefaultToolbarNavigation() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.image_toolbar_back);
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
    public void executeRequest(RequestBuilder builder, IRequestCallback<String> callback) {
        builder.print();
        mRequest.execute(this, builder, callback);
    }

    public final void exit() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.none, mTransitionOptions.exitAnimation);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 获得当前Activity
     *
     * @return 当前Activity
     */
    @Override
    public Context getContext() {
        return this;
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

    public Toolbar getToolBar() {
        return mToolbar;
    }

    protected int getToolbarSolidId() {
        return R.id.toolbar_solid;
    }

    public TransitionLayout getTransitionLayout() {
        return mTransitionLayout;
    }

    /**
     * 隐藏Toast
     */
    @Override
    public final void hideToast() {
        mToastHelper.hide();
    }

    @Override
    public void hideToolbar() {
        if (mToolbar != null) {
            mToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideToolbarItem(int id) {
        MenuItem item;

        if (mToolbar != null) {
            item = mMenu.findItem(id);
            if (item != null) {
                item.setVisible(false);
            }
        }
    }

    private void initToast() {
        mToastHelper = new ToastHelper(mTransitionLayout);
    }

    /**
     * 隐藏Toast
     */
    @Override
    public final boolean isRequestExecuting() {
        return mRequest.isExecuting();
    }

    @Override
    public boolean isToolbarShowing() {
        return mToolbar != null && mToolbar.getVisibility() == View.VISIBLE;
    }

    protected TransitionLayout makeTransitionLayout() {
        return new TransitionLayout(this);
    }

    public void onBackPressed(long delay) {
        mHandler.removeCallbacks(mBackPressedRunnable);
        mHandler.postDelayed(mBackPressedRunnable, delay);
    }

    @Override
    public void onBackPressed() {
        mTransitionLayout.startExitTransition(mTransitionOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetUtil.checkConnection(this);
        configActivity();
        attachTransitionLayout(makeTransitionLayout());
        mTransitionLayout.setOnTransitionListener(mOnTransitionListener);
        super.setContentView(mTransitionLayout);
        initToast();
        setContentView(getLayoutId());
        ButterKnife.inject(this);
        attachToolbar(getToolbarSolidId());
        acquireArguments(getIntent());
        mTransitionOptions = TransitionOptions.fromBundle(getIntent().getBundleExtra("transition"));
        establishCallbacks();
        enquiryViews();
        attachCallbacks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        createMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        cancelRequest();
        mToastHelper.destroy();
        mHandler.removeCallbacks(mBackPressedRunnable);
        mBackPressedRunnable = null;
        if (mTransitionLayout != null) {
            mTransitionLayout.destroy();
            mTransitionLayout = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    protected void onPause() {
        unregisterConnectionReceiver();
        Log.e(null, "on pause activity " + getClass().getSimpleName());
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        postCreate();
        mTransitionLayout.startEnterTransition(this, mTransitionOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(null, "on resume activity " + getClass().getSimpleName());
        registerConnectionReceiver();
    }

    protected void onToolbarNavigationClick() {
        onBackPressed();
    }

    protected void postCreate() {
    }

    private void registerConnectionReceiver() {
        registerReceiver(mConnectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void setContentView(int layoutId) {
        mTransitionLayout.setContentView(layoutId);
    }

    @Override
    public void setContentView(View view) {
        mTransitionLayout.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mTransitionLayout.setContentView(view, params);
    }

    @Override
    public void setToolbarNavigation(int imageId) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(imageId);
        }
    }

    public void setToolbarSubTitle(int titleId) {
        if (mToolbar != null) {
            mToolbar.setSubtitle(titleId);
        }
    }

    public void setToolbarSubTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setSubtitle(title);
        }
    }

    @Override
    public void setToolbarTitle(int textId) {
        if (mToolbar != null) {
            mToolbar.setTitle(textId);
        }
    }

    @Override
    public void setToolbarTitle(String text) {
        if (mToolbar != null) {
            mToolbar.setTitle(text);
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
     * @param indeterminate
     *         是否显示进度条
     */
    @Override
    public void showToast(String text, boolean indeterminate) {
        mToastHelper.show(text, indeterminate);
    }

    /**
     * 显示Toast
     *
     * @param textId
     *         文本Id
     * @param indeterminate
     *         是否显示进度条
     */
    @Override
    public final void showToast(int textId, boolean indeterminate) {
        showToast(getString(textId), indeterminate);
    }

    @Override
    public void showToolbar() {
        if (mToolbar != null) {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showToolbarItem(int id) {
        MenuItem item;

        if (mToolbar != null) {
            item = mMenu.findItem(id);
            if (item != null) {
                item.setVisible(true);
            }
        }
    }

    @Override
    public void start(Intent intent) {
        start(intent, -1);
    }

    public void start(Intent intent, TransitionOptions options) {
        start(intent, -1, options);
    }

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     * @param requestCode
     *         请求代码
     */
    @Override
    public void start(Intent intent, int requestCode) {
        start(intent, requestCode, TransitionOptions.makeCustomTransition(R.anim.slide_in_right, R.anim.slide_out_right));
    }

    public void start(Intent intent, int requestCode, TransitionOptions options) {
        hideToast();
        intent.putExtra("transition", options.toBundle());
        startActivityForResult(intent, requestCode);
        overridePendingTransition(options.enterAnimation, R.anim.none);
    }

    private void unregisterConnectionReceiver() {
        unregisterReceiver(mConnectionReceiver);
    }
}