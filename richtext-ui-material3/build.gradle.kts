plugins {
  id("richtext-kmp-library")
  id("org.jetbrains.dokka")
}

kotlin {
  // iOS for Amethyst's shared UI. Only the pure-Compose modules: the markdown parsers are JVM-only.
  iosArm64()
  iosSimulatorArm64()

  android {
    namespace = "com.halilibo.richtext.ui.material3"
  }
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)

        api(project(":richtext-ui"))
      }
    }
    val commonTest by getting

    val androidMain by getting
    val jvmMain by getting
  }
}
