package com.etlegacy.app.q3e;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.etlegacy.app.R;

import java.util.ArrayList;

public class Q3EAd {

    public static Runnable adrunnable = null;

    static final String[] adpkgsn = new String[]{"etlegacy.com", "fearless-assassins.com"};

    public static ImageView createiwbyid(final AppCompatActivity ctx, Bitmap orig, int width, int id)
    {
        ImageView iw = new ImageView(ctx);
        Bitmap bmtmp = Bitmap.createBitmap(orig, 0, id * (orig.getHeight() / adpkgsn.length), orig.getWidth(), orig.getHeight() / adpkgsn.length);
        Bitmap bm = Bitmap.createScaledBitmap(bmtmp, width, width * bmtmp.getHeight() / bmtmp.getWidth(), true);
        iw.setImageBitmap(bm);
        iw.setTag(adpkgsn[id]);
        iw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((String) v.getTag()));
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                ctx.startActivity(marketIntent);
            }
        });
        return iw;
    }

    public static void LoadAds(final AppCompatActivity ctx)
    {
        final LinearLayout ll = ctx.findViewById(R.id.adlayout_id);
        ll.removeAllViews();

        Bitmap imagorig = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.adlist);
        //carousel
        ArrayList<Integer> ids = new ArrayList<>(0);
        ArrayList<View> vws = new ArrayList<>(0);
        Display display = ctx.getWindowManager().getDefaultDisplay();
        final int width = Q3EUtils.dip2px(ctx, 320);//Magic number
        final int dspwidth = display.getWidth();
        for (int i = 0; i < adpkgsn.length; i++)
        {
            if (!Q3EUtils.isAppInstalled(ctx, adpkgsn[i]))
                ids.add(i);
        }
        for (int i : ids)
        {
            vws.add(createiwbyid(ctx, imagorig, width, i));
        }
        int carouselcount = dspwidth / width + ((dspwidth % width == 0) ? 0 : 1);

        if (carouselcount <= ids.size())
        {
            for (int i = 0; i < carouselcount; i++)
                vws.add(createiwbyid(ctx, imagorig, width, ids.get(i)));
            for (int i = 0; i < carouselcount; i++)
                vws.add(0, createiwbyid(ctx, imagorig, width, ids.get(ids.size() - 1 - i)));
            ETLHorizontalScrollView.maxscrollx = width * (ids.size()) + carouselcount * width / 2;
            ETLHorizontalScrollView.minscrollx = width * carouselcount / 2;
            ETLHorizontalScrollView.deltascrollx = width * ids.size();
        }
        else
        {
            ETLHorizontalScrollView.maxscrollx = 0;
            ETLHorizontalScrollView.minscrollx = 0;
            ETLHorizontalScrollView.deltascrollx = 0;
        }
        for (View v : vws)
        {
            ll.addView(v);
        }
        final ETLHorizontalScrollView mhsw = ctx.findViewById(R.id.adlayout_hsw);

        if (adrunnable != null)
        {
            ll.removeCallbacks(adrunnable);
        }
        adrunnable = new Runnable()
        {
            @Override
            public void run()
            {
                if ((!ctx.isFinishing()) && (mhsw.getScrollX() % width == 0))
                {
                    mhsw.scrollBy(width, 0);
                    ll.postDelayed(this, 10000);
                }
            }
        };

        ll.postDelayed(adrunnable, 10000);

    }
}
