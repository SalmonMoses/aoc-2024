plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("kapt") version "2.0.0"
    id ("com.google.devtools.ksp") version "2.0.20-1.0.25"
}

group = "me.salmonmoses"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    val koin_version = "4.1.0-Beta1"
    val koin_annotations_version = "2.0.0-Beta2"

    // Koin
    implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koin_version"))
    implementation(project.dependencies.platform("io.insert-koin:koin-annotations-bom:$koin_annotations_version"))
    // define a BOM and its version
    implementation(project.dependencies.platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

    implementation("io.insert-koin:koin-core")
    // Koin Annotations
    implementation("io.insert-koin:koin-annotations")
    // Koin Annotations KSP Compiler
    ksp("io.insert-koin:koin-ksp-compiler:$koin_annotations_version")
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