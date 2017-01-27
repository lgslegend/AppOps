package com.lgs.AppOps.AppOpsUI;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lgs.AppOps.*;

public abstract class PagedActivity extends Activity {


	protected LayoutInflater inflater;

	
	@Override
	protected void onCreate(Bundle z) {
		super.onCreate(z);
		setContentView(R.layout.blank_activity);
		ViewPager vPager = (ViewPager) findViewById(R.id.blank_vp);
		
	    inflater = getLayoutInflater(); 
		View[] vList = new View[2];
		vList[0]=inflater.inflate(R.layout.blank_layout, null);
		vList[1]=setMyView();
		vPager.addOnPageChangeListener(listener);
		vPager.setAdapter(new InnerAdapter(vList));
		vPager.setCurrentItem(1);
	}
	
	protected abstract View setMyView();
		

	
	private static class InnerAdapter extends PagerAdapter{

		@Override
		public int getCount() {return 2;}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			 return arg0 == arg1;
		}
		
		public InnerAdapter(View[] vList){
			views =vList;
		}
		
//		@Override
//	    public Object instantiateItem(View vp, int index) {
//	        ((ViewPager) vp).addView(views[index], 0);
//	        return views[index];
//	    }

		@Override
		public Object instantiateItem(ViewGroup vgp, int position){
			vgp.addView(views[position], 0);
			return views[position];
		}

		
		private final View[] views;
		
	}
	
	
	private final OnPageChangeListener listener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int index) {			
			if(index==0) finish();			
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {}
	};
	
	
}
