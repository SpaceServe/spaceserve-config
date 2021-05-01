package org.spaceserve.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.block.Block
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class BlockSerializer : KSerializer<Block> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Block", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Block) {
        encoder.encodeString(Registry.BLOCK.getId(value).toString())
    }

    override fun deserialize(decoder: Decoder): Block {
        val blockId = Identifier.tryParse(decoder.decodeString()) ?:
        throw SerializationException("Could not deserialize Block '${decoder.decodeString()}'")

        return Registry.BLOCK.get(blockId)
    }
}
