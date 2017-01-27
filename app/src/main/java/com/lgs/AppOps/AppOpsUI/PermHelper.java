package com.lgs.AppOps.AppOpsUI;


import android.app.AppOpsManager;
import android.app.AppOpsManager.OpEntry;
import android.app.AppOpsManager.PackageOps;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.internal.app.IAppOpsService;
import com.lgs.AppOps.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

final class PermHelper {


    private final static IAppOpsService appOps;

    private final static HashMap<String, Integer> sPermStrings;
    private final static HashMap<String, Integer> nPermStrings;
    private final static boolean[] privateOps;
    private final static String[] permissionNames;
    private static PackageManager pm;
    private static int[] normalFilterOps;
    private static int[] systemFilterOps;
    private static int PERM_COUNT;

    static {
        appOps = IAppOpsService.Stub
                .asInterface(ServiceManager.getService("appops"));
        //这里要反射拿到总共有多少数量的权限。
        Field f = null;
        try {
            f = AppOpsManager.class.getField("_NUM_OP");
        } catch (NoSuchFieldException ignored) {
        }
        try {
            PERM_COUNT = f.getInt(null);
        } catch (IllegalAccessException ignored) {
        }
        permissionNames = new String[PERM_COUNT];
        sPermStrings = new HashMap<>();
        nPermStrings = new HashMap<>();
        privateOps = new boolean[PERM_COUNT];
    }


    public static void Init(Context c) {
        pm = c.getPackageManager();
        Resources resources = c.getResources();

        String[] opStr = resources.getStringArray(R.array.opStr);
        String[] opDesc = resources.getStringArray(R.array.opDesc);
        String[] sFilter = resources.getStringArray(R.array.sfilter);
        String[] nFilter = resources.getStringArray(R.array.nfilter);
        String[] privacyStr = resources.getStringArray(R.array.privacyStr);

        int i = 0;

        HashMap<String, String> opDescriptions = new HashMap<>();
        boolean[] nBool = new boolean[PERM_COUNT];
        boolean[] sBool = new boolean[PERM_COUNT];
        HashSet<String> nSet = new HashSet<>();
        HashSet<String> sSet = new HashSet<>();
        HashSet<String> pSet = new HashSet<>();
        ArrayList<Integer> nList = new ArrayList<>();
        ArrayList<Integer> sList = new ArrayList<>();


        for (; i < opStr.length; i++) {
            opDescriptions.put(opStr[i], opDesc[i]);
        }
        for (i = 0; i < nFilter.length; i++) {
            nSet.add(nFilter[i]);
        }
        for (i = 0; i < sFilter.length; i++) {
            sSet.add(sFilter[i]);
        }
        for (i = 0; i < privacyStr.length; i++) {
            pSet.add(privacyStr[i]);
        }
        for (i = 0; i < PERM_COUNT; i++) {
            String opName = AppOpsManager.opToName(i);
            String opDe = opDescriptions.get(opName);
            permissionNames[i] = opDe == null ? "X_" + opName : opDe;
            nBool[i] = sBool[i] = (AppOpsManager.opToSwitch(i) == i);
            if (nSet.contains(opName)) nBool[i] = false;
            if (sSet.contains(opName)) sBool[i] = false;
            privateOps[i] = pSet.contains(AppOpsManager.opToName(i));
        }

        for (i = 0; i < PERM_COUNT; i++) {
            if (nBool[i]) nList.add(i);
            if (sBool[i]) sList.add(i);
        }
        int length = sList.size();
        systemFilterOps = new int[length];
        for (i = 0; i < length; i++) {
            systemFilterOps[i] = sList.get(i);
        }

        length = nList.size();
        normalFilterOps = new int[length];
        for (i = 0; i < length; i++) {
            normalFilterOps[i] = nList.get(i);
        }

        for (i = 0; i < length; i++) {
            int nOpId = normalFilterOps[i];
            String np = AppOpsManager.opToPermission(nOpId);
            if (np != null && !nPermStrings.containsKey(np)
                    ) nPermStrings.put(np, nOpId);
        }
        length = systemFilterOps.length;
        for (i = 0; i < length; i++) {
            int sOpId = systemFilterOps[i];
            String sp = AppOpsManager.opToPermission(sOpId);
            if (sp != null && !sPermStrings.containsKey(sp))
                sPermStrings.put(sp, sOpId);
        }

    }


