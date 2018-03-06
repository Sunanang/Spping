package com.mdht.shopping.spping;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

/**
 * Created by ${Sunanang} on 17/11/3.
 */

public class ShareUtils {

    public static  boolean getShare(Context context,String Name){
        SharedPreferences shared = context.getSharedPreferences("backgroundImg", Context.MODE_PRIVATE);
        String string = shared.getString("imageName", "background");
        if(string.equals("background") || Name.equals(string)){
            return true;
        }else
            return false;
    }

    public static void setShare(Context context,String Name){
        SharedPreferences shared = context.getSharedPreferences("backgroundImg", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString("imageName",Name);
        edit.commit();
        edit.clear();
    }

    public static boolean getBack(Context context){
        SharedPreferences shared = context.getSharedPreferences("backgroundImg", Context.MODE_PRIVATE);
        String string = shared.getString("imageName", "background");
        if(string.equals("background")){
            return true;
        }else
            return false;
    }


    /**

     * 检测该包名所对应的应用是否存在

     * @param packageName

     * @return

     */

    public static boolean checkPackage(Context mContext,String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            mContext.getPackageManager().getApplicationInfo(packageName, PackageManager
                    .GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
