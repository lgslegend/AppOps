package com.lgs.AppOps.AppOpsUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class AutoRevokeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String packageName = intent.getData().getSchemeSpecificPart();
			
		OpsInfo oInfo =PermHelper.getOpsForPackage(packageName);
		if(oInfo==null) return;
		
		int i=0,length =oInfo.allOpIds.length,uid =oInfo.UserID;
		for(;i<length;i++){
			int opId=oInfo.allOpIds[i];
			if(PermHelper.isPrivateOp(opId)) 
				PermHelper.revokePermission(packageName, uid, opId);
		}		
	}

}
