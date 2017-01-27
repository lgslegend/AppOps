package com.lgs.AppOps.AppOpsUI;

import android.util.Log;

public final class Xlog {

	public static void d(String s){
		Log.e("omio", s);
	}
	
	public static void d(int s){
		Log.e("omio", s+"");
	}
	public static void d(int[] s){
		String z="";
		for(int q:s){
			z+=","+q;
		}
		Log.e("omio", z);
	}
}
