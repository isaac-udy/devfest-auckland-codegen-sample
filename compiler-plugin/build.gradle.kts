plugins {
    id("java-library")
    id("kotlin")
    alias(libs.plugins.kotlin.ksp)
}

dependencies {
    implementation(libs.kotlin.stdLib)
    compileOnly(libs.kotlin.compilerEmbeddable)

    ksp(libs.processing.autoServiceKsp)
    implementation(libs.processing.autoServiceAnnotations)

    implementation(libs.processing.kotlinPoet)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}