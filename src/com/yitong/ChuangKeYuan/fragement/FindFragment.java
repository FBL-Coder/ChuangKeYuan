package com.yitong.ChuangKeYuan.fragement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.friendscircle.adapter.CircleAdapter;
import com.yitong.ChuangKeYuan.friendscircle.bean.CircleItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentConfig;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.FavortItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.FindData;
import com.yitong.ChuangKeYuan.friendscircle.mvp.presenter.CirclePresenter;
import com.yitong.ChuangKeYuan.friendscircle.mvp.view.ICircleView;
import com.yitong.ChuangKeYuan.friendscircle.utils.AsyncFindData;
import com.yitong.ChuangKeYuan.friendscircle.utils.CommonUtils;
import com.yitong.ChuangKeYuan.friendscircle.utils.DatasUtil;
import com.yitong.ChuangKeYuan.friendscircle.widgets.CommentListView;
import com.yitong.ChuangKeYuan.friendscircle.widgets.DivItemDecoration;
import com.yitong.ChuangKeYuan.ui.ShareActivity;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.utils.UiUtils;

import java.util.List;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/17.
 * 发现fragment；
 *  此功能是在开源实例的基础上，进行数据、部分功能以及显示修改后的；
 */
public class FindFragment extends Fragment implements ICircleView, Parcelable {

    protected static final String TAG = FindFragment.class.getSimpleName();
    /**
     * 圈子适配器
     */
    private CircleAdapter mAdapter;
    /**
     * 评论区域
     */
    private LinearLayout mEditTextBody;
    /**
     * 评论出入框
     */
    private EditText mEditText;
    /**
     * 发表评论按钮
     */
    private ImageView mSendIv;

    private int mScreenHeight;
    private int mEditTextBodyHeight;
    private int mCurrentKeyboardH;
    private int mSelectCircleItemH;
    private int mSelectCommentItemOffset;

