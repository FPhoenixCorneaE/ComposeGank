plugins {
    id(Plugins.androidApplication)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kotlinKapt)
    id(Plugins.kotlinParcelize)
}

android {
    defaultConfig {
        applicationId = DefaultConfig.applicationId
        compileSdk = DefaultConfig.compileSdkVersion
        minSdk = DefaultConfig.minSdkVersion
        targetSdk = DefaultConfig.targetSdkVersion
        versionCode = DefaultConfig.versionCode
        versionName = DefaultConfig.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        ndk {
            // 设置支持的SO库架构(添加 "x86" 架构是为了能安装到模拟器)
            // "armeabi", "x86", "armeabi-v7a", "x86_64", "arm64-v8a"
            abiFilters.addAll(listOf("armeabi-v7a", "x86"))
        }
    }

    buildTypes {
        getByName(Deps.BuildType.Debug) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName(Deps.BuildType.Release) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    lint {
        isCheckDependencies = true
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Deps.Compose.version
    }

    packagingOptions {
        resources.excludes.add("META-INF/proguard/coroutines.pro")
    }

    configurations.all {
        resolutionStrategy {
            // 强制指定版本
            force(Deps.Kotlin.stdlib)
        }
    }
}

// 输出文件
android.applicationVariants.all {
    // 编译类型
    val buildType = buildType.name
    outputs.all {
        // 输出 Apk
        if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
            if (buildType == Deps.BuildType.Debug) {
                this.outputFileName =
                    "${project.name}_V${android.defaultConfig.versionName}_${buildType}_${Deps.getSystemTime()}.apk"
            } else if (buildType == Deps.BuildType.Release) {
                this.outputFileName =
                    "${project.name}_V${android.defaultConfig.versionName}_${buildType}_${Deps.getSystemTime()}.apk"
            }
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // Kotlin
    addKotlinDependencies()
    // AndroidX
    addAndroidXDependencies()
    // FPhoenixCorneaE
    addFPhoenixCorneaEDependencies()
    // ComposeOfficial
    addComposeOfficialDependencies()
    // ThirdParty
    addThirdPartyDependencies()
    // Debug
    addDebugDependencies()
    // Test
    addTestDependencies()
    // AndroidTest
    addAndroidTestDependencies()
}