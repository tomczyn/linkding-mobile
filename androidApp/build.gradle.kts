@file:Suppress("UnstableApiUsage")

plugins {
    id("android.application")
    id("android.application.compose")
    alias(libs.plugins.junit5)
}

android {
    defaultConfig {
        applicationId = "com.tomczyn.linkding.android"
        versionCode = getVersionCode()
        versionName = getVersionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        @Suppress("UNUSED_VARIABLE")
        val debug by getting {
            applicationIdSuffix = LinkdingBuildType.DEBUG.applicationIdSuffix
        }
        @Suppress("UNUSED_VARIABLE")
        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = LinkdingBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "com.tomczyn.linkding.android"
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.timber)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.kmm.viewmodel)

    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
}

// androidx.test is forcing JUnit, 4.12. This forces it to use 4.13
configurations.configureEach {
    resolutionStrategy {
        force(libs.junit4)
        // Temporary workaround for https://issuetracker.google.com/174733673
        force("org.objenesis:objenesis:2.6")
    }
}

val checkReleaseVersion by tasks.registering {
    doLast {
        val versionName = android.defaultConfig.versionName
        if (versionName?.matches("\\d+(\\.\\d+)+".toRegex()) == false) {
            throw GradleException(
                "Version name for release builds can only be numeric (like 1.0.0), but was $versionName\n" +
                    "Please use git tag to set version name on the current commit and try again\n" +
                    "For example: git tag -a 1.0.0 -m 'tag message'"
            )
        }
    }
}

tasks.whenTaskAdded {
    if (name.contains("assemble") &&
        name.contains("Release")
    ) {
        dependsOn(checkReleaseVersion)
    }
}
