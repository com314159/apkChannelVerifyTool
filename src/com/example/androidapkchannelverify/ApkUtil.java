package com.example.androidapkchannelverify;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * @author ZhiCheng Guo
 * @version 2015年1月28日 下午8:16:39
 */
@SuppressWarnings("unused")
public class ApkUtil {
	private static final String TAG = ApkUtil.class.getSimpleName();
	
	public static boolean installNormal(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
			return false;
		}

		i.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		return true;
	}

	
	public static String getApkPackageName(Context context,String apkPath) {
		
		if (context == null || apkPath == null) {
			return null;
		}
		
        PackageManager pm = context.getPackageManager();    
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);    
        if(info != null){    
            ApplicationInfo appInfo = info.applicationInfo;    
            String appName = pm.getApplicationLabel(appInfo).toString();    
            String packageName = appInfo.packageName;  //得到安装包名称  

            return packageName;
        }    
		
        return null;
	}

	public static int getApkVersionCode(Context context,String apkPath) {
		
		if (context == null || apkPath == null) {
			return -1;
		}
		
        PackageManager pm = context.getPackageManager();    
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);    
        if(info != null){    
            ApplicationInfo appInfo = info.applicationInfo;    

            return info.versionCode;
        }    
		
        return -1;
	}
	
	public static String getApkMetaData(Context context,String apkPath,String key) {
		
		if (context == null || key == null) {
			return null;
		}
		
        PackageManager pm = context.getPackageManager();    
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_META_DATA);    
        if(info != null){    
            ApplicationInfo appInfo = info.applicationInfo;    
            
            if (appInfo == null || appInfo.metaData == null) {
            	return null;
            }
            
            return appInfo.metaData.getString(key);
        }    
		
        return null;
	}

}
