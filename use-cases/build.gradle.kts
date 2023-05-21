@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("plugin.serialization") version libs.versions.kotlin
}

dependencies {
    implementation(projects.domain)
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", libs.versions.kserialization.get())
    testImplementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", libs.versions.coroutines.get())
    testImplementation(kotlin("reflect", libs.versions.kotlin.get()))
}