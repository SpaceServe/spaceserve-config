# SpaceServe Config API

A super simple to use api that uses kotlinx serialization and Kotlin data classes. Simply make a data class that
implements `IConfigure`, and all the necessary methods are right there for you. Saving, loading, and resetting the 
config are simple and documented method calls. Custom serializers are available for Minecraft's complex types; currently
available are: Block, Enchantment, Identifier, Item, Text

## Add to your project
```kotlin
repositories {
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    // Config
    modImplementation("com.github.SpaceServe:spaceserve-config:0.1.0")
    include("com.github.SpaceServe:spaceserve-config:0.1.0")
}
```
