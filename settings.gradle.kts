import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

if (!file(".git").exists()) {
    // Leaf start - project setup
    val errorText = """
        
        =====================[ ERROR ]=====================
        The Palladium project directory is not a properly cloned Git repository.
         
         In order to build Palladium from source you must clone
         the Palladium repository using Git, not download a code
         zip from GitHub.
         
         Built Palladium jars are available from tame.gg release infrastructure.
         
         See https://github.com/PaperMC/Paper/blob/main/CONTRIBUTING.md
         for further information on building and modifying Paper forks.
        ===================================================
    """.trimIndent()
    // Leaf end - project setup
    error(errorText)
}

rootProject.name = "palladium"

for (name in listOf("leaf-api", "leaf-server")) {
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
}
