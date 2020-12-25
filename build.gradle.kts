import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
//import sun.jvmstat.monitor.MonitoredVmUtil.commandLine

plugins {
    kotlin("multiplatform") version "1.4.10"
    application
    kotlin("plugin.serialization") version "1.4.10"
}

group = "course.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
    maven { url = uri("https://jitpack.io") }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
        withJava()
    }
    js(LEGACY) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("io.ktor:ktor-client-core:1.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-serialization:1.4.0")
                implementation("io.ktor:ktor-server-netty:1.4.0")
                implementation("io.ktor:ktor-html-builder:1.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
                implementation(kotlin("script-runtime"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains:kotlin-react:16.13.1-pre.113-kotlin-1.4.0")
                implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.113-kotlin-1.4.0")
                implementation("io.ktor:ktor-client-js:1.4.0")
                implementation("io.ktor:ktor-client-json-js:1.4.0")
                implementation("io.ktor:ktor-client-serialization-js:1.4.0")
                implementation(npm("react", "16.14.0"))
                implementation(npm("react-dom", "16.14.0"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        dependencies {
            implementation("com.github.ascclemens:khttp:0.1.0")
            implementation("io.ktor:ktor-websockets:1.4.0")
            implementation("org.jetbrains.exposed:exposed-core:0.24.1")
            implementation("org.jetbrains.exposed:exposed-dao:0.24.1")
            implementation("org.jetbrains.exposed:exposed-jdbc:0.24.1")
            implementation("com.zaxxer:HikariCP:2.7.8")
            implementation("org.postgresql:postgresql:42.2.2")
            implementation("org.jetbrains.exposed:exposed:0.17.7")
        }
    }
}

application {
    mainClassName = "ServerKt"
}

tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack") {
    outputFileName = "output.js"
}

tasks.getByName<Jar>("jvmJar") {
    dependsOn(tasks.getByName("jsBrowserProductionWebpack"))
    val jsBrowserProductionWebpack = tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack")
    from(File(jsBrowserProductionWebpack.destinationDirectory, jsBrowserProductionWebpack.outputFileName))
}

tasks.getByName<JavaExec>("run") {
//    commandLine("bash start_python.sh")
    dependsOn(tasks.getByName<Jar>("jvmJar"))
    classpath(tasks.getByName<Jar>("jvmJar"))
}