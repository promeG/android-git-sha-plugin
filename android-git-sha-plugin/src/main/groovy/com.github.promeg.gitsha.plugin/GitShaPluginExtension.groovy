package com.github.promeg.gitsha.plugin

import com.android.builder.model.SigningConfig

/**
 * Created by guyacong on 2015/3/24.
 */
class GitShaPluginExtension {
    String prefix;
    String subfix;

    String jarsignerPath;
    String zipalignPath;

    SigningConfig defaultSigningConfig;

    GitShaPluginExtension() {
    }
}
