package com.github.promeg.androidgitsha.lib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Created by guyacong on 2015/3/26.
 */
public class GitShaUtils {

    /**
     * @return current GIT SHA, if read SHA failed, return 'default'
     */
    public static String getGitSha(Context context){
        String gitSha1 = null;
        String pkgName = context.getPackageName();
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            gitSha1 = bundle.getString("ANDROID_GIT_SHA_PLUGIN_GIT_SHA1");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(TextUtils.isEmpty(gitSha1)){
            return "default";
        }
        return gitSha1;
    }
}
