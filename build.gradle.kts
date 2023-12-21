import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
	kotlin("plugin.jpa") version "1.9.20"

	jacoco
	id("org.sonarqube") version "4.4.1.3373"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework:spring-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")

}

//allOpen {
//	annotation("javax.persistence.Entity")
//	annotation("javax.persistence.Embeddable")
//	annotation("javax.persistence.MappedSuperclass")
//}
//
//noArg {
//	annotation("javax.persistence.Entity")
//}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform { }
}

jacoco {
	// JaCoCo 버전
	toolVersion = "0.8.8"

//  테스트결과 리포트를 저장할 경로 변경
//  default는 "${project.reporting.baseDir}/jacoco"
//  reportsDir = file("$buildDir/customJacocoReportDir")
}

tasks.jacocoTestReport {
	reports {
		// 원하는 리포트를 켜고 끌 수 있습니다.
		html.required.set(true)
		xml.required.set(true)
		csv.required.set(false)
//  각 리포트 타입 마다 리포트 저장 경로를 설정할 수 있습니다.
//  html.destination = file("$buildDir/jacocoHtml")
//  xml.destination = file("$buildDir/jacoco.xml")
	}
}
tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			element = "CLASS"

			limit {
				counter = "BRANCH"
				value = "COVEREDRATIO"
				minimum = "0.00".toBigDecimal()
			}
		}
	}
}

val testCoverage by tasks.registering {
	group = "verification"
	description = "Runs the unit tests with coverage."

	dependsOn(":test", ":jacocoTestReport", ":jacocoTestCoverageVerification")
	val jacocoTestReport = tasks.findByName("jacocoTestReport")
	jacocoTestReport?.mustRunAfter(tasks.findByName("test"))
	tasks.findByName("jacocoTestCoverageVerification")?.mustRunAfter(jacocoTestReport)
}

sonar {
	properties {
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.organization", "crayon13")
		property("sonar.projectKey", "crayon13_kotlin-springboot-api")
	}
}