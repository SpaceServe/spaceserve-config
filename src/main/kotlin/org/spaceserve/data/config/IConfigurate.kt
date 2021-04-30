package org.spaceserve.data.config

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files
import kotlin.reflect.full.starProjectedType

/**
 * To create a config, implement this in a data class. Create one instance of that data class and with it, you can
 * [save], [load], and [reset] the config
 */
interface IConfigurate {
    /**
     * Saves the current config to file
     */
    fun save() {
        Files.createDirectories(
            FabricLoader
                .getInstance()
                .configDir
        )
            .toFile()
            .resolve("${javaClass.simpleName}.json")
            .writeText(
                Json { prettyPrint = true }
                    .encodeToString(
                        serializer(this::class.starProjectedType),
                        this
                    )
            )
    }

    /**
     * Loads the config from file, uses default if doesn't exist
     */
    fun load() {

    }

    /**
     * Set all options to default values
     *
     * @param updateFile whether or not it should update the file with the reset values, defaults to false
     */
    fun reset(updateFile: Boolean = false) {
        if (updateFile) save()
    }
}