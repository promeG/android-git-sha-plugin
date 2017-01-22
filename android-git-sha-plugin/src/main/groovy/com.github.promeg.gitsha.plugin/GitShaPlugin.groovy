package com.github.promeg.gitsha.plugin

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by guyacong on 2015/8/11.
 */
class GitShaPlugin implements Plugin<Project> {
    def CMD_GIT_SHA = 'git rev-parse --short HEAD'
    GitShaPluginExtension gitShaExt;

    @Override
    void apply(Project project) {

        gitShaExt = project.extensions.create("gitSha", GitShaPluginExtension)

        project.afterEvaluate {
            def buildTypeMatcher = gitShaExt.buildTypeMatcher == null ? "release" : gitShaExt.buildTypeMatcher
            def flavorMatcher = gitShaExt.flavorMatcher == null ? /.*/ : gitShaExt.flavorMatcher
            def abortIfGitDirty = gitShaExt.abortIfGitDirty == null ? true : gitShaExt.abortIfGitDirty

            def hasApp = project.plugins.withType(AppPlugin)
            if (!hasApp) {
                return
            }

            final def appVariants = project.android.applicationVariants

            appVariants.matching({ it.buildType.name.matches(buildTypeMatcher) }).all {
                def flavorName = it.properties.get('flavorName')
                if (flavorName.matches(flavorMatcher)) {
                    it.getAssemble().doFirst { task ->
                        if (abortIfGitDirty && !isGitClean()) {
                            throw new IllegalStateException("GitShaPlugin:  There are uncommit changes or untracked files in current git branch. Abort!")
                        }
                    }
                    def gitSha = execCmdAndGetResult(CMD_GIT_SHA)
                    it.outputs.each { output ->
                        output.processManifest.doLast {
                            def manifestFile = output.processManifest.manifestOutputFile
                            def updatedContent = manifestFile.getText('UTF-8').replaceAll("\"ANDROID_GIT_SHA_PLUGIN_GIT_SHA1_VALUE\"", "\"" + gitSha + "\"")
                            manifestFile.write(updatedContent, 'UTF-8')
                        }
                    }
                }
            }
        }
    }

    boolean isGitClean() {
        // check if the current git branch is dirty
        if (execCmdAndGetResult("git diff --shortstat") != '') {
            println("branch is dirty!")
            return false
        }
        // check if there is any untracked file
        if (execCmdAndGetResult("git status --porcelain") != '') {
            println("there are untracked files!")
            return false
        }
        return true
    }


    String execCmdAndGetResult(String cmd)
            throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(cmd)
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))
        String result = ''
        String line
        while ((line = reader.readLine()) != null) {
            result += line
        }
        process.waitFor()
        return result
    }
}
