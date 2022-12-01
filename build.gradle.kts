import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("java")
	id("org.jetbrains.intellij") version "1.10.0"
	id("org.jetbrains.kotlin.jvm") version "1.7.22"
}

intellij {
	pluginName.set("IdeaVimExtension")
	version.set("2022.3")
	plugins.add("IdeaVIM:2.0.0")
	updateSinceUntilBuild.set(false)
	downloadSources.set(true)
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "17"

}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
}

group = "io.github.hadix"
version = "1.6.6"

repositories {
	mavenCentral()
}