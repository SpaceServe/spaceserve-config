package org.spaceserve.config.test

import com.mojang.authlib.GameProfile
import com.mojang.authlib.GameProfileRepository
import kotlinx.serialization.Serializable
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.spaceserve.config.IConfigure
import org.spaceserve.config.serializers.*

/**
 * An example config using [IConfigure]. Be sure to define default values for all options. Default is
 * ```json
 * {
 *   "integer": 0,
 *   "boolean": false,
 *   "enum": "OPTION1",
 *   "identifier": "minecraft:air",
 *   "item": "minecraft:diamond",
 *   "block": "minecraft:acacia_sign",
 *   "enchantment": "minecraft:efficiency",
 *   "text": "{\"text\":\"default\"}"
 *   "itemStack": {
 *     "item": "minecraft:diamond",
 *     "count": 1
 *   },
 *   "gameProfile": {
 *     "uuid": null,
 *     "name": "Player42"
 *   }
 * }
 * ```
 */
@Serializable
data class TestConfig(
    /**
     * A simple integer option
     */
    var integer: Int = 0,

    /**
     * A simple boolean option
     */
    var boolean: Boolean = false,

    /**
     * An enum option
     */
    var enum: TestEnum = TestEnum.OPTION1,

    /**
     * A Minecraft [Identifier] option, uses [IdentifierSerializer] for custom serialization handling
     */
    @Serializable(with = IdentifierSerializer::class)
    var identifier: Identifier = Identifier.tryParse("minecraft:air")!!,

    /**
     * A Minecraft [Item] option, uses [ItemSerializer] for custom serialization handling
     */
    @Serializable(with = ItemSerializer::class)
    var item: Item = Items.DIAMOND,

    /**
     * A Minecraft [Block] option, uses [BlockSerializer] for custom serialization handling
     */
    @Serializable(with = BlockSerializer::class)
    var block: Block = Blocks.ACACIA_SIGN,

    /**
     * A Minecraft [Enchantment] option, uses [EnchantmentSerializer] for custom serialization handling
     */
    @Serializable(with = EnchantmentSerializer::class)
    var enchantment: Enchantment = Enchantments.EFFICIENCY,

    /**
     * A Minecraft [Text] option, uses [TextSerializer] for custom serialization handling
     */
    @Serializable(with = TextSerializer::class)
    var text: Text = LiteralText("default"),

    @Serializable(with = ItemStackSerializer::class)
    var itemStack: ItemStack = ItemStack(item, 1),

    @Serializable(with = GameProfileSerializer::class)
    var gameProfile: GameProfile = GameProfile(null, "Player42"),
) : IConfigure {
    /**
     * Overriding [fileName] to save to a file named something other than the class' name
     */
    override val fileName: String
        get() = "TestConfig"
}
