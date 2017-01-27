package com.lgs.AppOps.AppOpsUI;

import java.util.ArrayList;
import java.util.Objects;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

final class TabsPagerAdapter extends PagerAdapter{

	private final ArrayList<View> innerTabs;
	
	public TabsPagerAdapter(ArrayList<View> list){
		innerTabs=list;
	}
	
	
	@Override
	public int getCount() {return 2;}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1; 
	}

//	@Override
//    public Object instantiateItem(View vp, int index) {
//        ((ViewPager) vp).addView(innerTabs.get(index), 0);
//        return innerTabs.get(index);
//    }

	@Override
	public Object instantiateItem(ViewGroup vgp,int position){
		vgp.addView(innerTabs.get(position), 0);
		return innerTabs.get(position);
	}
}
