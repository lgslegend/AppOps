package com.lgs.AppOps.AppOpsUI;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lgs.AppOps.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AppListActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tab);

        aboutActivity =
                new Intent(AppListActivity.this,AboutActivity.class);

        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(aboutActivity);
            }});

        tabNormal =(TextView) findViewById(R.id.tab_normal);
        tabSystem =(TextView) findViewById(R.id.tab_system);
        tabUnselectedColor =tabSystem.getTextColors();
        tabs =(ViewPager)findViewById(R.id.app_tabs);
        tabNormalBottom =findViewById(R.id.tab_normal_bottom);
        tabSystemBottom =findViewById(R.id.tab_systeml_bottom);

        //todo 不知道低版本是否可行。
        bottomColor =
                ContextCompat.getColor(AppListActivity.this,R.color.tabChangeBottom);
         
        tabNormal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(tabs.getCurrentItem()==0) return;
				tabs.setCurrentItem(0); 
				
			}
		});
        tabSystem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(tabs.getCurrentItem()==1) return;
				tabs.setCurrentItem(1); 
				
			}
		});
        tabs.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int index) {
				if(index==0){
					tabNormal.setTextColor(Color.WHITE);
					tabSystem.setTextColor(tabUnselectedColor);
					tabNormalBottom.setBackgroundColor(bottomColor);
					tabSystemBottom.setBackgroundColor(Color.TRANSPARENT);
				}else {
				    tabSystem.setTextColor(Color.WHITE);
				    tabNormal.setTextColor(tabUnselectedColor);
					tabSystemBottom.setBackgroundColor(bottomColor);
					tabNormalBottom.setBackgroundColor(Color.TRANSPARENT);
				}

			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});

        StatusBarColor.SetColor(this, R.drawable.appActivityTitle);
        LayoutInflater mInflater = getLayoutInflater(); 
        View nlayout =mInflater.inflate(R.layout.list_view_layout,null);
        View slayout =mInflater.inflate(R.layout.list_view_layout,null);
        ListView nAppListView = (ListView) nlayout.findViewById(R.id.appList);
        ListView sAppListView = (ListView) slayout.findViewById(R.id.appList);

        GetInstalledUserApps();

        AppListAdapter nAdapter=
                new AppListAdapter(AppListActivity.this, nApplist);
        nAppListView.setAdapter(nAdapter);
        nAppListView.setOnItemClickListener(nAdapter.ClickEvent);
        AppListAdapter sAdapter=
                new AppListAdapter(AppListActivity.this, sApplist);
        sAppListView.setAdapter(sAdapter);
        sAppListView.setOnItemClickListener(sAdapter.ClickEvent);
        ArrayList<View> listViews = new ArrayList<>();
        listViews.add(nlayout); 
        listViews.add(slayout); 
        tabs.setAdapter(new TabsPagerAdapter(listViews));      
        tabs.setCurrentItem(0);
        
    }


    private void GetInstalledUserApps(){

    	nApplist = new ArrayList<AppInfo>();
    	sApplist = new ArrayList<AppInfo>();
        PackageManager pm= getPackageManager();

        PermHelper permHelper=new PermHelper();

        List<ApplicationInfo> packages =pm.getInstalledApplications(0);
        int length =packages.size();
        for(int i=0;i<length;i++){

        	ApplicationInfo packageInfo =packages.get(i);

            //无任何权限的不显示
            //if(packageInfo.packageName.equals("android")|| !PermHelper.hasOps(packageInfo.packageName))continue;
            
            AppInfo appInfo = new AppInfo();
            appInfo.AppName=packageInfo.loadLabel(pm).toString();
            appInfo.PackageName=packageInfo.packageName;
            appInfo.Icon=packageInfo.loadIcon(pm);

            if ((packageInfo.flags&ApplicationInfo.FLAG_SYSTEM)!= 0)
            	sApplist.add(appInfo);
            else nApplist.add(appInfo);

        }
        Collections.sort(sApplist,comp);Collections.sort(nApplist,comp);
    }


    private  Intent aboutActivity;

    private ArrayList<AppInfo> nApplist;
    private ArrayList<AppInfo> sApplist;

    private ViewPager tabs;
    private TextView tabNormal;
    private TextView tabSystem;
    private View  tabNormalBottom;
    private View  tabSystemBottom;
    private ColorStateList tabUnselectedColor;
    
    private static int bottomColor=0;

    
    private final static AppCompare comp =new AppCompare();
}
