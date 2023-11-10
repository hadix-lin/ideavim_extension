import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.8.10"
	id("org.jetbrains.intellij") version "1.16.0"
}

intellij {
	pluginName.set("IdeaVimExtension")
	version.set("2023.1.2")
	plugins.add("IdeaVIM:2.7.0")
	updateSinceUntilBuild.set(false)
	downloadSources.set(true)
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "17"
}

dependencies {
	implementation(kotlin("stdlib"))
}

group = "io.github.hadix"
version = "1.7.2"

repositories {
	mavenCentral()
}