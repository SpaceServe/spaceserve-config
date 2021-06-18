package org.spaceserve.config.helpers

import kotlinx.serialization.Serializable
import org.spaceserve.config.serializers.UuidSerializer
import java.util.*

@Serializable
internal data class SerializableGameProfile(
    val name: String? = null,
    @Serializable(with = UuidSerializer::class)
    val uuid: UUID? = null,
)
