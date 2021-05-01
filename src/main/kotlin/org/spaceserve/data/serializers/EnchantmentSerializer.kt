package org.spaceserve.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.enchantment.Enchantment
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class EnchantmentSerializer : KSerializer<Enchantment> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Enchantment", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Enchantment) {
        encoder.encodeString(Registry.ENCHANTMENT.getId(value).toString())
    }

    override fun deserialize(decoder: Decoder): Enchantment {
        val enchantmentId = Identifier.tryParse(decoder.decodeString()) ?:
        throw SerializationException("Could not deserialize Enchantment '${decoder.decodeString()}'")

        return Registry.ENCHANTMENT.get(enchantmentId) ?:
        throw SerializationException("Could not deserialize Enchantment '$enchantmentId'")
    }
}
