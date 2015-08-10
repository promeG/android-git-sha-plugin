package com.github.promeg.gitsha.plugin
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.internal.reflect.Instantiator
/**
 * Created by guyacong on 2015/3/24.
 */
class GitShaPlugin implements Plugin<Project> {
    def CMD_GIT_SHA = 'git rev-parse --short HEAD'

    @Override
    void apply(Project project) {

        project.afterEvaluate {
            def hasApp = project.plugins.withType(AppPlugin)
            if (!hasApp) {
                return
            }

            final def log = project.logger
            final def variants = project.android.applicationVariants

            //log.debug("jarsignerExe: " + jarsignerExe)
            //log.debug("zipalignExe: " + zipalignExe)

            if( !isGitClean() ){
                throw new IllegalStateException("There are uncommit changes or untracked files.")
            }
            project.task('gitsha') << {
                println "\t\\-----hello: " + execCmdAndGetResult(CMD_GIT_SHA)
                println "git check: " + isGitClean()
            }
        }
    }

    boolean isGitClean(){
        // check if the current git branch is dirty
        if(execCmdAndGetResult("git diff --shortstat") != ''){
            println("branch is dirty!")
            return false
        }
        // check if there is any untracked file
        if(execCmdAndGetResult("git status --porcelain") != ''){
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
            println("jarsigner output: " + line)
                result += line
        }
        process.waitFor()
        return result
    }
}
