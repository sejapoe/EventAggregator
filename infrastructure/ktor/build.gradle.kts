@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.github.johnrengelman.shadow") version libs.versions.shadowjar
    application
}

tasks {
    jar {
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "io.ktor.server.cio.EngineMain",
                    "ImplementationTitle" to project.name,
                    "Implementation-Version" to project.version
                )
            )
        }
    }
    shadowJar {
        manifest.inheritFrom(jar.get().manifest)
    }
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.useCases)
    implementation(projects.adapters.config)

    implementation("io.ktor", "ktor-server-core", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-server-cio", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-server-auth", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-server-auth-jwt", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-server-forwarded-header", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-server-content-negotiation", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-serialization-gson", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-server-call-logging", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-server-default-headers", libs.versions.ktor.get())
    implementation("ch.qos.logback", "logback-classic", libs.versions.logback.get())
    implementation("io.insert-koin", "koin-ktor", libs.versions.koin.get())
    implementation("com.apurebase", "kgraphql-ktor", libs.versions.kgraphql.get())
    implementation("io.bkbn", "kompendium-core", libs.versions.kompendium.get())
    implementation("org.reflections", "reflections", libs.versions.reflections.get())
    implementation(kotlin("reflect", libs.versions.kotlin.get()))

    // Custom KGraphql build for Ktor 2.0 support
    implementation("io.ktor", "ktor-serialization-kotlinx-json", libs.versions.ktor.get())
    implementation("io.ktor:ktor-server-default-headers-jvm:2.3.0")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.0")
    implementation("io.ktor:ktor-server-forwarded-header-jvm:2.3.0")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.0")
    implementation("io.ktor:ktor-server-core-jvm:2.3.0")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.0")
}
