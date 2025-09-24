import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("com.android.library")
  kotlin("android")
}

kotlin {
  explicitApi()
  compilerOptions {
    jvmTarget = JvmTarget.JVM_21
  }
}

android {
  compileSdk = AndroidConfiguration.compileSdk

  defaultConfig {
    minSdk = AndroidConfiguration.minSdk
    targetSdk = AndroidConfiguration.targetSdk
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  buildFeatures {
    compose = true
  }

  publishing {
    singleVariant("release") {
      withSourcesJar()
    }
  }
}