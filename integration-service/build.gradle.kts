import com.google.protobuf.gradle.*

plugins {
	java
	idea
	id("com.google.protobuf") version "0.8.18"
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
}

group = "com.integration.service"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")
	implementation("com.opencsv:opencsv:5.8")
	implementation("io.grpc:grpc-netty-shaded:1.62.2")
	implementation("io.grpc:grpc-protobuf:1.62.2")
	implementation("io.grpc:grpc-stub:1.62.2")
	implementation("javax.annotation:javax.annotation-api:1.3.2")
	compileOnly("org.apache.tomcat:annotations-api:6.0.53")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.mockito:mockito-core")
	testImplementation("io.grpc:grpc-testing:1.62.2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.21.7"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.62.2"
		}
	}
	generateProtoTasks {
		all().forEach { task ->
			task.plugins {
				id("grpc")
			}
		}
	}
}

sourceSets {
	main {
		java {
			srcDirs("src/main/java", "${project.buildDir}/generated/source/proto/main/java", "${project.buildDir}/generated/source/proto/main/grpc")
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
