package com.lgs.AppOps.AppOpsUI;



final class OpsInfo {

	public final String PackageName;
	
	public final int UserID;
	
	
	public final int[] allOpIds;
	
	public final boolean[] opIdStates;
	

	
	public OpsInfo(String pn,int uid,int[] all,int disabledIndex){
		
		PackageName=pn;
		UserID=uid;
		allOpIds=all;
		opIdStates =new boolean[all.length];
		int i=0;
		for(;i<disabledIndex;i++){
			opIdStates[i]=false;
		}
		for(i=disabledIndex;i<all.length;i++){
			opIdStates[i]=true;
		}
		
	}
}
