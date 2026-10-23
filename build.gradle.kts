import net.fabricmc.loom.api.LoomGradleExtensionAPI

val obfuscated = stonecutter.eval(stonecutter.current.version, "<=1.21.11")

apply(plugin = if (obfuscated) "net.fabricmc.fabric-loom-remap" else "net.fabricmc.fabric-loom")
apply(plugin = "maven-publish")

version = "${property("mod.version")}+${stonecutter.current.version}"
group = property("mod.group") as String
base.archivesName = property("mod.id") as String

val requiredJava = if (obfuscated) JavaVersion.VERSION_21 else JavaVersion.VERSION_25
val loom = extensions.getByType<LoomGradleExtensionAPI>()

fun mod(configuration: String) = if (obfuscated) "mod" + configuration.replaceFirstChar(Char::uppercase) else configuration

repositories {
    maven("https://jitpack.io") {
        name = "JitPack"
        content { includeGroup("com.github.Shader-Escape.emergency-exit") }
    }
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
        content { includeGroup("maven.modrinth") }
    }
}

configurations.configureEach {
    resolutionStrategy {
        componentSelection.all {
            val exitMinecraft = property("deps.exit_minecraft").toString()
            if (candidate.group == "com.github.Shader-Escape.emergency-exit" && !candidate.version.endsWith("-$exitMinecraft")
                && !candidate.version.endsWith("-SNAPSHOT")) {
                reject("EXIT for another Minecraft version than $exitMinecraft")
            }
        }
        cacheDynamicVersionsFor(10, "minutes")
    }
}

dependencies {
    "minecraft"("com.mojang:minecraft:${property("minecraft")}")
    if (obfuscated) {
        "mappings"(loom.officialMojangMappings())
    }
    mod("implementation")("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    mod("implementation")("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    mod("implementation")("com.github.Shader-Escape.emergency-exit:emergency_exit-fabric:${property("deps.exit")}")

    mod("implementation")("maven.modrinth:malilib:${property("deps.malilib")}")
    mod("implementation")("maven.modrinth:tweakeroo:${property("deps.tweakeroo")}")
    mod("implementation")("maven.modrinth:tweakermore:${property("deps.tweakermore")}")
    mod("implementation")("maven.modrinth:minihud:${property("deps.minihud")}")
    mod("implementation")("maven.modrinth:worldedit-cui:${property("deps.worldeditcui")}")

    mod("implementation")("maven.modrinth:sodium:${property("deps.sodium")}")
    mod("implementation")("maven.modrinth:iris:${property("deps.iris")}")

}
loom.runConfigs.configureEach {
    runDirectory = rootProject.layout.projectDirectory.dir("run/${stonecutter.current.version}")
}

extensions.configure<JavaPluginExtension> {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava
}

tasks {
    withType<JavaCompile>().configureEach {
        options.release = requiredJava.majorVersion.toInt()
    }

    named<ProcessResources>("processResources") {
        val props = mapOf(
            "id" to project.property("mod.id"),
            "name" to project.property("mod.name"),
            "version" to project.version,
            "minecraft" to project.property("mod.mc_dep"),
            "java" to requiredJava.majorVersion
        )
        inputs.properties(props)
        filesMatching("fabric.mod.json") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        inputs.property("mixinJava", mixinJava)
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }

    named<Jar>("jar") {
        val projectName = project.property("mod.id")
        inputs.property("projectName", projectName)
        from(rootProject.file("LICENSE")) {
            rename { "${it}_$projectName" }
        }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(named<AbstractArchiveTask>(if (obfuscated) "remapJar" else "jar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

extensions.configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = property("mod.id") as String
            from(components["java"])
        }
    }
}
