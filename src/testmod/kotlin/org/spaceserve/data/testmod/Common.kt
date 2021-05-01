package org.spaceserve.data.testmod

import net.fabricmc.api.ModInitializer
import net.minecraft.world.World
import org.spaceserve.data.testmod.config.TestConfig
import org.spaceserve.data.testmod.config.TestEnum
import kotlin.system.exitProcess

object Common : ModInitializer {
    override fun onInitialize() {
        println("Testing...")

        val conf = TestConfig(
            booleanConfigOption = true, // not default
            enumConfigOption = TestEnum.OPTION3 // not default
        )
        conf.enumConfigOption = TestEnum.OPTION1 // set back to default
        conf.identifierConfigOption = World.NETHER.value // not default
        conf.save() // only saves boolean and identifier

        exitProcess(1)
    }
}

