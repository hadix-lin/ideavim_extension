import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.intellij") version "0.7.3"
    id("org.jetbrains.kotlin.jvm") version "1.5.0"
}

intellij {
    pluginName = "IdeaVimExtension"
    version = "IC-2020.2"
    setPlugins("IdeaVIM:0.67")
    updateSinceUntilBuild = false
    downloadSources = true
    intellijRepo = "https://www.jetbrains.com/intellij-repository"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.apiVersion = "1.3"
    kotlinOptions.languageVersion = "1.3"
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

group = "io.github.hadix"

repositories {
    mavenCentral()
}
