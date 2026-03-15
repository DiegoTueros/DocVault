plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {

    namespace = "com.docvault.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

}

dependencies {

    implementation(project(":domain"))
}