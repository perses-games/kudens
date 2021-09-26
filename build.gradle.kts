plugins {
    kotlin("multiplatform") version "1.5.31"
    `maven-publish`
}

group = "games.perses"
version = "1.2.1-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
/*
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
*/
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
/*
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
*/

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        //val jvmMain by getting
        //val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        //val nativeMain by getting
        //val nativeTest by getting
    }

    val publicationsFromMainHost =
        listOf(js()).map { it.name } + "kotlinMultiplatform"
    publishing {
        repositories {
            maven {
                name = "releases"
                // change to point to your repo, e.g. http://my.org/repo
                url = uri("https://nexus.astraeus.nl/nexus/content/repositories/releases")
                credentials {
                    val nexusUsername: String by project
                    val nexusPassword: String by project

                    username = nexusUsername
                    password = nexusPassword
                }
            }
            maven {
                name = "snapshots"
                // change to point to your repo, e.g. http://my.org/repo
                url = uri("https://nexus.astraeus.nl/nexus/content/repositories/snapshots")
                credentials {
                    val nexusUsername: String by project
                    val nexusPassword: String by project

                    username = nexusUsername
                    password = nexusPassword
                }
            }
        }
/*        publications {
            matching { it.name in publicationsFromMainHost }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
            }
        }*/
    }
}

