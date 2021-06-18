package org.spaceserve.config.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.spaceserve.config.helpers.LeveledEnchantment
import org.spaceserve.config.helpers.SerializableItemStack
import org.spaceserve.config.helpers.display

class ItemStackSerializer : KSerializer<ItemStack> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ItemStack", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ItemStack) {
        val serializableValue = SerializableItemStack(
            value.item,
            value.count,
            if (value.hasEnchantments()) {
                value.enchantments.toList().map { enchantNbt ->
                    check(enchantNbt is NbtCompound)
                    LeveledEnchantment(
                        enchantment = Registry.ENCHANTMENT.get(Identifier.tryParse(enchantNbt.getString("id")))!!,
                        level = enchantNbt.getInt("lvl"),
                    )
                }
            } else { null },
            value.display,
            value.damage,
            value.repairCost,
            value.hideFlags,
        )

        encoder.encodeSerializableValue(SerializableItemStack.serializer(), serializableValue)
    }

    override fun deserialize(decoder: Decoder): ItemStack {
        val serializableItemStack = decoder.decodeSerializableValue(SerializableItemStack.serializer())

        val itemStack = ItemStack(serializableItemStack.item, serializableItemStack.count)

        serializableItemStack.enchantments?.forEach { enchant ->
            itemStack.addEnchantment(enchant.enchantment, enchant.level)
        }

        itemStack.display = serializableItemStack.display

        // Default checks so that it only adds the nbt tag if these are defined, otherwise unneeded nbt tags are created
        if (serializableItemStack.damage != 0) {
            itemStack.damage = serializableItemStack.damage }
        if (serializableItemStack.repairCost != 0) {
            itemStack.repairCost = serializableItemStack.repairCost }
        if (serializableItemStack.hideFlags != 0) {
            itemStack.orCreateTag.putInt("HideFlags", serializableItemStack.hideFlags)
        }

        return itemStack
    }
}
