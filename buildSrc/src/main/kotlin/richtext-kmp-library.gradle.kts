import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("com.android.library")
  kotlin("multiplatform")
  id("maven-publish")
  id("signing")
}

repositories {
  google()
  mavenCentral()
}

kotlin {
  jvm()
  androidTarget {
    publishLibraryVariants("release")
    compilations.all {
      kotlinOptions.jvmTarget = JvmTarget.JVM_21.target
    }
  }
  explicitApi()
}

android {
  compileSdk = 36
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  defaultConfig {
    minSdk = 21
    targetSdk = compileSdk
  }
}
