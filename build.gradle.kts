plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("kapt") version "2.2.21"
    alias(libs.plugins.ksp)
}

group = "me.salmonmoses"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_24
}

dependencies {
    testImplementation(kotlin("test"))

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    // Koin Annotations
    implementation(libs.koin.annotations)
    // Koin Annotations KSP Compiler
    ksp(libs.koin.ksp.compiler)
    // define a BOM and its version
    implementation(project.dependencies.platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

    implementation(kotlin("reflect"))
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

tasks.test {
    useJUnitPlatform()
}

kapt {
    correctErrorTypes = true
}