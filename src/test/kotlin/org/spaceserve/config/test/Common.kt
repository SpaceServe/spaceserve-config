package org.spaceserve.config.test

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.fabricmc.api.ModInitializer
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier
import org.junit.Assert
import org.spaceserve.config.IConfigure
import java.nio.file.Files
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import kotlin.system.exitProcess

object Common : ModInitializer {
    override fun onInitialize() {
        testSerializers()
        testLoad()
        testSave()
        testReset()

        println("All tests successful!")

        exitProcess(0)
    }

    private fun testSerializers() {
        val defaultConfig = TestConfig()
        val serializer = Json { encodeDefaults = true; prettyPrint = true }

        assertEquals(
            defaultConfig,
            serializer.decodeFromString<TestConfig>(serializer.encodeToString(TestConfig.serializer(), defaultConfig)),
        )
    }

    private fun testLoad() {
        val testLoadConfigText = """{
  "integer": 100,
  "boolean": true,
  "enum": "OPTION2",
  "identifier": "minecraft:the_nether",
  "block": "minecraft:stone",
  "enchantment": "minecraft:mending",
  "text": "{\"text\":\"test load config\", \"color\":\"green\"}",
  "itemStack": {
    "item": "minecraft:netherite_sword",
    "count": 1,
    "enchantments": [
      {
        "id": "minecraft:sharpness",
        "lvl": 5
      },
      {
        "id": "minecraft:fire_aspect",
        "lvl": 2
      }
    ],
    "repairCost": 34
  }
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

private fun assertEquals(expected: Any?, actual: Any?) {
    // Future proof special case for ItemStack because Mojang is stupid
    if (expected is IConfigure && actual is IConfigure) {
        val expectedProps = expected::class.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .filterIsInstance<KMutableProperty<*>>()

        actual::class.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .filterIsInstance<KMutableProperty<*>>()
            .forEachIndexed { index, property ->
                val expectedProp = expectedProps[index].getter.call(expected)
                val actualProp = property.getter.call(actual)

                if (expectedProp is ItemStack && actualProp is ItemStack) {
                    Assert.assertTrue(
                        "ItemStack [$expectedProp] equals ItemStack [$actualProp]",
                        ItemStack.areEqual(expectedProp, actualProp),
                    )
                }
            }
    } else {
        Assert.assertEquals(expected, actual)
    }
}
