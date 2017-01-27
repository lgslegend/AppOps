package com.lgs.AppOps.AppOpsUI;


import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.lgs.AppOps.*;

final class StatusBarColor {

    static void SetColor(Activity activity, String z) {
        int r,g,b;
        r =Integer.valueOf(z.substring(3,5),16);
        g =Integer.valueOf(z.substring(5,7),16);
        b =Integer.valueOf(z.substring(7,9),16);
        SetColor(activity,r,g,b);
    }

    static void SetColor(Activity activity, int resId) {
        SetColor(activity,activity.getResources().getString(resId));
    }

    static void SetColor(Activity activity, int r, int g, int b) {
        b = Color.rgb(r, g, b);
        realSetColor(activity,b);
    }



    private static void realSetColor(Activity activity, int color){
        Window window = activity.getWindow();
        //因为要用到自定义的jar包 这里判断android 版本就直接用数字，不用VERSION_CODES类
        if(Build.VERSION.SDK_INT>20){
            //5.0以上直接设置。
            window.addFlags(-2147483648);
            // WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            //因为要用到自定义的jar包 这里就直接用数字，
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }else {
            //4.4
            // TODO: test 4.4
            OlderSet(activity,window,color);

        }

    }


    private static void OlderSet(Activity activity,Window win,int color){
        win.addFlags(67108864);
        //WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();
        WindowManager.LayoutParams winParams = win.getAttributes();
        Resources res = activity.getResources();

//        int r =Color.red(color),g =Color.green(color),b =Color.blue(color);
//        r=(r* 5 / 3);g =(g * 5 / 3); b =( b * 5 / 3);
//        if(r>255) r=255;if(g>255) g=255;if(b>255) b=255;
//        color =Color.rgb(r, g, b);

        boolean mNavBarAvailable;
        if ((winParams.flags & 134217728) != 0) {
            mNavBarAvailable = true;
        }else {
            int[] attrs = {16843759, 16843760};
            TypedArray a = activity.obtainStyledAttributes(attrs);
            mNavBarAvailable = a.getBoolean(1, false);
            a.recycle();
        }


        final boolean mInPortrait =
                res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        float widthDp = metrics.widthPixels / metrics.density;
        float heightDp = metrics.heightPixels / metrics.density;
        final float mSmallestWidthDp =Math.min(widthDp,heightDp);

        int mStatusBarHeight=0;
        int resourceId = res.getIdentifier(STATUS_BAR_HEIGHT_RES_NAME, "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = res.getDimensionPixelSize(resourceId);
        }


        int mNavigationBarHeight =0, mNavigationBarWidth=0;
        if(hasNavBar(activity,res)){
            String key=mInPortrait?NAV_BAR_HEIGHT_RES_NAME:NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
            resourceId =res.getIdentifier(key,"dimen", "android");
            if(resourceId>0){
                mNavigationBarHeight = res.getDimensionPixelSize(resourceId);
            }
            resourceId =res.getIdentifier(NAV_BAR_WIDTH_RES_NAME,"dimen", "android");
            if(resourceId>0){
                mNavigationBarWidth =res.getDimensionPixelSize(resourceId);
            }
        }

        View tintView = new View(activity);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams
                        (FrameLayout.LayoutParams.MATCH_PARENT, mStatusBarHeight);
        params.gravity = Gravity.TOP;
        if (mNavBarAvailable && !mInPortrait && mSmallestWidthDp < 600) {
            params.rightMargin = mNavigationBarWidth;
        }
        tintView.setLayoutParams(params);
        tintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        tintView.setVisibility(View.GONE);
        decorViewGroup.addView(tintView);
        tintView.setVisibility(View.VISIBLE);
        tintView.setBackgroundColor(color);


        if(mNavBarAvailable && mNavigationBarHeight>0){
            View navView =new View(activity);
            FrameLayout.LayoutParams navParams;
            if (mSmallestWidthDp >= 600 || mInPortrait) {
                navParams =
                   new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                           mNavigationBarHeight);
                navParams.gravity = Gravity.BOTTOM;
            } else {
                navParams = new FrameLayout.LayoutParams(mNavigationBarWidth,
                        FrameLayout.LayoutParams.MATCH_PARENT);
                navParams.gravity = Gravity.RIGHT;
            }
            navView.setLayoutParams(navParams);
            navView.setBackgroundColor(DEFAULT_TINT_COLOR);
            navView.setVisibility(View.GONE);
            decorViewGroup.addView(navView);
            navView.setVisibility(View.VISIBLE);
            navView.setBackgroundColor(color);
        }
    }

    private static boolean hasNavBar(Activity activity,Resources res){

        int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
        if(resourceId==0) return !ViewConfiguration.get(activity).hasPermanentMenuKey();
        if("1".equals(sNavBarOverride)) return false;
        if ("0".equals(sNavBarOverride)) return true;
        return res.getBoolean(resourceId);
    }


    private static String sNavBarOverride;
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
    private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
    private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";
    private static final int DEFAULT_TINT_COLOR = 0x99000000;

    static {
        sNavBarOverride= android.os.SystemProperties.get("qemu.hw.mainkeys");
    }

}
