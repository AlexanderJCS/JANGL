plugins {
    id("java")
}

group = "org.example"
version = "5.1.2"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    val dependencies = configurations
            .runtimeClasspath
            .get()
            .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// LWJGL

val lwjglVersion = "3.3.2"
val jomlVersion = "1.10.5"

var allNatives = arrayOf(
    "natives-linux",
    "natives-linux-arm64", "natives-linux-arm32",
    "natives-macos", "natives-macos-arm64",
    "natives-windows", "natives-windows-arm64", "natives-windows-x86"
)

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.joml", "joml", jomlVersion)

    allNatives.forEach() { lwjglNatives ->
        implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

        runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    }
}
