plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	runtimeOnly("com.mysql:mysql-connector-j")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.assertj:assertj-core:3.24.2")


}

tasks.withType<Test> {
	useJUnitPlatform()
}
