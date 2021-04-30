package org.spaceserve.data.testmod.config

import kotlinx.serialization.Serializable
import net.minecraft.util.Identifier
import org.spaceserve.data.config.IConfigurate
import org.spaceserve.data.serializers.IdentifierSerializer

/**
 * An example config using [IConfigurate]. Be sure to have default values defined for all options.
 */
@Serializable
data class TestConfig(
    /**
     * A simple integer option
     */
    var integerConfigOption: Int = 1,

    /**
     * A simple boolean option
     */
    var booleanConfigOption: Boolean,

    /**
     * An enum option
     */
    var enumConfigOption: TestEnum = TestEnum.OPTION1,

    /**
     * A Minecraft [Identifier] option, uses [IdentifierSerializer] for custom serialization handling
     */
    @Serializable(with = IdentifierSerializer::class)
    var identifierConfigOption: Identifier = Identifier.tryParse("minecraft:dirt")!!
) : IConfigurate
