package org.spaceserve.data.testmod

import net.fabricmc.api.ModInitializer
import org.spaceserve.data.testmod.config.TestConfig
import org.spaceserve.data.testmod.config.TestEnum
import kotlin.system.exitProcess

object Common : ModInitializer {
    override fun onInitialize() {
        println("Testing...")

        val conf = TestConfig(
            booleanConfigOption = true,
            enumConfigOption = TestEnum.OPTION3
        )
        conf.enumConfigOption = TestEnum.OPTION1
        conf.save()

        exitProcess(1)
    }
}

