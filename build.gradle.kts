import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.8.10"
	id("org.jetbrains.intellij") version "1.11.0"
}

intellij {
	pluginName.set("IdeaVimExtension")
	version.set("2022.3.1")
	plugins.add("IdeaVIM:2.0.0")
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
version = "1.6.10"

repositories {
	mavenCentral()
}