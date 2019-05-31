import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.jetbrains.intellij") version "0.4.8"
    id("org.jetbrains.kotlin.jvm") version "1.3.30"
}

intellij {
    pluginName = "IdeaVimExtension"
    version = "IC-2017.1"
    setPlugins("IdeaVIM:0.49")
    updateSinceUntilBuild = false
    downloadSources = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions.apiVersion = "1.0"
    kotlinOptions.languageVersion = "1.0"
}

group = "io.github.hadix"

repositories {
    mavenCentral()
}
