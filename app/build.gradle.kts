plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.moutamid.tiptop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.moutamid.tiptop"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        setProperty("archivesBaseName", "TipTop-$versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/NOTICE.md")
    }

    buildFeatures { viewBinding = true }

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
}

dependencies {
    implementation("com.fxn769:stash:1.3.2")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.dhaval2404:imagepicker:2.1")

//    implementation("com.squareup:square:33.0.0.20230925") {
//        exclude(group =  "jakarta.activation")
//        exclude(group =  "jakarta.xml.bind")
//        exclude(group =  "org.hamcrest")
//    }
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.stripe:stripe-java:20.77.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.stripe:stripe-android:20.0.0")

    val billing_version = "6.0.1"
    implementation("com.android.billingclient:billing:$billing_version")
    implementation("androidx.preference:preference:1.2.1")
    implementation("com.anjlab.android.iab.v3:library:2.0.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}