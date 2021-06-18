# SpaceServe Config API

A super simple to use api that uses kotlinx serialization and Kotlin data classes. Simply make a data class that
implements `IConfigure`, and all the necessary methods are right there for you. Saving, loading, and resetting the 
config are simple and documented method calls. 

### Custom serializers
Custom serializers are available for Minecraft's complex types; currently
available are: 
 - Block
 - Enchantment
 - GameProfile
 - Identifier
 - Item
 - ItemStack
 - Text
 - UUID

## Add to your project
```kotlin
repositories {
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
}

dependencies {
    // Config
    modImplementation("maven.modrinth:config:0.2.0")
    include("maven.modrinth:config:0.2.0")
}
```
