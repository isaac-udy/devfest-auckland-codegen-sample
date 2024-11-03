plugins {
    id("java-library")
    id("kotlin")
    alias(libs.plugins.kotlin.ksp)
}

dependencies {
    implementation(libs.kotlin.stdLib)

    implementation(libs.processing.ksp)

    ksp(libs.processing.autoServiceKsp)
    implementation(libs.processing.autoServiceAnnotations)

    implementation(libs.processing.kotlinPoet)
    implementation(libs.processing.kotlinPoet.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}