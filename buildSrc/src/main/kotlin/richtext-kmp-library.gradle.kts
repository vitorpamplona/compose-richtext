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
      kotlinOptions.jvmTarget = "17"
    }
  }
  explicitApi()
}

android {
  compileSdk = 36
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  defaultConfig {
    minSdk = 21
    targetSdk = compileSdk
  }
}
