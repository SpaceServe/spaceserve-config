package org.spaceserve.config.helpers

import com.google.gson.JsonParseException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.text.Text
import net.minecraft.text.Texts
import org.spaceserve.config.serializers.EnchantmentSerializer
import org.spaceserve.config.serializers.ItemSerializer
import org.spaceserve.config.serializers.TextSerializer

@Serializable
data class SerializableItemStack(
    @Serializable(with = ItemSerializer::class)
    val item: Item = Items.AIR,

    val count: Int = 0,

    val enchantments: List<LeveledEnchantment>? = null,

    val display: Display? = null,

    val damage: Int = 0,

    val repairCost: Int = 0,

    val hideFlags: Int = 0,
)

@Serializable
data class LeveledEnchantment(
    @Serializable(with = EnchantmentSerializer::class)
    @SerialName("id")
    val enchantment: Enchantment,

    @SerialName("lvl")
    val level: Int = 1,
)

@Serializable(with = TextSerializer::class)
data class Display(
    val name: Text? = null,

    val lore: List<Text>? = null,

    /**
     * Color hex code for color-able items like leather armour
     */
    val color: Int? = null,
)

/**
 * Setting to null will remove all display data. Setting to a [Display] with null properties will only mutate the
 * non-null properties.
 *
 * To remove a specific display property use its corresponding `removeCustom<property>` method
 */
var ItemStack.display: Display?
    get() {
        return if (this.hasTag() && this.tag!!.contains("display", 10)) {
            val displayNbt = this.tag!!.getCompound("display")

            val displayColor =
                if (displayNbt.contains("color", 99)) { displayNbt.getInt("color") }
                else { null }

            val displayLore: MutableList<Text> = mutableListOf()
            if (displayNbt.getType("Lore") == 9.toByte()) {
                val loreStrings = displayNbt.getList("Lore", 8)

                loreStrings.forEachIndexed { i, _ ->
                    try {
                        val loreText = Text.Serializer.fromJson(loreStrings.getString(i))
                        if (loreText != null) {
                            displayLore.add(Texts.setStyleIfAbsent(loreText, ItemStack.LORE_STYLE))
                        }
                    } catch (err: JsonParseException) { }
                }
            }

            Display(
                this.name,
                if (displayLore.isEmpty()) { null } else { displayLore },
                displayColor,
            )
        } else {
            null
        }
    }
    set(value) {
        if (value == null) {
            this.removeCustomName()
        } else {
            if (value.name != null) this.setCustomName(value.name)

            val displayNbt = this.getOrCreateSubTag("display")
            if (value.color != null) {
                displayNbt.putInt("color", value.color)
            }

            val loreNbtList = NbtList()
            if (value.lore != null) {
                value.lore.forEach { loreText ->
                    loreNbtList.add(NbtString.of(Text.Serializer.toJson(loreText)))
                }
            }
            displayNbt.put("Lore", loreNbtList)
        }
    }

fun ItemStack.removeCustomLore() = this.removeDisplayTag("Lore")

fun ItemStack.removeCustomColor() = this.removeDisplayTag("color")

private fun ItemStack.removeDisplayTag(tagName: String) {
    if (this.hasTag() && this.tag!!.contains("display", 10)) {
        this.tag!!.getCompound("display").remove(tagName)
    }
}
