package com.lgs.AppOps.AppOpsUI;


import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.lgs.AppOps.*;

public final class AboutActivity extends PagedActivity {


	private PackageManager pm;
    private static ComponentName receiver;
	@Override
	protected View setMyView() {

		View  layout =inflater.inflate(R.layout.vp_about, null);
		
		
  if(receiver==null) 
      receiver =new ComponentName(getPackageName(),AutoRevokeReceiver.class.getName());

		Button returnButton = (Button) layout.findViewById(R.id.returnButton);
		CheckBox revokeButton = (CheckBox) layout.findViewById(R.id.chkRevoke);
		TextView versionText = (TextView) layout.findViewById(R.id.VersionNum);
      
      pm =getPackageManager();
      
      String version="V ";
      try {
			version +=pm.getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException ignored) {}
      versionText.setText(version);
      
      
      int rState =pm.getComponentEnabledSetting(receiver);
      //TODO 初始的时候应该不勾选
      revokeButton.setChecked(
      		rState==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
      		|| rState==PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
      		);
      
      revokeButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				
				if(checked) {
					pm.setComponentEnabledSetting(receiver,
							PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
							PackageManager.DONT_KILL_APP);
				}else {
					pm.setComponentEnabledSetting(receiver,
							PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
							PackageManager.DONT_KILL_APP);
				}
				  
				
			}
	  });
      returnButton.setOnClickListener(new OnClickListener(){
          @Override
          public void onClick(View v) {	finish();}
      });
		StatusBarColor.SetColor(this,R.drawable.aboutActivityTitle);
		return layout;
	}
}
