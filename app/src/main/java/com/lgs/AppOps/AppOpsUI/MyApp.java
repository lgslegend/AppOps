package com.lgs.AppOps.AppOpsUI;

import android.app.Application;

public final class MyApp extends Application {
	@Override
    public void onCreate() {
        super.onCreate();

        PermHelper.Init(this);

    }
}
