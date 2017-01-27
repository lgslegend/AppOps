package com.lgs.AppOps.AppOpsUI;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lgs.AppOps.*;

public final class PermissionsActivity extends PagedActivity {



    private ListView listView;

    private PerListAdapter adapter;

    @Override
	protected View setMyView() {
		
		
        Intent intent= getIntent();
        String appName =intent.getStringExtra("appName");
        String packageName =intent.getStringExtra("package");
        View  layout =inflater.inflate(R.layout.vp_permission_list, null);
        adapter= new PerListAdapter(inflater,  packageName);

        Button returnButton = (Button) layout.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {	finish();}});




        TextView titleTextView =
                (TextView)layout.findViewById(R.id.PermitListActivityTitle);

        titleTextView.setText(appName);
        ImageView appIcon =(ImageView)layout.findViewById(R.id.permAppIcon);
        PackageManager  pm = getPackageManager();
        ApplicationInfo ai=null;
        try {
			ai=	pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
		} catch (NameNotFoundException ignored) {}
        appIcon.setImageDrawable(ai.loadIcon(pm));


        StatusBarColor.SetColor(this,R.drawable.permitActivityTitle);

        listView =(ListView)layout.findViewById(R.id.permitList);
        listView.setAdapter(adapter);
		return layout;
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		listView=null;
		adapter =null;
	}

}
