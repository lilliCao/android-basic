package com.example.android.ncquiz;

import android.content.Context;
import android.util.AttributeSet;


/**
 * Created by tali on 19.11.17.
 */

public class ImageButtonBalance extends android.support.v7.widget.AppCompatImageButton {


    public ImageButtonBalance(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myMinWidth=getMeasuredWidth();
        int w = resolveSize(myMinWidth, widthMeasureSpec);
        int h = resolveSize(myMinWidth, heightMeasureSpec);
        setMeasuredDimension(w, h);
    }
}
