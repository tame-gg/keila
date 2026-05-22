import paper.libs.com.google.gson.Gson

plugins {
    `java-library`
    `maven-publish`
    idea
}

java {
    withSourcesJar()
    withJavadocJar()
}

val annotationsVersion = "26.1.0" // Leaf - Bump Dependencies
val adventureVersion = "4.26.1"
val bungeeCordChatVersion = "1.21-R0.2-deprecated+build.21"
// Leaf start - Bump Dependencies
val slf4jVersion = "2.0.17"
val log4jVersion = "2.25.3"
// Leaf end - Bump Dependencies

val apiAndDocs: Configuration by configurations.creating {
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.SOURCES))
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
}
configurations.api {
    extendsFrom(apiAndDocs)
}

// Configure mockito agent that is needed in newer Java versions
val mockitoAgent = configurations.register("mockitoAgent")
abstract class MockitoAgentProvider : CommandLineArgumentProvider {
    @get:CompileClasspath
    abstract val fileCollection: ConfigurableFileCollection

    override fun asArguments(): Iterable<String> {
        return listOf("-javaagent:" + fileCollection.files.single().absolutePath)
    }
}

dependencies {
    // api dependencies are listed transitively to API consumers
    // Leaf start - Bump Dependencies
    // TODO: Waiting Paper, breaks with Eco/EcoEnchant since 33.5.0
    api("com.google.guava:guava:33.4.0-jre")
    // TODO: Waiting Paper, Gson has breaking change since 2.12.0
    // TODO: See https://github.com/google/gson/commit/6c2e3db7d25ceceabe056aeb8b65477fdd509214
    api("com.google.code.gson:gson:2.11.0")
    api("org.yaml:snakeyaml:2.6")
    // Leaf end - Bump Dependencies
    api("org.joml:joml:1.10.8") {
        isTransitive = false // https://github.com/JOML-CI/JOML/issues/352
    }
    // TODO: Breaking changes in 8.5.17/18
    // TODO: See https://github.com/vigna/fastutil/commit/c6434abd1177b9933c68f11005ec457d5abf58d3
    api("it.unimi.dsi:fastutil:8.5.15")  // Leaf - Bump Dependencies
    api("org.apache.logging.log4j:log4j-api:$log4jVersion")
    api("org.slf4j:slf4j-api:$slf4jVersion")
    api("com.mojang:brigadier:1.3.10")
    api("io.sentry:sentry:8.34.1") // Pufferfish

    // Deprecate bungeecord-chat in favor of adventure
    api("net.md-5:bungeecord-chat:$bungeeCordChatVersion") {
        exclude("com.google.guava", "guava")
    }

    apiAndDocs(platform("net.kyori:adventure-bom:$adventureVersion"))
    apiAndDocs("net.kyori:adventure-api")
    apiAndDocs("net.kyori:adventure-text-minimessage")
    apiAndDocs("net.kyori:adventure-text-serializer-gson")
    apiAndDocs("net.kyori:adventure-text-serializer-legacy")
    apiAndDocs("net.kyori:adventure-text-serializer-plain")
    apiAndDocs("net.kyori:adventure-text-logger-slf4j")

    // Leaf start - Bump Dependencies
    api("org.apache.maven:maven-resolver-provider:3.9.13") // make API dependency for Paper Plugins
    implementation("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.27") // Dreeam TODO - Update to 2.0.1
    implementation("org.apache.maven.resolver:maven-resolver-transport-http:1.9.27") // Dreeam TODO - Update to 2.0.1
    // Leaf end - Bump Dependencies

    // Annotations - Slowly migrate to jspecify
    val annotations = "org.jetbrains:annotations:$annotationsVersion"
    compileOnly(annotations)
    testCompileOnly(annotations)

    val checkerQual = "org.checkerframework:checker-qual:3.54.0" // Leaf - Bump Dependencies
    compileOnlyApi(checkerQual)
    testCompileOnly(checkerQual)

    api("org.jspecify:jspecify:1.0.0")

    // Test dependencies
    // Leaf start - Bump Dependencies
    testImplementation("org.apache.commons:commons-lang3:3.20.0")
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.0-M1")
    testImplementation("org.hamcrest:hamcrest:3.0")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.ow2.asm:asm-tree:9.9.1")
    mockitoAgent("org.mockito:mockito-core:5.23.0") { isTransitive = false } // configure mockito agent that is needed in newer java versions
    // Leaf end - Bump Dependencies
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Leaf start - Bump Dependencies
    // commons-lang3 is removed in maven-resolver-provider since 3.9.8
    // Add this because bukkit api still need it.
    compileOnly("org.apache.commons:commons-lang3:3.20.0")
    // Leaf end - Bump Dependencies
}

