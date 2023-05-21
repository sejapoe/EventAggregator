rootProject.name = "EventAggregator"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    ":domain",
    ":use-cases",
    ":infrastructure:ktor",
    ":adapters:auth",
    ":adapters:config",
    ":adapters:log",
    ":adapters:repo"
)

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.8.21")
            version("shadowjar", "8.1.1")
            version("coroutines", "1.7.0-RC")
            version("ktor", "2.3.0")
            version("kompendium", "3.14.0")
            version("slf4j", "2.0.7")
            version("logback", "1.4.7")
            version("koin", "3.4.0")
            version("kgraphql", "0.19.0")
            version("exposed", "0.41.1")
            version("jwt", "4.4.0")
            version("bcrypt", "0.10.2")
            version("hikari", "5.0.1")
            version("mariadb", "3.1.3")
            version("h2", "2.1.214")
            version("flyway", "9.16.3")
            version("mockk", "1.13.5")
            version("reflections", "0.10.2")
            version("updateversions", "0.46.0")
            version("postgresql", "42.3.1")
        }
    }
}
include("domain")
