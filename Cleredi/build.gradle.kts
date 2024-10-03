plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
    }
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory.asFile)
}