val generatedDir: java.nio.file.Path = rootProject.layout.projectDirectory.dir("paper-api/src/generated/java").asFile.toPath() // Leaf - project setup
idea {
    module {
        generatedSourceDirs.add(generatedDir.toFile())
    }
}
sourceSets {
    main {
        java {
            srcDir(generatedDir)
            // Leaf start - project setup
            srcDir(file("../paper-api/src/main/java"))
        }
        resources {
            srcDir(file("../paper-api/src/main/resources"))
        }
    }
    test {
        java {
            srcDir(file("../paper-api/src/test/java"))
        }
        resources {
            srcDir(file("../paper-api/src/test/resources"))
            // Leaf end - project setup
        }
    }
}

val outgoingVariants = arrayOf("runtimeElements", "apiElements", "sourcesElements", "javadocElements")
val mainCapability = "${project.group}:${project.name}:${project.version}"
configurations {
    val outgoing = outgoingVariants.map { named(it) }
    for (config in outgoing) {
        config {
            attributes {
                attribute(io.papermc.paperweight.util.mainCapabilityAttribute, mainCapability)
            }
            outgoing {
                capability(mainCapability)
                // Paper-MojangAPI has been merged into Paper-API
                capability("io.papermc.paper:paper-mojangapi:${project.version}")
                capability("com.destroystokyo.paper:paper-mojangapi:${project.version}")
                // Conflict with old coordinates
                capability("com.destroystokyo.paper:paper-api:${project.version}")
                capability("org.spigotmc:spigot-api:${project.version}")
                capability("org.bukkit:bukkit:${project.version}")
            }
        }
    }
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        // For Brigadier API
        outgoingVariants.forEach {
            suppressPomMetadataWarningsFor(it)
        }
        from(components["java"])
    }
}

abstract class GenerateApiVersioningFile : DefaultTask() {
    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Input
    abstract val projectVersion: Property<String>

    @get:Input
    abstract val apiVersion: Property<String>

    @TaskAction
    fun generate() {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        val map = mapOf(
            "version" to projectVersion.get(),
            "currentApiVersion" to apiVersion.get()
        )
        file.writeText(Gson().toJson(map))
    }
}

// Gale start - hide irrelevant compilation warnings
tasks.withType<JavaCompile> {
    val compilerArgs = options.compilerArgs
    compilerArgs.add("-Xlint:-module")
    compilerArgs.add("-Xlint:-removal")
    compilerArgs.add("-Xlint:-dep-ann")
    compilerArgs.add("--enable-preview") // Keila - keep API compile args aligned with server
    compilerArgs.add("--add-modules=jdk.incubator.vector") // Gale - Pufferfish - SIMD support
}
// Gale end - hide irrelevant compilation warnings
val generateApiVersioningFile = tasks.register<GenerateApiVersioningFile>("generateApiVersioningFile") {
    outputFile.set(layout.buildDirectory.file("apiVersioning.json"))
    projectVersion.set(project.version.toString())
    apiVersion.set(rootProject.providers.gradleProperty("apiVersion"))
}

tasks.jar {
    from(generateApiVersioningFile.flatMap { it.outputFile })
    manifest {
        attributes(
            "Automatic-Module-Name" to "org.bukkit"
        )
    }

    // Gale start - package license into jar
    from("${project.projectDir}/LICENSE.txt") {
        into("")
    }
    // Gale end - package license into jar
}

