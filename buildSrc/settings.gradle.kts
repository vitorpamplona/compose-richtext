// Named explicitly: a build that adds its own plugin repository (a mirror in an init script) would
// otherwise replace Gradle's default list, and the Plugin Portal with it.
pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}
