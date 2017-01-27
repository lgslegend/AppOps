package com.lgs.AppOps.AppOpsUI;

import java.text.Collator;

final class AppCompare  implements java.util.Comparator<AppInfo>{

	private static final Collator cmp;
	static{
		cmp =Collator.getInstance(java.util.Locale.CHINA);   
	}
	
	@Override
	public int compare(AppInfo lhs, AppInfo rhs) {
		return cmp.compare(lhs.AppName, rhs.AppName);
	}

}
