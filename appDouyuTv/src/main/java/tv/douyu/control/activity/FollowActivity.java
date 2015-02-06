package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.GridItemDecoration;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.model.bean.Room;
import tv.douyu.wrapper.holder.RoomHolder;
import tv.douyu.model.parser.RoomListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class FollowActivity extends ActivityFramework {
    private FollowRecyclerHelper mFollowRecyclerHelper;

    public static Intent create(Context context) {
        return new Intent(context, FollowActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.app_follow);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        mFollowRecyclerHelper = new FollowRecyclerHelper(this);
        mFollowRecyclerHelper.setHasFixedSize(true);
        mFollowRecyclerHelper.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mFollowRecyclerHelper.setItemDecoration(new GridItemDecoration(2));
        mFollowRecyclerHelper.setAutoLoadMore(DouyuTv.getInstance().getSetting().isAutoLoadMore());
        mFollowRecyclerHelper.setListParser(new RoomListParser());
        mFollowRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        start(SearchActivity.create(this));

        return false;
    }

    @Override
    public void startAction() {
        mFollowRecyclerHelper.from(API.getFollow(20, mFollowRecyclerHelper.getCurrentPage()));
    }

    private class FollowRecyclerHelper extends RecyclerFramework<Room> {
        public FollowRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Room> createHolder(View itemView, int viewType) {
            return new RoomHolder(itemView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_room, parent, false);
        }

        @Override
        public void onItemClick(int position, Room room) {
            start(RoomActivity.create(getContext(), room.getRoom_id()));
        }
    }
}
