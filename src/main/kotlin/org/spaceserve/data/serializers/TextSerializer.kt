package org.spaceserve.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.text.Text

class TextSerializer : KSerializer<Text> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Text", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Text) {
        encoder.encodeString(Text.Serializer.toJson(value))
    }

    override fun deserialize(decoder: Decoder): Text {
        val textJson = decoder.decodeString()

        return Text.Serializer.fromLenientJson(textJson) as Text
    }
}
