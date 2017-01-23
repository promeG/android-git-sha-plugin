# android-git-sha-plugin

Automaticlly add current GIT SHA value to your apk. It can rise an error if the current git branch is dirty.

## Why you should use a GIT SHA in your crash reporting.

Acturally this project is inspried by [Why You Should Use a GIT SHA in Your Crash Reporting](http://www.donnfelker.com/why-you-should-use-a-git-sha-in-your-crash-reporting/) .

Please read it before using this plugin.

## How to use android-git-sha-plugin

### 1. Add to your project

#### In your project's root build.gradle file.
```groovy
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath 'com.github.promeg:android-git-sha-plugin:1.0.1'
  }
}
```

#### In your application module's build.gradle file.
```groovy
apply plugin: 'com.android.application'
apply plugin: 'android-git-sha'

dependencies {
  compile 'com.github.promeg:android-git-sha-lib:1.0.1'
}
```

### 2. Config plugin

```groovy
gitSha {
    buildTypeMatcher = 'release' // RegExp to specify the build type
    flavorMatcher = 'inner|fortest' // RegExp to specify the flavor
    abortIfGitDirty = true // whether abort build if the current git branch is dirty
}
```

### 3. Read GIT SHA value in java code



```java
GitShaUtils.getGitSha(appContext); // return current GIT SHA value.
```

Then you can add GIT SHA value in your crash reporting.

```java
 Crashlytics.setString("git_sha", GitShaUtils.getGitSha(appContext));
``` 


