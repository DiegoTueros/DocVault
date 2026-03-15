plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {

    namespace = "com.docvault.feature.documents"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

}

dependencies {

    implementation(project(":domain"))
    implementation(project(":core"))

    implementation(libs.coroutines.android)

}