    private CirclePresenter mPresenter;
    private CommentConfig mCommentConfig;
    private SuperRecyclerView recyclerView;
    private RelativeLayout bodyLayout;
    private LinearLayoutManager layoutManager;
    private FindFragment findFragment;
    private TextView Title,ku_tv;
    private RelativeLayout relativeLayout;
    private ImageView mTitleRightIv;
    private RelativeLayout rl_find_pro;
    private ProgressBar pb_find_pro;
    private LinearLayout ku_task_yichang;
    private FindData mResult;
    private final static int TYPE_PULLREFRESH = 1;
    private final static int TYPE_UPLOADREFRESH = 2;
    private int page = 1;
    private int mPage = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = UiUtils.inflateView(R.layout.activity_main);
        findFragment = this;
        mPresenter = new CirclePresenter(findFragment);
        initView(view);
        mPresenter.attachView(findFragment);
        mPresenter.loadData(TYPE_PULLREFRESH);
        return view;
    }

    @Override
    public void onResume() {
        if (MyApplication.getFLAG() == 666) {
            MyApplication.setFLAG(0);
            Log.i("SMZQ", "FragMnet ---onResume");
            mPresenter.loadData(TYPE_PULLREFRESH);
        }
        super.onResume();
    }

    /**
     * 初始化组件
     * @param view 根视图布局
     */
    @SuppressLint({"ClickableViewAccessibility", "InlinedApi"})
    private void initView(View view) {
        initTitle(view);
        recyclerView = (SuperRecyclerView) view.findViewById(R.id.recyclerView);
        ku_task_yichang = (LinearLayout) view.findViewById(R.id.ku_task_yichang);
        ku_tv = (TextView) view.findViewById(R.id.ku_tv);

        //初始化加载进度条进度条
        rl_find_pro = (RelativeLayout) view.findViewById(R.id.rl_find_pro);
        pb_find_pro = (ProgressBar) view.findViewById(R.id.pb_find_pro);
        Progressbar_Util.ProVisibility(pb_find_pro, getActivity());

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        recyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mEditTextBody.getVisibility() == View.VISIBLE) {
                    updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
                return false;
            }
        });

        recyclerView.setRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.loadData(TYPE_PULLREFRESH);
                    }
                }, 2000);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Glide.with(getActivity()).resumeRequests();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getActivity()).pauseRequests();
                }

            }
        });

        mAdapter = new CircleAdapter(getActivity());
        mAdapter.setCirclePresenter(mPresenter);
        recyclerView.setAdapter(mAdapter);

        mEditTextBody = (LinearLayout) view.findViewById(R.id.editTextBodyLl);
        mEditText = (EditText) view.findViewById(R.id.circleEt);
        mSendIv = (ImageView) view.findViewById(R.id.sendIv);
        mSendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    //发布评论
                    final String content = mEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        ToastUtil.showToast(getActivity(), "评论内容不能为空...");
                        return;
                    }
                    mPresenter.addComment(content, mCommentConfig, page);
                }
                updateEditTextBodyVisible(View.GONE, null);
            }
        });

        setViewTreeObserver(view);
    }

    /**
     * 初始化标题栏
     * @param view 根布局视图
     */
    private void initTitle(View view) {
        Title = (TextView) view.findViewById(R.id.tv_titlebar_title);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.tv_titlebar_search);
        mTitleRightIv = (ImageView) view.findViewById(R.id.iv_titlebar_right);
        Title.setText("发现");
        mTitleRightIv.setImageResource(R.drawable.fenxiang);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getToKen() != null && !"".equals(MyApplication.getToKen())) {
                    startActivity(new Intent(getActivity().getApplication(), ShareActivity.class));
                } else {
                    ToastUtil.showToast(getActivity(), "请先登录！");
                }
            }
        });
    }


    /**
     * 获取状态栏高度，以及初始化数据布局；
     * @param view 根布局视图
     */
    private void setViewTreeObserver(View view) {
        bodyLayout = (RelativeLayout) view.findViewById(R.id.bodyLayout);
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == mCurrentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                mCurrentKeyboardH = keyboardH;
                mScreenHeight = screenH;//应用屏幕的高度
                mEditTextBodyHeight = mEditTextBody.getHeight();

                //偏移listview
                if (layoutManager != null && mCommentConfig != null) {
                    layoutManager.scrollToPositionWithOffset(mCommentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE, getListviewOffset(mCommentConfig));
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 48;
        return result;
    }

    @Override
    public void update2DeleteCircle(String circleId) {
        List<CircleItem> circleItems = mAdapter.getDatas();
        for (int i = 0; i < circleItems.size(); i++) {
            if (circleId.equals(circleItems.get(i).getId())) {
                circleItems.remove(i);
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void update2AddFavorite(int circlePosition, FavortItem addItem) {
        if (addItem != null) {
            CircleItem item = (CircleItem) mAdapter.getDatas().get(circlePosition);
            item.getFavorters().add(addItem);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void update2DeleteFavort(int circlePosition, String favortId) {
        CircleItem item = (CircleItem) mAdapter.getDatas().get(circlePosition);
        List<FavortItem> items = item.getFavorters();
        for (int i = 0; i < items.size(); i++) {
            if (favortId.equals(items.get(i).getId())) {
                items.remove(i);
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void update2AddComment(int circlePosition, CommentItem addItem) {
        if (addItem != null) {
            CircleItem item = (CircleItem) mAdapter.getDatas().get(circlePosition);
            item.getComments().add(addItem);
            mAdapter.notifyDataSetChanged();
        }
        //清空评论文本
        mEditText.setText("");
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {
        CircleItem item = (CircleItem) mAdapter.getDatas().get(circlePosition);
        List<CommentItem> items = item.getComments();
        for (int i = 0; i < items.size(); i++) {
            if (commentId.equals(items.get(i).getId())) {
                items.remove(i);
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        mCommentConfig = commentConfig;
        mEditTextBody.setVisibility(visibility);

        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (View.VISIBLE == visibility) {
            mEditText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(mEditText.getContext(), mEditText);

        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(mEditText.getContext(), mEditText);
        }
    }

    @Override
    public void update2loadData(final int loadType, List<CircleItem> datas) {
        AsyncFindData findData;

        if (HttpDataUtil.isNetworkConnected(getActivity())) {
            if (MyApplication.getToKen() == null | "".equals(MyApplication.getToKen())) {
                rl_find_pro.setVisibility(View.GONE);
                ku_task_yichang.setVisibility(View.VISIBLE);
                ku_tv.setText("请先登录");
            } else {
                if (loadType == 1) {
                    page = 2;
                    FormBody boby = new FormBody.Builder()
                            .add("username", MyApplication.getusername())
                            .add("password", MyApplication.getuserpassword())
                            .build();
                    findData = new AsyncFindData(getActivity(),
                            mPresenter, mAdapter, rl_find_pro, bodyLayout, recyclerView, boby, loadType);
                    DatasUtil.getItemdatas().clear();
                    findData.setGetFindDataListener(new AsyncFindData.GetCircleItemListener() {
                        @Override
                        public void getCircleItem(List<CircleItem> items) {
                            DatasUtil.setItemdatas(items);
                        }

                        @Override
                        public void getFindData(FindData data) {
                            DatasUtil.setFindData(data);
                            mPage = Integer.parseInt(data.getTotalpage());
                        }
                    });
                    findData.execute(com.yitong.ChuangKeYuan.utils.DatasUtil.URL_BASE
                            + com.yitong.ChuangKeYuan.utils.DatasUtil.URL_FindData);

                    Log.i("FIND", "下拉刷新中");

                } else if (loadType == 2) {
                    Log.i("FIND_PAGE", " :" + page + "总页面 :" + mPage);
                    FormBody boby = new FormBody.Builder()
                            .add("username", MyApplication.getusername())
                            .add("password", MyApplication.getuserpassword())
                            .add("p", String.valueOf(page++)).build();

                    findData = new AsyncFindData(getActivity(),
                            mPresenter, mAdapter, rl_find_pro, bodyLayout, recyclerView, boby, loadType);

                    findData.setGetFindDataListener(new AsyncFindData.GetCircleItemListener() {
                        @Override
                        public void getCircleItem(List<CircleItem> items) {
                            DatasUtil.setItemdatas(items);
                        }

                        @Override//获取发现数据
                        public void getFindData(FindData result) {

                        }
                    });

                    findData.execute(com.yitong.ChuangKeYuan.utils.DatasUtil.URL_BASE
                            + com.yitong.ChuangKeYuan.utils.DatasUtil.URL_FindData);

                    if (page == Integer.parseInt(DatasUtil.getFindData().getTotalpage())) {
                        recyclerView.removeMoreListener();
                    }
                    Log.i("FIND", "上拉加载中");
                }

            }

            if (page <= mPage) {
                recyclerView.setupMoreListener(new OnMoreListener() {
                    @Override
                    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.loadData(TYPE_UPLOADREFRESH);
                            }
                        }, 2000);

                    }
                }, 1);
            } else {
                recyclerView.removeMoreListener();
            }
        } else {
            rl_find_pro.setVisibility(View.GONE);
            bodyLayout.setVisibility(View.GONE);
            ku_task_yichang.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight - 48;
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + mSelectCommentItemOffset;
        }
        Log.i(TAG, "listviewOffset : " + listviewOffset);
        return listviewOffset;
    }

    /**
     * 评论回复以及绘制视图
     * @param commentConfig
     */
    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE - firstPosition);

        if (selectCircleItem != null) {
            mSelectCircleItemH = selectCircleItem.getHeight();
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    mSelectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            mSelectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle bundle = new Bundle();
        dest.writeBundle(bundle);


    }
}