    public static boolean hasOps(String packageName) {

        List<AppOpsManager.PackageOps> pOps = null;
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(packageName,
                    PackageManager.GET_PERMISSIONS);
        } catch (NameNotFoundException ignored) {
        }
        boolean isNormalApp = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;

        HashMap<String, Integer> perms = isNormalApp ? nPermStrings : sPermStrings;
        if (pi.requestedPermissions != null) {
            for (String perm : pi.requestedPermissions) {
                if (perms.containsKey(perm)) return true;
            }
        }
        int uid = pi.applicationInfo.uid;
        try {
            if (isNormalApp)
                pOps = appOps.getOpsForPackage(uid, packageName, normalFilterOps);
            else {
                pOps = appOps.getOpsForPackage(uid, packageName, systemFilterOps);
            }
        } catch (RemoteException ignored) {
        }
        return pOps != null;

    }


    public static OpsInfo getOpsForPackage(String packageName) {


        List<PackageOps> pOps = null;
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(packageName,
                    PackageManager.GET_PERMISSIONS);
        } catch (NameNotFoundException ignored) {
        }
        boolean isNormalApp = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;

        int uid = pi.applicationInfo.uid;
        try {
            if (isNormalApp)
                pOps = appOps.getOpsForPackage(uid, packageName, normalFilterOps);
            else {
                pOps = appOps.getOpsForPackage(uid, packageName, systemFilterOps);
            }
        } catch (Exception ignored) {
            pOps=null;
        }

        HashMap<String, Integer> perms = isNormalApp ? nPermStrings : sPermStrings;
        byte[] opArray = new byte[PERM_COUNT];
        int i = 0;
        // 0--> 没有    1-->允许     2-->禁止
        for (; i < PERM_COUNT; i++) {
            opArray[i] = 0;
        }
        if (pi.requestedPermissions != null) {
            for (String p : pi.requestedPermissions) {
                Integer pOpId = perms.get(p);
                if (pOpId != null) opArray[pOpId] = 1;
            }
        }

        if (pOps != null) {
            List<OpEntry> opes = pOps.get(0).getOps();
            for (OpEntry ope : opes) {
                int mode = ope.getMode();
                int opId = ope.getOp();
                if (mode == AppOpsManager.MODE_ALLOWED || mode == 3) {
                    //3 is default 不同的ROM可能没有
                    opArray[opId] = 1;
                } else {
                    opArray[opId] = 2;
                }
            }
        }

        ArrayList<Integer> enable = new ArrayList<>();
        ArrayList<Integer> disable = new ArrayList<>();
        for (i = 0; i < PERM_COUNT; i++) {
            if (opArray[i] == 1) enable.add(i);
            else {
                if (opArray[i] == 2) disable.add(i);
            }
        }

        i = enable.size();
        int dSize = disable.size();
        int[] allOp = new int[i + dSize];

        for (i = 0; i < dSize; i++) {
            allOp[i] = disable.get(i);
        }
        for (i = dSize; i < allOp.length; i++) {
            allOp[i] = enable.get(i - dSize);
        }

        return new OpsInfo(packageName, uid, allOp, dSize);
    }

    public static boolean isPrivateOp(int opId) {
        return privateOps[opId];
    }


    public static String getPermName(int opId) {
        return permissionNames[opId];
    }

    public static void allowPermission(String packageName, int uid, int opId) {
        try {
            appOps.setMode(opId, uid, packageName, AppOpsManager.MODE_ALLOWED);
        } catch (Exception ignored) {
        }
    }

    public static void revokePermission(String packageName, int uid, int opId) {
        try {
            appOps.setMode(opId, uid, packageName, AppOpsManager.MODE_IGNORED);
        } catch (Exception ignored) {
        }
    }


}
