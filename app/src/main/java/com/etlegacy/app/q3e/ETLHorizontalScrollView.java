package com.etlegacy.app.q3e;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class ETLHorizontalScrollView extends HorizontalScrollView {
    public static int maxscrollx=0;
    public static int minscrollx=0;
    public static int deltascrollx=0;

    public ETLHorizontalScrollView(Context context)
    {
        super(context);
    }
    public ETLHorizontalScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public ETLHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if ((maxscrollx!=0)&&(l>maxscrollx))
        {
            l-=deltascrollx;
            scrollTo(l, t);
        }
        else
        if ((minscrollx!=0)&&(l<minscrollx))
        {
            l+=deltascrollx;
            scrollTo(l, t);
        }
        else
            super.onScrollChanged(l, t, oldl, oldt);
    }
}
