package org.spaceserve.config.serializers

import com.mojang.authlib.GameProfile
import com.mojang.authlib.minecraft.MinecraftSessionService
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.client.MinecraftClient
import net.minecraft.server.MinecraftServer
import org.spaceserve.config.helpers.SerializableGameProfile

class GameProfileSerializer : KSerializer<GameProfile> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("GameProfile", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: GameProfile) {
        encoder.encodeSerializableValue(
            SerializableGameProfile.serializer(),
            SerializableGameProfile(
                value.name,
                value.id,
            ),
        )
    }

    override fun deserialize(decoder: Decoder): GameProfile {
        val serializableGameProfile = decoder.decodeSerializableValue(SerializableGameProfile.serializer())

        return GameProfile(serializableGameProfile.uuid, serializableGameProfile.name)
    }
}
