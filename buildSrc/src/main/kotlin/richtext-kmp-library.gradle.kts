import AndroidConfiguration.compileSdk
import AndroidConfiguration.minSdk
import AndroidConfiguration.targetSdk
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  kotlin("multiplatform")
  id("com.android.kotlin.multiplatform.library")
  id("org.jetbrains.kotlin.plugin.compose")
  id("org.jetbrains.compose")
  id("com.vanniktech.maven.publish")
  id("org.jetbrains.dokka")
  signing
}

repositories {
  google()
  mavenCentral()
}

val signingKey = System.getenv("GPG_PRIVATE_KEY")?.replace("\\n", "\n")
val signingPassword = System.getenv("GPG_PRIVATE_PASSWORD")
val hasSigningKey = signingKey != null && signingPassword != null

signing {
  if (hasSigningKey) {
    useInMemoryPgpKeys(signingKey, signingPassword)
  }
}

// Maven Central credentials are provided via ORG_GRADLE_PROJECT_mavenCentralUsername
// and ORG_GRADLE_PROJECT_mavenCentralPassword environment variables.
mavenPublishing {
  publishToMavenCentral()
  // Only sign when a key is available. Builders without one (JitPack, local
  // publishToMavenLocal) would otherwise fail with "no configured signatory".
  if (hasSigningKey) {
    signAllPublications()
  }
}

kotlin {
  jvm()
  explicitApi()

  android {
    compileSdk = 36
    minSdk = AndroidConfiguration.minSdk

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }
  }
}

