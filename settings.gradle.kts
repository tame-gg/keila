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
        The Keila project directory is not a properly cloned Git repository.
         
         In order to build Keila from source you must clone
         the Keila repository using Git, not download a code
         zip from GitHub.
         
         Built Keila jars are available from tame.gg release infrastructure.
         
         See https://github.com/PaperMC/Paper/blob/main/CONTRIBUTING.md
         for further information on building and modifying Paper forks.
        ===================================================
    """.trimIndent()
    // Leaf end - project setup
    error(errorText)
}

rootProject.name = "keila"

for (name in listOf("leaf-api", "leaf-server")) {
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
}
