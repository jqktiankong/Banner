package com.jqk.bannerlibrary.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jqk.bannerlibrary.R;
import com.jqk.bannerlibrary.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class BannerView extends RelativeLayout {

    private ViewPager viewPager;
    private MarkView markView;
    private List<View> views;
    private int imgsPathSize;
    private int viewSize;
    private boolean canScroll;
    private int pos;

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

    public void setData(List<String> imgsPath) {

        views = new ArrayList<View>();
        imgsPathSize = imgsPath.size();
        viewSize = imgsPathSize + 2;

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

        for (String path : imgsPath) {
            View imgView = LayoutInflater.from(getContext()).inflate(R.layout.view_img, this, false);
            ImageView imageView = (ImageView) imgView.findViewById(R.id.img);
            GlideApp.with(getContext()).load(path).into(imageView);
            views.add(imgView);
        }

        if (imgsPathSize >= 2) {
            View firstImgView = LayoutInflater.from(getContext()).inflate(R.layout.view_img, this, false);
            ImageView imageView = (ImageView) firstImgView.findViewById(R.id.img);
            GlideApp.with(getContext()).load(imgsPath.get(imgsPath.size() - 1)).into(imageView);
            views.add(0, firstImgView);

            View lastImgView = LayoutInflater.from(getContext()).inflate(R.layout.view_img, this, false);
            ImageView imageView1 = (ImageView) lastImgView.findViewById(R.id.img);
            GlideApp.with(getContext()).load(imgsPath.get(0)).into(imageView1);
            views.add(views.size(), lastImgView);
        }

        BannerAdapter bannerAdapter = new BannerAdapter(views);
        viewPager.setAdapter(bannerAdapter);
        // 初始化图片位置和圆点位置
        if (canScroll) {
            viewPager.setCurrentItem(1, false);
            markView.setSelect(0);
            pos = 2;
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;

                if (pos == 0) {
                    markView.setSelect(viewSize - 3);
                } else if (pos == viewSize - 1) {
                    markView.setSelect(0);
                } else {
                    Log.d("123", "setMark position = " + (pos - 1));
                    markView.setSelect(pos - 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        if (canScroll) {
                            if (pos == 0) {
                                viewPager.setCurrentItem(viewSize - 2, false);
                                markView.setSelect(viewSize - 3);
                            } else if (pos == viewSize - 1) {
                                viewPager.setCurrentItem(1, false);
                                markView.setSelect(0);
                            }
                        }
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
    }
}
