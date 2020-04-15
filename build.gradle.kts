import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.jetbrains.intellij") version "0.4.9"
    id("org.jetbrains.kotlin.jvm") version "1.3.50"
}

intellij {
    pluginName = "IdeaVimExtension"
    version = "IC-2020.1"
    setPlugins("IdeaVIM:0.56")
    updateSinceUntilBuild = false
    downloadSources = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions.apiVersion = "1.3"
    kotlinOptions.languageVersion = "1.3"
    kotlinOptions.jvmTarget = "1.8"
}

group = "io.github.hadix"

repositories {
    mavenCentral()
}
