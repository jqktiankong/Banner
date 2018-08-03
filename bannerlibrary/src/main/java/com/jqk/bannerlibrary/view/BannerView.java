package com.jqk.bannerlibrary.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.jqk.bannerlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 * 当跟listview，recyclerview滑动冲突时，在父控件中处理，banner没有做处理
 */

public class BannerView extends RelativeLayout {
    // 展示图片视图
    private ViewPager viewPager;
    // 小圆点视图
    private MarkView markView;
    // 空视图
    private LinearLayout emptyView;
    // 填充的总布局
    private List<View> views;
    // 图片路径的大小
    private int imgsPathSize;
    // 填充视图的大小
    private int viewSize;
    // 能不能滑动
    private boolean canScroll;
    // 当前显示的item
    private int currentItem;
    // 是否开始滚动
    private boolean isStart = false;
    // 滚动时间间隔
    private long delayTime = 4000;

    private WeakHandler handler = new WeakHandler();

    public BannerView(@NonNull Context context) {
        super(context);
        initView();
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        viewPager = new ViewPager(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(layoutParams);
        addView(viewPager);
    }

    public BannerView setImgsPath(final List<Object> imgsPath) {

        views = new ArrayList<View>();
        imgsPathSize = imgsPath.size();
        viewSize = imgsPathSize + 2;

        if (imgsPathSize == 0) { // 没有图片时，显示暂无图片
            emptyView = new LinearLayout(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            emptyView.setLayoutParams(layoutParams);
            emptyView.setGravity(Gravity.CENTER);
            emptyView.setBackgroundColor(getResources().getColor(R.color.empty_background));

            TextView emptyTv = new TextView(getContext());
            emptyTv.setText(getResources().getString(R.string.empty));
            emptyTv.setTextSize(20);
            emptyTv.setTextColor(getResources().getColor(android.R.color.white));
            emptyView.addView(emptyTv);

            addView(emptyView);

            canScroll = false;
        } else { // 有图片时，适配小圆点跟viewpager
            viewPager = new ViewPager(getContext());
            RelativeLayout.LayoutParams l = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            viewPager.setLayoutParams(l);
            addView(viewPager);

            markView = new MarkView(getContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            layoutParams.setMargins(0, 0, 0, 10);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            markView.setLayoutParams(layoutParams);
            markView.setMarks(imgsPath.size());
            addView(markView);

            if (imgsPathSize >= 2) {
                canScroll = true;
            } else {
                canScroll = false;
            }

            for (Object path : imgsPath) {
                View imgView = LayoutInflater.from(getContext()).inflate(R.layout.view_img, this, false);
                ImageView imageView = (ImageView) imgView.findViewById(R.id.img);
                Glide.with(getContext()).load(path).into(imageView);
                views.add(imgView);
            }

            if (imgsPathSize >= 2) {
                View firstImgView = LayoutInflater.from(getContext()).inflate(R.layout.view_img, this, false);
                ImageView imageView = (ImageView) firstImgView.findViewById(R.id.img);
                Glide.with(getContext()).load(imgsPath.get(imgsPath.size() - 1)).into(imageView);
                views.add(0, firstImgView);

                View lastImgView = LayoutInflater.from(getContext()).inflate(R.layout.view_img, this, false);
                ImageView imageView1 = (ImageView) lastImgView.findViewById(R.id.img);
                Glide.with(getContext()).load(imgsPath.get(0)).into(imageView1);
                views.add(views.size(), lastImgView);
            }

            BannerAdapter bannerAdapter = new BannerAdapter(views);
            viewPager.setAdapter(bannerAdapter);
            // 初始化图片位置和圆点位置
            if (canScroll) {
                viewPager.setCurrentItem(1, false);
                markView.setSelect(0);
                currentItem = 1;
            } else {
                markView.setVisibility(View.GONE);
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentItem = position;
                    Log.d("123", "onPageSelected = " + position);
                    if (currentItem == 0) {
                        markView.setSelect(viewSize - 3);
                    } else if (currentItem == viewSize - 1) {
                        markView.setSelect(0);
                    } else {
                        markView.setSelect(currentItem - 1);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    switch (state) {
                        case ViewPager.SCROLL_STATE_IDLE:
                            if (currentItem == 0) {
                                viewPager.setCurrentItem(viewSize - 2, false);
                            } else if (currentItem == viewSize - 1) {
                                viewPager.setCurrentItem(1, false);
                            }
                            break;
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            if (canScroll) {
                                if (currentItem == 0) {
                                    viewPager.setCurrentItem(viewSize - 2, false);
                                } else if (currentItem == viewSize - 1) {
                                    viewPager.setCurrentItem(1, false);
                                }
                            }
                            break;
                        case ViewPager.SCROLL_STATE_SETTLING:
                            break;
                    }
                }
            });

            // 设置banner点击事件
            bannerAdapter.setOnClickListener(new BannerAdapter.OnClickListener() {
                @Override
                public void onClick(int position) {
                    String message = (String) imgsPath.get(position - 1);
                    if (onBannerClickListener != null) {
                        onBannerClickListener.onClick(message);
                    }
                }
            });
        }
        return this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 手动滑动时，暂停手动滚动
        int action = ev.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_OUTSIDE) {

            if (isStart) {
                startAutoPlay();
            }

        } else if (action == MotionEvent.ACTION_DOWN) {
            stopAutoPlay();
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 开始自动滚动
     */
    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    /**
     * 关闭自动滚动
     */
    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    /**
     * 开始
     */
    public void start() {
        if (canScroll) {
            startAutoPlay();
            isStart = true;
        }
    }

    /**
     * 停止
     */
    public void stop() {
        stopAutoPlay();
        isStart = false;
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            currentItem = currentItem % (viewSize - 1) + 1;
            if (currentItem == 1) {
                viewPager.setCurrentItem(currentItem, false);
                handler.post(task);
            } else {
                viewPager.setCurrentItem(currentItem, true);
                handler.postDelayed(task, delayTime);
            }
        }
    };

    // banner点击事件接口
    private OnBannerClickListener onBannerClickListener;

    public interface OnBannerClickListener {
        void onClick(String message);
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }
}
