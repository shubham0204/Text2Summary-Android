plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
}

group = "io.shubham0204"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.1")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create("release", MavenPublication::class.java) {
            from(components["java"])
            groupId = "io.shubham0204"
            artifactId = "text2summary"
            version = version
        }
    }
}
