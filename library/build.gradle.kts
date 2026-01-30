import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	`maven-publish`
	id("kotlin-parcelize")
//	id("com.google.devtools.ksp")
//	id("com.google.dagger.hilt.android")
}

android {
	namespace = "lib.virgo.showcase"
	compileSdk = 36

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlin {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_17)
		}
	}
	buildFeatures {
		viewBinding = true
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)

//	implementation(libs.hilt.android)
//	ksp(libs.hilt.android.compiler)

	implementation(libs.glide)
}

publishing.publications {
	create<MavenPublication>("release") {
		afterEvaluate {
			from(components["release"])
		}
	}
}