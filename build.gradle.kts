import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `java-library`
    id("org.jetbrains.dokka") version "1.4.32"
    kotlin("jvm") version "1.6.10"
    `maven-publish`
    application
    signing
}

group = "top.mughome.utils"
version = "0.0.4"

repositories {
    maven("https://mvn.mughome.top/repository/maven-public/")
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10")
    implementation("org.json:json:20220320")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("com.alibaba:fastjson:1.2.80")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.6")
    implementation("org.apache.directory.studio:org.apache.commons.lang:2.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

sourceSets {
    main {
        java {
            include("**/*.java")
            include("**/*.kt")
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "top.mughome.utils"
            artifactId = "flarum"
            version = "0.0.4"

            from(components["java"])

            pom {
                name.set("Flarum Lib for MUGHome")
                description.set("A Util Tool for Flarum Things")
                developers {
                    developer {
                        id.set("Yang")
                        name.set("Yang")
                        email.set("1799579663@qq.com")
                    }

                    developer {
                        id.set("siscon")
                        name.set("siscon")
                        email.set("1748329745@qq.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/MUGHomeDev/MUGHome.Utils.Flarum.git")
                    developerConnection.set("scm:git:ssh://git@github.com:MUGHomeDev/MUGHome.Utils.Flarum.git")
                    url.set("https://github.com/MUGHomeDev/MUGHome.Utils.Flarum")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = uri("https://mvn.mughome.top/repository/maven-releases/")
            val snapshotsRepoUrl = uri("https://mvn.mughome.top/repository/maven-snapshots/")
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            credentials {
                username = properties["repoUser"].toString()
                password = properties["repoPassword"].toString()
            }
        }
    }
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}