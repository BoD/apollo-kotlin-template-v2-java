plugins {
    java
    id("com.apollographql.apollo") version "2.5.11"
    application
}

group = "org.example"
version = "1.0.0-SNAPSHOT"

allprojects {
    repositories {
//        mavenLocal()
        google()
        mavenCentral()
    }
}


dependencies {
    implementation("com.apollographql.apollo", "apollo-runtime", "2.5.11")
}

apollo {
    packageName.set("com.example")
}

application {
    mainClass.set("com.example.Main")
}

// `./gradlew downloadApolloSchema --endpoint='https://apollo-fullstack-tutorial.herokuapp.com/graphql' --schema=`pwd`/src/main/graphql/schema.graphqls` to download the schema
// `./gradlew run` to run the app
