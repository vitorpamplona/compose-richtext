plugins {
  id("richtext-kmp-library")
  id("org.jetbrains.dokka")
}

kotlin {
  // iOS for Amethyst's shared UI. Only the pure-Compose modules: the markdown parsers are JVM-only.
  iosArm64()
  iosSimulatorArm64()

  android {
    namespace = "com.halilibo.richtext.ui"
  }
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
      }
    }
    val commonTest by getting

    val jvmAndroidMain by creating {
      dependsOn(commonMain)
    }

    val androidMain by getting {
      dependsOn(jvmAndroidMain)
    }
    val jvmMain by getting {
      dependsOn(jvmAndroidMain)
    }

    // Wired by hand like the source sets above, which turn off the default hierarchy.
    val iosMain by creating {
      dependsOn(commonMain)
    }
    val iosArm64Main by getting {
      dependsOn(iosMain)
    }
    val iosSimulatorArm64Main by getting {
      dependsOn(iosMain)
    }
  }
}
