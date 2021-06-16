package org.spaceserve.data.test

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.fabricmc.api.ModInitializer
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier
import org.junit.Assert.*
import org.spaceserve.data.test.config.TestConfig
import org.spaceserve.data.test.config.TestEnum
import java.nio.file.Files
import kotlin.system.exitProcess

object Common : ModInitializer {
    override fun onInitialize() {
        testLoad()
        testSave()
        testReset()
        // testSerializers()

        exitProcess(0)
    }

    private fun testLoad() {
        val testLoadConfigText = """{
  "integer": 100,
  "boolean": true,
  "enum": "OPTION2",
  "identifier": "minecraft:the_nether",
  "block": "minecraft:stone",
  "enchantment": "minecraft:mending",
  "text": "{\"text\":\"test load config\", \"color\":\"green\"}"
}"""

        // Create the config file
        Files.createDirectories(
            net.fabricmc.loader.api.FabricLoader
                .getInstance()
                .configDir
        )
            .toFile()
            .resolve("TestConfig.json")
            .let {
                it.createNewFile()
                it.writeText(
                    testLoadConfigText
                )
            }

        val loadedConfig = TestConfig().load() as TestConfig
        val expectedConfig = Json.decodeFromString<TestConfig>(testLoadConfigText)

        assertEquals(expectedConfig, loadedConfig)
    }

    private fun testSave() {
        // Remove file in case it already exists
        Files.createDirectories(
            net.fabricmc.loader.api.FabricLoader
                .getInstance()
                .configDir
        )
            .toFile()
            .resolve("TestConfig.json")
            .delete()

        TestConfig().save()

        assertEquals(TestConfig(), TestConfig().load())
    }

    private fun testReset() {
        val testConfig = TestConfig(
            200,
            true,
            TestEnum.OPTION2,
            Identifier("minecraft:the_end"),
            Items.GLOW_ITEM_FRAME,
            Blocks.GOLD_BLOCK,
            Enchantments.FLAME,
            LiteralText("Not default value")
        )

        testConfig.save() // save the non-default values

        val defaultConfig = TestConfig()

        // Resets values of object to default
        assertEquals(defaultConfig, testConfig.reset(true))
        // Saves to file the defaults
        assertEquals(defaultConfig, TestConfig().load())
    }
}

