plugins {
    kotlin("jvm")
    id("java")
    id("org.jetbrains.compose")

    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "com.justparokq"
version = "1.1-COMPOSE"

dependencies {
    implementation(compose.desktop.currentOs)

    implementation("com.1stleg:jnativehook:2.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.6.4")
    implementation("org.apache.commons:commons-math3:3.6.1")
}

javafx {
    modules("javafx.controls", "javafx.fxml")
    version = "17"
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
//    nativeDistributions {
//        targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.AppImage, TargetFormat.Exe)
//        packageName = "compose"
//        packageVersion = "1.0.0"
//    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.register<Jar>("uberJar") {
    archiveClassifier.set("uber")

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}