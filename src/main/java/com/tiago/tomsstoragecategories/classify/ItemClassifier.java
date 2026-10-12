package com.tiago.tomsstoragecategories.classify;

import java.util.List;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;

import com.tiago.tomsstoragecategories.apotheosis.ApotheosisSupport;
import com.tiago.tomsstoragecategories.category.ItemFacts;
import com.tiago.tomsstoragecategories.curios.CuriosSupport;
import com.tiago.tomsstoragecategories.enchanting.EnchantingSupport;

/**
 * Computes {@link ItemFacts} from the data of an item stack.
 * <p>
 * Nothing here is based on item names or on the registry namespace. Every rule uses item tags, item classes or
 * data components that vanilla (or Apotheosis) defines for the item. The result is not cached, so changes to the
 * components of a stack (for example new affixes or gems) are always reflected.
 */
public final class ItemClassifier {

	private static final TagKey<Item> SWORDS = itemTag("minecraft", "swords");
	private static final TagKey<Item> AXES = itemTag("minecraft", "axes");
	private static final TagKey<Item> PICKAXES = itemTag("minecraft", "pickaxes");
	private static final TagKey<Item> SHOVELS = itemTag("minecraft", "shovels");
	private static final TagKey<Item> HOES = itemTag("minecraft", "hoes");
	private static final TagKey<Item> HEAD_ARMOR = itemTag("minecraft", "head_armor");
	private static final TagKey<Item> CHEST_ARMOR = itemTag("minecraft", "chest_armor");
	private static final TagKey<Item> LEG_ARMOR = itemTag("minecraft", "leg_armor");
	private static final TagKey<Item> FOOT_ARMOR = itemTag("minecraft", "foot_armor");

	private static final List<TagKey<Item>> ORE_TAGS = List.of(
			itemTag("c", "ores"),
			itemTag("c", "raw_materials"));

	private static final List<TagKey<Item>> WOOD_TAGS = List.of(
			itemTag("minecraft", "logs"),
			itemTag("minecraft", "planks"));

	private static final List<TagKey<Item>> MATERIAL_TAGS = List.of(
			itemTag("c", "ingots"),
			itemTag("c", "nuggets"),
			itemTag("c", "gems"),
			itemTag("c", "dusts"),
			itemTag("c", "raw_materials"),
			itemTag("c", "plates"),
			itemTag("c", "rods"));

	private ItemClassifier() {}

	public static ItemFacts classify(ItemStack stack) {
		if (stack.isEmpty()) {
			return ItemFacts.NONE;
		}

		Item item = stack.getItem();
		boolean block = item instanceof BlockItem;

		boolean armor = item instanceof ArmorItem
				|| item == Items.SHIELD
				|| stack.is(HEAD_ARMOR) || stack.is(CHEST_ARMOR) || stack.is(LEG_ARMOR) || stack.is(FOOT_ARMOR);

		boolean sword = item instanceof SwordItem || stack.is(SWORDS);
		boolean axe = item instanceof AxeItem || stack.is(AXES);

		// Swords also carry the TOOL component in vanilla, so they are excluded from the tool rule explicitly.
		boolean tool = !armor && !sword && !block && (
				item instanceof DiggerItem
				|| item instanceof HoeItem
				|| axe
				|| stack.is(PICKAXES) || stack.is(SHOVELS) || stack.is(HOES)
				|| stack.has(DataComponents.TOOL)
				|| item == Items.SHEARS
				|| item == Items.FISHING_ROD
				|| item == Items.FLINT_AND_STEEL);

		boolean weapon = !armor && !block && (
				sword
				|| axe
				|| item instanceof ProjectileWeaponItem
				|| item instanceof TridentItem
				|| item instanceof MaceItem
				// Items that are not tools and deal attack damage (modded melee and ranged weapons).
				|| (!tool && dealsAttackDamage(stack)));

		boolean food = stack.has(DataComponents.FOOD);

		boolean materialTag = hasAnyTag(stack, MATERIAL_TAGS);
		boolean ore = hasAnyTag(stack, ORE_TAGS);
		boolean wood = hasAnyTag(stack, WOOD_TAGS);

		return new ItemFacts(
				weapon, armor, tool, food, block, materialTag,
				ore, wood,
				CuriosSupport.isAccessory(stack),
				EnchantingSupport.isEnchantingSource(stack),
				ApotheosisSupport.read(stack));
	}

	private static boolean hasAnyTag(ItemStack stack, List<TagKey<Item>> tags) {
		for (TagKey<Item> tag : tags) {
			if (stack.is(tag)) {
				return true;
			}
		}
		return false;
	}

	private static boolean dealsAttackDamage(ItemStack stack) {
		for (var entry : stack.getAttributeModifiers().modifiers()) {
			if (entry.attribute().value() == Attributes.ATTACK_DAMAGE.value() && entry.modifier().amount() > 0) {
				return true;
			}
		}
		return false;
	}

	private static TagKey<Item> itemTag(String namespace, String path) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
	}
}