abstract class Services {
    @get:Inject
    abstract val fileSystemOperations: FileSystemOperations
}
val services = objects.newInstance<Services>()

tasks.withType<Javadoc>().configureEach {
    val options = options as StandardJavadocDocletOptions
    options.overview = "../paper-api/src/main/javadoc/overview.html" // Leaf - project setup
    options.use()
    options.isDocFilesSubDirs = true
    options.links(
        // Leaf start - Bump Dependencies
        "https://guava.dev/releases/33.5.0-jre/api/docs/",
        "https://www.javadocs.dev/org.yaml/snakeyaml/2.5/",
        // Leaf end - Bump Dependencies
        "https://www.javadocs.dev/org.jetbrains/annotations/$annotationsVersion/",
        "https://www.javadocs.dev/org.joml/joml/1.10.8/",
        "https://www.javadocs.dev/com.google.code.gson/gson/2.11.0",
        "https://jspecify.dev/docs/api/",
        "https://jd.advntr.dev/api/$adventureVersion/",
        "https://jd.advntr.dev/key/$adventureVersion/",
        "https://jd.advntr.dev/text-minimessage/$adventureVersion/",
        "https://jd.advntr.dev/text-serializer-gson/$adventureVersion/",
        "https://jd.advntr.dev/text-serializer-legacy/$adventureVersion/",
        "https://jd.advntr.dev/text-serializer-plain/$adventureVersion/",
        "https://jd.advntr.dev/text-logger-slf4j/$adventureVersion/",
        "https://www.javadocs.dev/org.slf4j/slf4j-api/$slf4jVersion/",
        "https://logging.apache.org/log4j/2.x/javadoc/log4j-api/",
        "https://www.javadocs.dev/org.apache.maven.resolver/maven-resolver-api/1.9.25", // Leaf - Bump Dependencies
    )
    options.tags("apiNote:a:API Note:")
    options.tags("implNote:a:Implementation Note:")

    inputs.files(apiAndDocs).ignoreEmptyDirectories().withPropertyName(apiAndDocs.name + "-configuration")
    val apiAndDocsElements = apiAndDocs.elements
    doFirst {
        options.addStringOption(
            "sourcepath",
            apiAndDocsElements.get().map { it.asFile }.joinToString(separator = File.pathSeparator, transform = File::getPath)
        )
    }

    // workaround for https://github.com/gradle/gradle/issues/4046
    inputs.dir("../paper-api/src/main/javadoc").withPropertyName("javadoc-sourceset") // Leaf - project setup
    val fsOps = services.fileSystemOperations
    doLast {
        fsOps.copy {
            from("../paper-api/src/main/javadoc") { // Leaf - project setup
                include("**/doc-files/**")
            }
            into("build/docs/javadoc")
        }
    }

    options.addStringOption("Xdoclint:none", "-quiet") // Gale - hide irrelevant compilation warnings
    options.addStringOption("-add-modules", "jdk.incubator.vector") // Gale - Pufferfish - SIMD support
}

tasks.test {
    useJUnitPlatform()

    // configure mockito agent that is needed in newer java versions
    val provider = objects.newInstance<MockitoAgentProvider>()
    provider.fileCollection.from(mockitoAgent)
    jvmArgumentProviders.add(provider)
}

// Compile tests with -parameters for better junit parameterized test names
tasks.compileTestJava {
    options.compilerArgs.add("-parameters")
}

val scanJarForBadCalls by tasks.registering(io.papermc.paperweight.tasks.ScanJarForBadCalls::class) {
    badAnnotations.add("Lio/papermc/paper/annotation/DoNotUse;")
    jarToScan.set(tasks.jar.flatMap { it.archiveFile })
    classpath.from(configurations.compileClasspath)
}
// Leaf start - Bump Dependencies
repositories {
    mavenCentral()
}
// Leaf end - Bump Dependencies
tasks.check {
    dependsOn(scanJarForBadCalls)
}
