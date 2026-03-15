// 1. Вказуємо Gradle, де шукати самі плагіни
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// 2. Оголошуємо плагіни з конкретними версіями
plugins {
    id("com.android.application") version "8.2.2"
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

// 3. Налаштування репозиторіїв для бібліотек
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// --- Твоя логіка Android (namespace, sdk тощо) ---
// Оскільки ми використовуємо один файл для всього, додаємо блок 'android' сюди:

subprojects {
    apply(plugin = "com.android.application")
    
    configure<com.android.build.api.dsl.ApplicationExtension> {
        namespace = "com.shadow.voidlink"
        compileSdk = 34

        defaultConfig {
            applicationId = "com.shadow.voidlink"
            minSdk = 24
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
            }
        }
        
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}
