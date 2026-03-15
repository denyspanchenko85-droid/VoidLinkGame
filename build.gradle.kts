plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.shadow.voidlink"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shadow.voidlink"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
