package tv.douyu.wrapper.helper;

import android.view.View;
import android.widget.EditText;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.helpers.ViewSwitchHelper;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;

import tv.douyu.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/25
 */
public abstract class PlayerControlBottomHelper implements View.OnClickListener, View.OnFocusChangeListener {
    private ToggleViewValueAnimator mBottomAnimator;
    private boolean mDanmakuShowing = true;
    private ViewSwitchHelper mDanmakuSwitchHelper;
    private ViewSwitchHelper mLockSwitchHelper;
    private boolean mLocked = false;
    private boolean mPlayShowing = false;
    private ViewSwitchHelper mPlaySwitchHelper;
    private View mRootView;
    private View player_control_bottom;
    private View player_danmaku_cancel;
    private View player_danmaku_hotwords;
    private EditText player_danmaku_input;
    private View player_danmaku_off;
    private View player_danmaku_on;
    private View player_danmaku_send;
    private View player_lock_off;
    private View player_lock_on;
    private View player_pause;
    private View player_play;

    public PlayerControlBottomHelper(View rootView) {
        mRootView = rootView;
        player_control_bottom = mRootView.findViewById(R.id.player_control_bottom);
        player_play = mRootView.findViewById(R.id.player_play);
        player_pause = mRootView.findViewById(R.id.player_pause);
        player_lock_on = mRootView.findViewById(R.id.player_lock_on);
        player_lock_off = mRootView.findViewById(R.id.player_lock_off);
        player_danmaku_on = mRootView.findViewById(R.id.player_danmaku_on);
        player_danmaku_off = mRootView.findViewById(R.id.player_danmaku_off);
        player_danmaku_cancel = mRootView.findViewById(R.id.player_danmaku_cancel);
        player_danmaku_input = (EditText) mRootView.findViewById(R.id.player_danmaku_input);
        player_danmaku_hotwords = mRootView.findViewById(R.id.player_danmaku_hotwords);
        player_danmaku_send = mRootView.findViewById(R.id.player_danmaku_send);

        mBottomAnimator = ToggleViewValueAnimator.animate(player_control_bottom);
        mPlaySwitchHelper = new ViewSwitchHelper(player_play, player_pause);
        mDanmakuSwitchHelper = new ViewSwitchHelper(player_danmaku_on, player_danmaku_off);
        mLockSwitchHelper = new ViewSwitchHelper(player_lock_on, player_lock_off);

        RippleDrawable.attach(player_play, RippleStyle.Light_Square);
        RippleDrawable.attach(player_pause, RippleStyle.Light_Square);
        RippleDrawable.attach(player_lock_on, RippleStyle.Light_Square);
        RippleDrawable.attach(player_lock_off, RippleStyle.Light_Square);
        RippleDrawable.attach(player_danmaku_on, RippleStyle.Light_Square);
        RippleDrawable.attach(player_danmaku_off, RippleStyle.Light_Square);
        RippleDrawable.attach(player_danmaku_cancel, RippleStyle.Dark_Square);
        RippleDrawable.attach(player_danmaku_hotwords, RippleStyle.Light);
        RippleDrawable.attach(player_danmaku_send, RippleStyle.Light);

        player_play.setOnClickListener(this);
        player_pause.setOnClickListener(this);
        player_lock_on.setOnClickListener(this);
        player_lock_off.setOnClickListener(this);
        player_danmaku_on.setOnClickListener(this);
        player_danmaku_off.setOnClickListener(this);
        player_danmaku_cancel.setOnClickListener(this);
        player_danmaku_input.setOnClickListener(this);
        player_danmaku_hotwords.setOnClickListener(this);
        player_danmaku_send.setOnClickListener(this);
        player_danmaku_input.setOnFocusChangeListener(this);

        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mBottomAnimator.yOff(mRootView.getMeasuredHeight())
                        .yOn(mRootView.getMeasuredHeight() - player_control_bottom.getMeasuredHeight()).alphaOff(0f).alphaOn(1f)
                        .visibilityOff(View.GONE).visibilityOn(View.VISIBLE);
                hide(false);
            }
        });
    }

    public void clearDanmakuInput() {
        setInputText("");
    }

    public String getInputText() {
        return player_danmaku_input.getText().toString().trim();
    }

    public void hide(boolean animate) {
        mBottomAnimator.toggleOff(animate);
    }

    public void hideDanmaku(boolean animate) {
        mDanmakuShowing = false;
        clearDanmakuInput();
        mDanmakuSwitchHelper.switchToView(animate, player_danmaku_on);
    }

    public boolean isDanmakuShowing() {
        return mDanmakuShowing;
    }

    public boolean isLocked() {
        return mLocked;
    }

    public boolean isPlayShowing() {
        return mPlayShowing;
    }

    public boolean isShowing() {
        return mBottomAnimator.isToggledOn();
    }

    public void lock(boolean animate) {
        mLocked = true;
        mLockSwitchHelper.switchToView(animate, player_lock_off);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_play:
                showPause(true);
                onPlayClick();
                break;
            case R.id.player_pause:
                showPlay(true);
                onPauseClick();
                break;
            case R.id.player_lock_on:
                lock(true);
                onLockOnClick();
                break;
            case R.id.player_lock_off:
                unlock(true);
                onLockOffClick();
                break;
            case R.id.player_danmaku_on:
                showDanmaku(true);
                onDanmakuOnClick();
                break;
            case R.id.player_danmaku_off:
                hideDanmaku(true);
                onDanmakuOffClick();
                break;
            case R.id.player_danmaku_cancel:
                clearDanmakuInput();
                player_danmaku_input.clearFocus();
                break;
            case R.id.player_danmaku_hotwords:
                onDanmakuHotWordsClick();
                break;
            case R.id.player_danmaku_send:
                onDanmakuSendClick();
                break;
        }
    }

    protected abstract void onDanmakuClearFocus();

    protected abstract void onDanmakuHotWordsClick();

    protected abstract void onDanmakuOffClick();

    protected abstract void onDanmakuOnClick();

    protected abstract void onDanmakuRequestFocus();

    protected abstract void onDanmakuSendClick();

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            onDanmakuRequestFocus();
        } else {
            onDanmakuClearFocus();
        }
    }

    protected abstract void onLockOffClick();

    protected abstract void onLockOnClick();

    protected abstract void onPauseClick();

    protected abstract void onPlayClick();

    public void setInputText(String text) {
        player_danmaku_input.setText(text);
        player_danmaku_input.setSelection(text.length());
    }

    public void show(boolean animate) {
        mBottomAnimator.toggleOn(animate);
    }

    public void showDanmaku(boolean animate) {
        mDanmakuShowing = true;
        mDanmakuSwitchHelper.switchToView(animate, player_danmaku_off);
    }

    public void showPause(boolean animate) {
        mPlayShowing = false;
        mPlaySwitchHelper.switchToView(animate, player_pause);
    }

    public void showPlay(boolean animate) {
        mPlayShowing = true;
        mPlaySwitchHelper.switchToView(animate, player_play);
    }

    public void toggleDanmaku() {
        if (isDanmakuShowing()) {
            hideDanmaku(true);
        } else {
            showDanmaku(true);
        }
    }

    public void toggleLock() {
        if (mLocked) {
            unlock(true);
        } else {
            lock(true);
        }
    }

    public void toggleLockShow() {
        if (player_lock_off.getVisibility() == View.VISIBLE) {
            player_lock_off.setVisibility(View.GONE);
        } else {
            player_lock_off.setVisibility(View.VISIBLE);
        }
    }

    public void togglePlay() {
        if (isPlayShowing()) {
            showPause(true);
        } else {
            showPlay(true);
        }
    }

    public void toggleShow() {
        if (isShowing()) {
            hide(true);
        } else {
            show(true);
        }
    }

    public void unlock(boolean animate) {
        mLocked = false;
        mLockSwitchHelper.switchToView(animate, player_lock_on);
    }
}