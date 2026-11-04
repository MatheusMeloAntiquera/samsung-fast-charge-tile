plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.matheus.fastcharge"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.matheus.fastcharge"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    lint {
        // WRITE_SECURE_SETTINGS é concedida via adb; o lint reclamaria por ser "protegida".
        disable += "ProtectedPermissions"
    }
}
