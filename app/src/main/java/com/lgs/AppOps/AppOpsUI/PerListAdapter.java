package com.lgs.AppOps.AppOpsUI;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lgs.AppOps.*;

final class PerListAdapter extends BaseAdapter {

    private static final int DisabledColor = Color.rgb(255, 0, 0);
    private static final int EnabledColor = Color.rgb(109, 109, 109);
    private final OpsInfo permiInfo;
    private final LayoutInflater inflater;


    public PerListAdapter(LayoutInflater li, String pkgName) {

        inflater = li;
      permiInfo = PermHelper.getOpsForPackage(pkgName);


    }

    @Override
    public int getCount() {
        return permiInfo.opIdStates.length;
    }

    @Override
    public Object getItem(int arg0) {
        return permiInfo.allOpIds[arg0];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position,
                        View convertView,
                        ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.permission_list_view, null);
        }

        final int opId = permiInfo.allOpIds[position];

        final TextView permiName =
                (TextView) convertView.findViewById(R.id.permiName);
        ToggleButton switchButton =
                (ToggleButton) convertView.findViewById(R.id.permiSwitch);

        if (permiInfo.opIdStates[position]) {
            switchButton.setChecked(true);
            permiName.setTextColor(EnabledColor);
        } else {
            switchButton.setChecked(false);
            permiName.setTextColor(DisabledColor);
        }

        permiName.setText(PermHelper.getPermName(opId));
        switchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isChecked = ((ToggleButton) v).isChecked();
                permiInfo.opIdStates[position] = isChecked;
                if (isChecked) {
                    permiName.setTextColor(EnabledColor);
                    PermHelper
                            .allowPermission(permiInfo.PackageName, permiInfo.UserID, opId);
                } else {
                    permiName.setTextColor(DisabledColor);
                    PermHelper
                            .revokePermission(permiInfo.PackageName, permiInfo.UserID, opId);
                }
// Xlog.d("p"+position+" o"+opId+" "+android.app.AppOpsManager.opToName(opId));

            }
        });


        return convertView;
    }

}
