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

import com.jqk.bannerlibrary.R;
import com.jqk.bannerlibrary.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class BannerView extends FrameLayout {

    private ViewPager viewPager;
    private List<View> views;
    private int dataSize;
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
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(layoutParams);
        addView(viewPager);
    }

    public void setData(List<String> imgsPath) {

        views = new ArrayList<View>();
        dataSize = imgsPath.size();

        if (dataSize >= 2) {
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

        if (dataSize >= 2) {
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

        if (canScroll) {
            viewPager.setCurrentItem(1, false);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("123", "position = " + position);

                pos = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("123", "state = " + state);

                if (canScroll) {
                    //        若viewpager滑动未停止，直接返回
                    if (state != ViewPager.SCROLL_STATE_IDLE) return;
//        若当前为第一张，设置页面为倒数第二张
                    if (pos == 0) {
                        viewPager.setCurrentItem(views.size() - 2, false);
                    } else if (pos == views.size() - 1) {
//        若当前为倒数第一张，设置页面为第二张
                        viewPager.setCurrentItem(1, false);
                    }
                }
            }
        });
    }
}
