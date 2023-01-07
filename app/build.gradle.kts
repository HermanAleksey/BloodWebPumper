import org.jetbrains.compose.desktop.application.dsl.*

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

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            //can include only necessary modules via         modules("java.sql")
            includeAllModules = true

            val majorVersion = 1
            val minorVersion = 0
            val patchVersion = 0
            packageVersion = "$majorVersion.$minorVersion.$patchVersion"

            javaHome = System.getenv("JAVA_HOME")

            outputBaseDir.set(project.buildDir.resolve("composeOutput"))

            packageName = "BloodWebPumper"
            description = "App to waste time more efficiently"
            copyright = "©2023 AkakiDev team. All rights reserved."

            windows {
                iconFile.set(project.file("/src/main/resources/scenes/drawables/bubba.ico"))

            }
            //tutorial to configuration and tasks running
            //https://github.com/JetBrains/compose-jb/tree/master/tutorials/Native_distributions_and_local_execution#minification--obfuscation
        }
    }
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