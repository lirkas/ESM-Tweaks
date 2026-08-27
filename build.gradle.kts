plugins {
    id("java-library")
    id("maven-publish")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
    id("eclipse")
    id("com.gtnewhorizons.retrofuturagradle") version "2.0.2"
}

val modId: String = project.property("modID") as String
val minecraftVersion: String = project.property("minecraftVersion") as String
version = project.property("modVersion") as String
group = project.property("group") as String

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

minecraft {
    mcVersion.set(minecraftVersion)
    val (mappingsChannel, mappingsVersion) = (project.property("mcpMappings") as String).split('_', limit = 2)
    mcpMappingChannel.set(mappingsChannel)
    mcpMappingVersion.set(mappingsVersion)
    // old build system had configurable target forge version; rfg does not allow this

    injectedTags.put("VERSION", project.version)
    useDependencyAccessTransformers.set(true)

    // old build system used separate run directories for the test client and server; rfg does not allow this
    extraRunJvmArguments.add("-Dlog4j.configurationFile=log4j2-devfix.xml")
    username.set("Tweaker")
    userUUID.set("00000000-0000-0000-0000-000000000000")
}

tasks.injectTags {
    outputClassName.set("${project.group}.ESMTweaksConsts")
}

tasks.applyJST {
    // https://github.com/GTNewHorizons/RetroFuturaGradle/issues/101
    javaLauncher.set(minecraft.getToolchainLauncher(project, 25))
}

repositories {
    maven {
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
}

dependencies {
    implementation(rfg.deobf("curse.maven:tool-progression-266550:3270468"))
    implementation(rfg.deobf("curse.maven:epic-siege-mod-229449:3356157"))
}

tasks.jar {
    archiveBaseName = modId
    val logLevel: String

    // -Prelease
    if (project.hasProperty("release")) {
        logLevel = "INFO"
    } else {
        logLevel = "ALL"
        archiveClassifier.set("dev")
    }

    manifest {
        attributes(
            // This sets the log level for this mod's logger
            "LogLevel" to logLevel
        )
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("mcmod.info") {
        expand("modVersion" to project.version, "minecraftVersion" to minecraftVersion)
    }
}
