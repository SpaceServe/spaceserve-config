package org.spaceserve.config.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class ItemSerializer : KSerializer<Item> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Item", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Item) {
        encoder.encodeString(Registry.ITEM.getId(value).toString())
    }

    override fun deserialize(decoder: Decoder): Item {
        val itemId = Identifier.tryParse(decoder.decodeString()) ?:
        throw SerializationException("Could not deserialize Item '${decoder.decodeString()}'")

        return Registry.ITEM.get(itemId)
    }
}
