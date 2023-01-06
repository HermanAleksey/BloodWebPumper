plugins {
    kotlin("jvm")
    id("java")
    // https://github.com/JetBrains/compose-jb/releases
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev686"

//    id ("application")
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "com.justparokq"
version = "1.1-COMPOSE"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.1stleg:jnativehook:2.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.6.4")
    implementation("org.apache.commons:commons-math3:3.6.1")

    implementation("org.jgrapht:jgrapht-core:1.5.1")
    implementation("org.jgrapht:jgrapht-io:1.5.1")
    implementation("org.jgrapht:jgrapht-ext:1.5.1")

    implementation(compose.desktop.currentOs)
}

javafx {
    modules("javafx.controls", "javafx.fxml")
    version = "17"
}

compose.desktop {
    application {
        mainClass = "MainKt"
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