@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version libs.versions.kotlin apply false
}

allprojects {
    group = "ru.sejapoe"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "kotlin")
    repositories {
        mavenCentral()
    }

    afterEvaluate {
        dependencies {
            "testImplementation"(kotlin("test", libs.versions.kotlin.get()))
            "testImplementation"("io.mockk", "mockk", libs.versions.mockk.get())
        }
    }
}