package com.github.promeg.gitsha.plugin


/**
 * Created by guyacong on 2015/8/11.
 */
class GitShaPluginExtension {
    /**
     * RegExp to match build types
     *
     * Default value: 'release'
     */
    String buildTypeMatcher;

    /**
     * RegExp to match flavors
     *
     * Default value: '.*'
     */
    String flavorMatcher;

    /**
     * Default true: abort build if the current git branch is dirty
     */
    Boolean abortIfGitDirty;
}
