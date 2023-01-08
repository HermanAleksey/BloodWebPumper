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

object LibVersion {
    const val jnativehookVersion = "2.1.0"
    const val coroutinesVersion = "1.6.4"
    const val commonsMath3Version = "3.6.1"
    const val jgraphtVersion = "1.5.1"
}

group = "com.justparokq"
version = AppConfig.versionName

dependencies {
    implementation(compose.desktop.currentOs)

    implementation("com.1stleg:jnativehook:${LibVersion.jnativehookVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${LibVersion.coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:${LibVersion.coroutinesVersion}")
    implementation("org.apache.commons:commons-math3:${LibVersion.commonsMath3Version}")
    implementation("org.jgrapht:jgrapht-core:${LibVersion.jgraphtVersion}")
    implementation("org.jgrapht:jgrapht-io:${LibVersion.jgraphtVersion}")
    implementation("org.jgrapht:jgrapht-ext:${LibVersion.jgraphtVersion}")
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
            //can include only necessary modules via
            modules("com.1stleg:jnativehook:${LibVersion.jnativehookVersion}")
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