package com.lgs.AppOps;
/**
 * Created by Lgs on 2016/7/10.
 */

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.widget.Toast;

import com.lgs.AppOps.AppOpsUI.AppListActivity;


public class Secret_CodeReceiver extends BroadcastReceiver {

    private static final String MY_SECRET_CODE = "277677";

    @SuppressWarnings("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        //    Log.e("HH", "getAction:" + action);
        if ("android.provider.Telephony.SECRET_CODE".equalsIgnoreCase(action)) {
            String host = (intent.getData() == null) ? null : intent.getData().getHost();
            //     Log.e("HH1", "getAction:" + host +" "+MY_SECRET_CODE);

            if (MY_SECRET_CODE.equalsIgnoreCase(host)) {
                //        Log.d("HH", "do secret operate ...");
                try {



                    PackageManager packageManager = context.getPackageManager();
                    ComponentName componentName = new ComponentName(context, AppListActivity.class);
                    int res = packageManager.getComponentEnabledSetting(componentName);
                    if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                            || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                        // 隐藏应用图标
                        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                        Toast.makeText(context,"隐藏应用图标",Toast.LENGTH_LONG).show();
                    } else {
                        // 显示应用图标
                        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                                PackageManager.DONT_KILL_APP);
                        Toast.makeText(context,"显示应用图标",Toast.LENGTH_LONG).show();

                    }



                    //   Intent target = new Intent();
                    //  target.setClassName("com.lgs.WeatherFavorite", "com.lgs.WeatherFavorite.InfoDetail");
                    //   target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // context.startActivity(target);

                    // final int apiLevel = Build.VERSION.SDK_INT;
                    // context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    showAppUsage(context);
                } catch (Exception e) {
                    //   Log.e("HH", "exception " + e);
                }
            }
        }

    }


    public void showAppUsage(Context context) {
        try {
            Intent intent = new Intent();
            // final int apiLevel = Build.VERSION.SDK_INT;
            intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
           // e.printStackTrace();
        }
    }
}
