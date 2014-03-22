## WisePersist

WisePersist is simple JPA wrapper which provides @Transactional annotation and pre-configured Guice
module.


### How to use?

Firstly, add the Maven repository to your projects:

In Gradle projects, use:

```
repositories {
    maven {
        url "https://dl.dropboxusercontent.com/u/13110611/mvn-repo"
    }
}
```

In Maven projects, use:

```
<repositories>
    <repository>
        <id>jake-dropbox</id>
        <name>jake-dropbox</name>
        <url>https://dl.dropboxusercontent.com/u/13110611/mvn-repo</url>
    </repository>
</repositories>
```

And then, add dependency to your build.gradle (for Gradle projects) or pom.xml (for Maven
projects).

In Gradle projects, use:

```
    compile 'io.wisetime:wise-persist:1.0.0'
```

In Maven projects, use:

```
<dependency>
    <groupId>io.wisetime</groupId>
    <artifactId>wise-persist</artifactId>
    <version>1.0.0</version>
</dependency>
```
