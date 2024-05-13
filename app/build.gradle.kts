plugins {
    alias(libs.plugins.androidApplication)
    alias (libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
}


android {
    namespace = "com.example.healt_connect_test_app"

    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.healt_connect_test_app"

        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.connect.client)

}

//dependencies {
//
//    implementation("com.github.chuckerteam.chucker:library:3.5.2") {
//        exclude group: "com.google.android.material", module: "material"
//    }
//    implementation 'com.scottyab:rootbeer-lib:0.1.0'
//
//    coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.0.9'
//    implementation 'com.otaliastudios:zoomlayout:1.8.0'
//    implementation fileTree(include: ['*.jar'], dir: 'libs')
//    implementation project(':utility')
//    implementation project(':mediapicker')
//    implementation project(path: ':emojilike')
//    implementation project(path: ':videocompressor')
//
//    implementation(name: 'networkfw-release', ext: 'aar')
//    implementation(name: 'qcommon-release', ext: 'aar')
//    implementation(name: 'quickbridge-release', ext: 'aar')
//    implementation(name: 'quickcomponents-release', ext: 'aar')
//    implementation(name: 'quickmobileandroid-release', ext: 'aar')
//    implementation(name: 'renderingfw-release', ext: 'aar')
//    //implementation(name: 'quickannotation', ext: 'jar')
//    //implementation(name: 'quickprocessors', ext: 'jar')
//
//    implementation 'com.facebook.soloader:soloader:0.10.4'
//    implementation "com.airbnb.android:lottie:4.2.0"
//    implementation 'com.google.android.material:material:1.4.0'
//    implementation "com.google.dagger:dagger-android:2.48.1"
//    implementation 'com.google.dagger:dagger:2.48.1'
//    kapt 'com.google.dagger:dagger-compiler:2.48.1'
//    implementation "com.google.code.gson:gson:2.9.0"
//    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
//    implementation "io.reactivex.rxjava3:rxandroid:3.0.0"
//    implementation 'com.squareup.retrofit2:converter-scalars:2.1.0'
//    implementation "com.squareup.retrofit2:adapter-rxjava3:2.9.0"
//    implementation "com.jakewharton.timber:timber:$versions.timberVersion"
//    implementation 'org.conscrypt:conscrypt-android:2.2.1'
//    implementation "com.squareup.okhttp3:okhttp:4.9.0"
//    implementation "commons-beanutils:commons-beanutils:1.9.4"
//    implementation "org.apache.commons:commons-lang3:3.13.0"
//    implementation "com.eclipsesource.j2v8:j2v8:6.2.1@aar"
//    implementation 'com.facebook.yoga:yoga-layout:2.0.0'
//    implementation 'com.github.bumptech.glide:glide:4.11.0'
//    implementation 'com.jakewharton.rxbinding4:rxbinding-core:4.0.0'
//    implementation "commons-codec:commons-codec:1.15"
//    kapt 'com.github.bumptech.glide:compiler:4.11.0'
//    def room_version = "2.4.0-alpha03"
//    implementation "androidx.room:room-runtime:$room_version"
//    kapt "androidx.room:room-compiler:$room_version"
//    implementation "androidx.fragment:fragment:1.3.0"
//    implementation "androidx.activity:activity-ktx:1.9.0"
//    implementation "androidx.lifecycle:lifecycle-extensions:2.1.0"
//    implementation "androidx.lifecycle:lifecycle-common-java8:2.5.1"
//
//
//    implementation 'com.github.barteksc:android-pdf-viewer:2.8.2'
//    testImplementation 'junit:junit:4.13.2'
//    androidTestImplementation 'com.android.support.test:runner:1.0.2'
//    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
//
//    implementation 'androidx.appcompat:appcompat:1.6.1'
//    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
//    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
//    implementation 'com.github.Cutta:GifView:1.4'
//    implementation 'androidx.palette:palette-ktx:1.0.0'
//    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
//    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
//    implementation 'com.google.android.flexbox:flexbox:3.0.0'
//    implementation('com.github.afollestad.material-dialogs:commons:0.8.5.1@aar') {
//        transitive = true
//    }
//
//    implementation 'androidx.cardview:cardview:1.0.0'
//    implementation 'com.orhanobut:hawk:2.0.1'
//    // ProgressBar
//    implementation 'com.kaopiz:kprogresshud:1.2.0'
//    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
//    implementation 'me.relex:circleindicator:1.3.2'
//
//    implementation 'com.squareup.picasso:picasso:2.71828'
//    implementation 'org.greenrobot:eventbus:3.1.1'
//    implementation 'com.jaeger.statusbarutil:library:1.5.1'
//    // implementation 'com.onesignal:OneSignal:4.8.2'
//    implementation 'com.onesignal:OneSignal:[4.0.0, 4.99.99]'
//    //Image Compressor
//    implementation 'id.zelory:compressor:2.1.0'
//    //Image Corper
//    implementation 'com.github.yalantis:ucrop:2.2.2'
//    implementation 'com.yarolegovich:lovely-dialog:1.1.0'
//
//
//    implementation 'jp.wasabeef:glide-transformations:4.3.0'
//    implementation "com.github.bumptech.glide:okhttp3-integration:4.11.0"
//    implementation 'com.github.bumptech.glide:annotations:4.12.0'
//
//
//    implementation 'de.hdodenhof:circleimageview:3.0.1'
//    implementation 'com.kyleduo.switchbutton:library:2.0.0'
//
//    implementation 'com.ToxicBakery.viewpager.transforms:view-pager-transforms:2.0.24'
//
//    implementation 'ja.burhanrashid52:photoeditor:0.4.0'
//    implementation 'com.github.rtugeek:colorseekbar:1.7.5'
//    implementation 'com.asksira.android:bsimagepicker:1.2.2'
//    implementation 'jp.wasabeef:picasso-transformations:2.2.1'
//
//    implementation 'com.zhihu.android:matisse:0.5.3-beta3'
//    implementation 'com.arthenica:ffmpeg-kit-min-gpl:4.4.LTS'
//    implementation 'androidx.multidex:multidex:2.0.1'
//
//    implementation 'ru.egslava:MaskedEditText:1.0.5'
//    // EasyPopup
//    implementation 'com.github.zyyoona7:EasyPopup:1.1.2'
//    // Card Stack View
//    implementation "com.yuyakaido.android:card-stack-view:2.3.1"
//    // ExoPlayer
//    implementation 'com.google.android.exoplayer:exoplayer:2.19.1'
//    //Google Maps
//    implementation 'com.google.android.gms:play-services-maps:18.2.0'
//    implementation 'com.google.maps.android:android-maps-utils:0.5'
//
//
//    // Materialish Progress
//    implementation 'com.pnikosis:materialish-progress:1.7'
//    // google mobile vision
//    implementation 'com.google.android.gms:play-services-vision:20.1.3'
//    // barcode reader
//    implementation 'info.androidhive:barcode-reader:1.1.5'
//
//    // Google services location
//    implementation 'com.google.android.gms:play-services-location:21.2.0'
//    //PhotoView
//    implementation 'com.github.chrisbanes:PhotoView:2.3.0'
//
//    implementation 'com.facebook.fresco:fresco:1.13.0'
//
//    //  implementation 'com.google.android:flexbox:2.0.1'
//
//    // AppMetrica SDK.
//    implementation 'com.yandex.android:mobmetricalib:5.0.0'
//    // Optionally. Play Install Referrer library.
//    implementation 'com.android.installreferrer:installreferrer:2.2'
//
//    implementation 'io.reactivex.rxjava2:rxjava:2.1.9'
//    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
//
//
//    // Koin
//    implementation "org.koin:koin-androidx-scope:$versions.koinVersion"
//    implementation "org.koin:koin-androidx-viewmodel:$versions.koinVersion"
//    implementation "org.koin:koin-androidx-fragment:$versions.koinVersion"
//
//    // coroutines
//    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$versions.coroutinesVersion"
//    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$versions.coroutinesVersion"
//    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:$versions.coroutinesVersion"
//
//    // Parse
//    implementation "com.github.parse-community.Parse-SDK-Android:parse:$versions.parse"
//    implementation "com.github.parse-community:ParseLiveQuery-Android:$versions.liveQuery"
//    implementation "com.github.parse-community.Parse-SDK-Android:parse:1.26.0"
//
//    implementation 'androidx.work:work-runtime:2.9.0'// burayı play store crash için ekledim ****
//
//    // architecture components
//    implementation "androidx.lifecycle:lifecycle-extensions:$versions.lifecycleVersion"
//    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$versions.lifecycleVersion"
//    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$versions.lifecycleVersion"
//    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$versions.lifecycleVersion"
//    testImplementation "androidx.arch.core:core-testing:$versions.archCompomentVersion"
//
//    // ViewPager2
//    implementation "androidx.viewpager2:viewpager2:$versions.view_pager_two"
//
//    // Dexterƒ
//    implementation "com.karumi:dexter:$versions.dexter"
//
//    //dataroid
//    implementation "com.commencis:appconnect-android-sdk:3.8.0"
//    implementation 'com.journeyapps:zxing-android-embedded:4.3.0'
//
//    //health connect
//    implementation("androidx.health.connect:connect-client:1.1.0-alpha07")
//
//}