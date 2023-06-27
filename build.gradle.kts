import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {

    val kotlinVersion = "1.8.22"

    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.allopen") version kotlinVersion apply false
    id("org.kordamp.gradle.jandex") version "1.1.0" apply false
    id("io.quarkus") apply false

    id("com.adarshr.test-logger") version "3.2.0"
    id("com.diffplug.spotless") version "6.12.0"
}

allprojects {

    repositories { mavenCentral() }

    apply {
        plugin("kotlin")
        plugin("com.diffplug.spotless")
    }

    spotless {
        kotlin {
            ktlint()
                .editorConfigOverride(
                    mapOf(
                        "ktlint_disabled_rules" to "no-wildcard-imports"
                    )
                )
        }

        kotlinGradle {
            target("build.gradle.kts")
            ktlint().editorConfigOverride(
                mapOf(
                    "ktlint_disabled_rules" to "no-wildcard-imports"
                )
            )
        }
    }
}

subprojects {

    group = "org.acme"
    version = "1.0.0-SNAPSHOT"

    apply {
        plugin("kotlin")
        plugin("kotlin-allopen")
        plugin("io.quarkus")

        plugin("org.kordamp.gradle.jandex")

        plugin("com.adarshr.test-logger")
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<Test> {
        systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    }

    configure<AllOpenExtension> {
        annotation("jakarta.ws.rs.Path")
        annotation("jakarta.enterprise.context.ApplicationScoped")
        annotation("io.quarkus.test.junit.QuarkusTest")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
        kotlinOptions.javaParameters = true
    }

    val quarkusDependenciesBuild = tasks.findByName("quarkusDependenciesBuild")
    val quarkusGenerateCodeTests = tasks.findByName("quarkusGenerateCodeTests")
    val jandex = tasks.findByName("jandex")

    if (quarkusDependenciesBuild != null && jandex != null) {
        quarkusDependenciesBuild.dependsOn(jandex)
    }
    if (quarkusGenerateCodeTests != null && jandex != null) {
        quarkusGenerateCodeTests.dependsOn(jandex)
    }

    //

    testlogger {
        showStandardStreams = true
        showPassedStandardStreams = false
        showSkippedStandardStreams = false
        showFailedStandardStreams = true
        showSummary = true
        showPassed = true
        logLevel = LogLevel.LIFECYCLE
    }
}
