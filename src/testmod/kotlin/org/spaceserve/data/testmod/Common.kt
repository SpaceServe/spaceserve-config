package org.spaceserve.data.testmod

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.FabricLoader
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Items
import net.minecraft.world.World
import org.spaceserve.data.testmod.config.TestConfig
import org.spaceserve.data.testmod.config.TestEnum
import kotlin.system.exitProcess

object Common : ModInitializer {
    /**
     * The config for this mod. Can be a val if you never intend to reload it, otherwise use var.
     */
    var config = TestConfig()

    var tests = 0
    var fails = 0

    override fun onInitialize() {
        FabricLoader.INSTANCE.configDir.toFile().resolve("ExampleConfig.json").writeText("""{
    "integer": 100,
    "boolean": true,
    "enum": "OPTION2",
    "identifier": "minecraft:the_nether",
    "item": "minecraft:diamond",
    "block": "minecraft:dirt",
    "enchantment": "minecraft:multishot"
}""")
        // Loading the new config now that changes were made
        config = config.load() as TestConfig

        // Tests
        test(100, config.integer)
        test(true, config.boolean)
        test(TestEnum.OPTION2, config.enum)
        test(World.NETHER.value, config.identifier)
        test(Items.DIAMOND, config.item)
        test(Blocks.DIRT, config.block)
        test(Enchantments.MULTISHOT, config.enchantment)

        println("${tests - fails}/$tests tests passed")

        exitProcess(1)
    }

    private fun test(expected: Any, actual: Any?) {
        println("===< Test $tests | Passes: ${expected == actual} >===")
        println("Expected: $expected")
        println("Actual:   $actual")
        println()

        if (expected != actual) fails++
        tests++
    }
}

