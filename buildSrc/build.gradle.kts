repositories {
  google()
  mavenCentral()
}

plugins {
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
  // keep in sync with Dependencies.BuildPlugins.androidGradlePlugin
  implementation("com.android.tools.build:gradle:9.5.0")
  implementation("com.vanniktech.maven.publish:com.vanniktech.maven.publish.gradle.plugin:0.36.0")
  // keep in sync with Dependencies.Kotlin.gradlePlugin
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.21")
  // keep in sync with Dependencies.Compose.desktopVersion
  implementation("org.jetbrains.compose:org.jetbrains.compose.gradle.plugin:1.12.2")
  // keep in sync with Dependencies.Kotlin.version
  implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.4.21")
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.2.0")
  implementation(kotlin("script-runtime"))
}