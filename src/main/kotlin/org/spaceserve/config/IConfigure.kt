package org.spaceserve.config

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.nio.file.Files
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

/**
 * To create a config, implement this in a data class. Create one instance of that data class and with it, you can
 * [save], [load], and [reset] the config
 */
interface IConfigure {
    /**
     * The file where the config is stored. Defaults to be in the Minecraft config directory in a file named
     * ClassName.json
     *
     * @see fileName
     */
    val configFile: File
        get() = Files.createDirectories(
            FabricLoader
                .getInstance()
                .configDir
        )
            .toFile()
            .resolve("${fileName ?: javaClass.simpleName}.json")

    /**
     * An optional custom filename for the config. To use this, simple override it with the desired filename. Do not
     * include `.json` extension!
     */
    val fileName: String?
        get() = null

    /**
     * Saves the current config to file
     */
    fun save() {
        configFile.writeText(
            Json { prettyPrint = true; encodeDefaults = true }
                .encodeToString(
                    serializer(this::class.starProjectedType),
                    this
                )
        )
    }

    /**
     * Loads the config from file, uses default values if file does not exist.
     *
     * @return this [config][IConfigure] object
     */
    fun load(): IConfigure {
        val loadedConfig = Json.decodeFromString(
            serializer(this::class.starProjectedType),
            if (configFile.exists()) {
                configFile.readText()
            } else {
                configFile.createNewFile()
                "{}"
            }
        ) as IConfigure

        val loadedProperties = loadedConfig::class.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .filterIsInstance<KMutableProperty<*>>()

        this::class.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .filterIsInstance<KMutableProperty<*>>()
            .forEachIndexed { index, property ->
                property.setter.call(this, loadedProperties[index].getter.call(loadedConfig))
            }

        return this
    }

    /**
     * Resets the config values, optionally updates the file
     *
     * @param writeToFile if it should write to file the reset values
     * @return this [config][IConfigure] object
     */
    fun reset(writeToFile: Boolean = false): IConfigure {
        val default = Json.decodeFromString(serializer(this::class.starProjectedType), "{}") as IConfigure

        val defaultProperties = default::class.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .filterIsInstance<KMutableProperty<*>>()

        this::class.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .filterIsInstance<KMutableProperty<*>>()
            .forEachIndexed { index, property ->
                property.setter.call(this, defaultProperties[index].getter.call(default))
            }

        if (writeToFile) {
            this.save()
        }

        return this
    }
}
