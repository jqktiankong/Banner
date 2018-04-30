package com.jqk.bannerlibrary.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jqk.bannerlibrary.R;

/**
 * Created by Administrator on 2018/4/30 0030.
 */

public class MarkView extends LinearLayout {

    private int markNumber;

    public MarkView(Context context) {
        super(context);
        initView();
    }

    public MarkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(getContext().getResources().getColor(R.color.markview_background));
    }

    public void setMarks(int markNumber) {

        this.markNumber = markNumber;

        for (int i = 0; i < markNumber; i++) {
            ImageView markView = new ImageView(getContext());
            markView.setBackgroundResource(R.drawable.mark_default);
            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(20, 20);
            layoutparams.setMargins(10, 0, 10, 0);
            markView.setLayoutParams(layoutparams);
            addView(markView);
        }
    }

    public void setSelect(int selectNumber) {
        removeAllViews();
        for (int i = 0; i < markNumber; i++) {
            ImageView markView = new ImageView(getContext());

            if (i == selectNumber) {
                markView.setBackgroundResource(R.drawable.mark_default);
            } else {
                markView.setBackgroundResource(R.drawable.mark_default);
            }
            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(20, 20);
            layoutparams.setMargins(10, 0, 10, 0);
            markView.setLayoutParams(layoutparams);
            addView(markView);
        }
    }
}
