package com.harreke.easyappframework.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyappframework.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 消息视图
 *
 * 此视图可以显示三种消息：正在加载、加载出错和无内容，用于提示页面运作状态
 */
public class InfoView extends LinearLayout {
    /**
     * 空内容
     */
    public static final int INFO_EMPTY = 2;
    /**
     * 加载出错
     */
    public static final int INFO_ERROR = 3;
    /**
     * 隐藏
     */
    public static final int INFO_HIDE = 0;
    /**
     * 正在加载
     */
    public static final int INFO_LOADING = 1;

    private ImageView info_empty;
    private ImageView info_error;
    private ImageView info_loading;
    private TextView info_retry;
    private TextView info_text;
    private AnimationDrawable mDrawable;
    private String mEmptyText;
    private boolean mShowRetryWhenEmpty;

    public InfoView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.infoViewStyle);
    }

    public InfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;
        LayoutParams params;
        int retryColor;
        int textColor;
        int textSize;

        style = context.obtainStyledAttributes(attrs, R.styleable.InfoView, defStyle, 0);
        mEmptyText = style.getString(R.styleable.InfoView_emptyText);
        retryColor = style.getColor(R.styleable.InfoView_retryColor, 0);
        mShowRetryWhenEmpty = style.getBoolean(R.styleable.InfoView_showRetryWhenEmpty, false);
        textColor = style.getColor(R.styleable.InfoView_textColor, 0);
        textSize = (int) style.getDimension(R.styleable.InfoView_textSize, 0);
        style.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        info_loading = new ImageView(context);
        info_empty = new ImageView(context);
        info_error = new ImageView(context);
        info_text = new TextView(context);
        info_retry = new TextView(context);

        info_loading.setLayoutParams(new LayoutParams(textSize, textSize));
        info_loading.setImageResource(R.drawable.anim_loading);
        info_loading.setVisibility(GONE);
        mDrawable = (AnimationDrawable) info_loading.getDrawable();
        addView(info_loading);

        info_empty.setLayoutParams(new LayoutParams(textSize, textSize));
        info_empty.setImageResource(R.drawable.image_info_small);
        addView(info_empty);

        info_error.setLayoutParams(new LayoutParams(textSize, textSize));
        info_error.setImageResource(R.drawable.image_error_small);
        info_error.setVisibility(GONE);
        addView(info_error);

        params = new LayoutParams(-2, -2);
        info_text.setLayoutParams(params);
        info_text.setText(mEmptyText);
        info_text.setTextColor(textColor);
        info_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(info_text);

        info_retry.setLayoutParams(new LayoutParams(-2, -2));
        info_retry.setText(R.string.list_retry);
        info_retry.setTextColor(retryColor);
        info_retry.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        if (!mShowRetryWhenEmpty) {
            info_retry.setVisibility(GONE);
        }
        addView(info_retry);
    }

    private void hide() {
        setVisibility(GONE);
    }

    /**
     * 判断是否正在显示“重试”按钮
     *
     * @return 是否正在显示“重试”按钮
     */
    public final boolean isShowingRetry() {
        return info_retry.getVisibility() == VISIBLE;
    }

    /**
     * 设置消息视图可见方式
     *
     * @param infoVisibility
     *         可见方式
     *         {@link #INFO_HIDE}
     *         {@link #INFO_LOADING}
     *         {@link #INFO_EMPTY}
     *         {@link #INFO_ERROR}
     */
    public final void setInfoVisibility(int infoVisibility) {
        switch (infoVisibility) {
            case INFO_HIDE:
                hide();
                break;
            case INFO_LOADING:
                showLoading();
                break;
            case INFO_EMPTY:
                showEmpty();
                break;
            case INFO_ERROR:
                showError();
        }
    }

    /**
     * 设置“空内容”时是否显示“重试”按钮
     *
     * @param showRetryWhenEmpty
     *         “空内容”时是否需要显示“重试”按钮
     */
    public final void setShowRetryWhenEmpty(boolean showRetryWhenEmpty) {
        if (mShowRetryWhenEmpty != showRetryWhenEmpty) {
            mShowRetryWhenEmpty = showRetryWhenEmpty;
            if (showRetryWhenEmpty) {
                info_retry.setVisibility(View.VISIBLE);
            } else {
                info_retry.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示“空内容”消息
     */
    private void showEmpty() {
        setVisibility(VISIBLE);
        mDrawable.stop();
        info_loading.setVisibility(View.GONE);
        info_empty.setVisibility(View.VISIBLE);
        info_error.setVisibility(View.GONE);
        info_text.setText(mEmptyText);
        if (mShowRetryWhenEmpty) {
            info_retry.setVisibility(View.VISIBLE);
        } else {
            info_retry.setVisibility(View.GONE);
        }
    }

    /**
     * 显示“加载出错”消息
     */
    private void showError() {
        setVisibility(VISIBLE);
        mDrawable.stop();
        info_loading.setVisibility(View.GONE);
        info_empty.setVisibility(View.GONE);
        info_error.setVisibility(View.VISIBLE);
        info_text.setText(R.string.list_error);
        info_retry.setVisibility(View.VISIBLE);
    }

    /**
     * 显示“正在加载”消息
     */
    private void showLoading() {
        setVisibility(VISIBLE);
        mDrawable.start();
        info_loading.setVisibility(View.VISIBLE);
        info_empty.setVisibility(View.GONE);
        info_error.setVisibility(View.GONE);
        info_text.setText(R.string.list_loading);
        info_retry.setVisibility(View.GONE);
    }
}