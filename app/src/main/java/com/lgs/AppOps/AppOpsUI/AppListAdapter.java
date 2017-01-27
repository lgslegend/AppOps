package com.lgs.AppOps.AppOpsUI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgs.AppOps.R;

import java.util.ArrayList;

final class AppListAdapter  extends BaseAdapter{

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return appList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final AppInfo singleApp = appList.get(position);
        if (view==null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(R.layout.app_list_view, null);
        }
        TextView tvAppName=(TextView)view.findViewById(R.id.textViewAppName);
        ImageView ivAppIcon =(ImageView)view.findViewById(R.id.imageViewAppIcon);

        tvAppName.setText(singleApp.AppName);
        ivAppIcon.setImageDrawable(singleApp.Icon);

        return view;
    }

    AppListAdapter(Context c, ArrayList<AppInfo> list){
        context=c;
        appList=list;
        permissionActivity =new Intent(context,PermissionsActivity.class);
    }

    private final ArrayList<AppInfo> appList ;

    private final Context context;
    
    private final Intent permissionActivity;

    final OnItemClickListener ClickEvent=  new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView,
                View view,
                int position,
                long id) {
			AppInfo app = appList.get(position);
            permissionActivity
                    .putExtra("package", app.PackageName)
                    .putExtra("appName", app.AppName);
            
            context.startActivity(permissionActivity);
			
		}
	};

}
