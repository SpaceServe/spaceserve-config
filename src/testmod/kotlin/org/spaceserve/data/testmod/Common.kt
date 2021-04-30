package org.spaceserve.data.testmod

import net.fabricmc.api.ModInitializer
import kotlin.system.exitProcess

object Common : ModInitializer {
    override fun onInitialize() {
        println("Testing...")
        exitProcess(1)
    }
}

