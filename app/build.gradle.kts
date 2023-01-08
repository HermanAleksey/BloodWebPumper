import org.jetbrains.compose.desktop.application.dsl.*

plugins {
    kotlin("jvm")
    id("java")
    id("org.jetbrains.compose")

    id("org.openjfx.javafxplugin") version "0.0.13"
}

object AppConfig {
    private const val majorVersion = 1
    private const val minorVersion = 0
    private const val patchVersion = 0
    const val versionName = "$majorVersion.$minorVersion.$patchVersion"

    const val packageName = "BloodWebPumper"
    const val description = "App to waste time more efficiently"
    const val copyright = "©2023 AkakiDev team. All rights reserved."
    const val appIconPath = "/src/main/resources/scenes/drawables/bubba.ico"

    const val MAIN_CLASS = "MainKt"
    const val JAVA_HOME = "JAVA_HOME"
    const val outputDirectory = "composeOutput"
}

group = "com.justparokq"
version = AppConfig.versionName

dependencies {
    implementation(compose.desktop.currentOs)

    val jnativehookVersion = "2.1.0"
    val coroutinesVersion = "1.6.4"
    val commonsMath3Version = "3.6.1"
    implementation("com.1stleg:jnativehook:$jnativehookVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$coroutinesVersion")
    implementation("org.apache.commons:commons-math3:$commonsMath3Version")

    val jgraphtVersion = "1.5.1"
    implementation("org.jgrapht:jgrapht-core:$jgraphtVersion")
    implementation("org.jgrapht:jgrapht-io:$jgraphtVersion")
    implementation("org.jgrapht:jgrapht-ext:$jgraphtVersion")
}

javafx {
    modules("javafx.controls", "javafx.fxml")
    version = "17"
}

compose.desktop {
    application {
        mainClass = AppConfig.MAIN_CLASS

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            //can include only necessary modules via         modules("java.sql")
            includeAllModules = true


            packageVersion = AppConfig.versionName

            javaHome = System.getenv(AppConfig.JAVA_HOME)

            outputBaseDir.set(project.buildDir.resolve(AppConfig.outputDirectory))

            packageName = AppConfig.packageName
            description = AppConfig.description
            copyright = AppConfig.copyright

            windows {
                iconFile.set(project.file(AppConfig.appIconPath))
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
        attributes["Main-Class"] = AppConfig.MAIN_CLASS
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}