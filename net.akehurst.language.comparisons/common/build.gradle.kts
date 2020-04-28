plugins {
    kotlin("multiplatform") version ("1.3.71")
}

kotlin {
    jvm("jvm8") {
        //withJava()
        val main by compilations.getting {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
        val test by compilations.getting {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
    }
}

dependencies {
    "commonMainImplementation"(kotlin("stdlib"))
    "commonTestImplementation"(kotlin("test"))
    "commonTestImplementation"(kotlin("test-annotations-common"))

    "jvm8MainImplementation"(kotlin("stdlib-jdk8"))
    "jvm8TestImplementation"(kotlin("test-junit"))

    "jvm8MainImplementation"("org.apache.poi:poi:4.0.0")
    "jvm8MainImplementation"("org.apache.poi:poi-ooxml:4.0.0")
}