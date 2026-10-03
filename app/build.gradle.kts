plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android { namespace = "com.ludoroyale.app"; compileSdk = 35
    defaultConfig { applicationId = "com.ludoroyale.app"; minSdk = 24; targetSdk = 35; versionCode = 1; versionName = "1.0.0" }
    buildTypes {
        debug { applicationIdSuffix = ".debug"; versionNameSuffix = "-debug" }
        release { isMinifyEnabled = false; isShrinkResources = false; proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") }
    }
    signingConfigs {
        create("release") {
            val keystorePath = providers.gradleProperty("releaseKeystorePath").orNull
                ?: providers.environmentVariable("RELEASE_KEYSTORE_PATH").orNull
            val keystorePassword = providers.gradleProperty("releaseKeystorePassword").orNull
                ?: providers.environmentVariable("RELEASE_KEYSTORE_PASSWORD").orNull
            val keyAliasValue = providers.gradleProperty("releaseKeyAlias").orNull
                ?: providers.environmentVariable("RELEASE_KEY_ALIAS").orNull
            val keyPasswordValue = providers.gradleProperty("releaseKeyPassword").orNull
                ?: providers.environmentVariable("RELEASE_KEY_PASSWORD").orNull
            if (keystorePath != null && keystorePassword != null && keyAliasValue != null && keyPasswordValue != null) {
                storeFile = file(keystorePath)
                storePassword = keystorePassword
                keyAlias = keyAliasValue
                keyPassword = keyPasswordValue
            }
        }
    }
    buildTypes.getByName("release").signingConfig = signingConfigs.getByName("release")
    buildFeatures { compose = true; buildConfig = true }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")
}
