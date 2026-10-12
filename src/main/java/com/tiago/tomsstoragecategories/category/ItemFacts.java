package com.tiago.tomsstoragecategories.category;

import com.tiago.tomsstoragecategories.apotheosis.ApotheosisData;

/**
 * Classification facts about one item stack, computed from Minecraft, Curios, Apothic Enchanting and Apotheosis data.
 * <p>
 * This record is intentionally free of Minecraft classes so that the category rules can be unit tested.
 * See {@link ItemCategory} for how these facts are used.
 *
 * @param weapon          the item is a weapon (sword, axe, ranged weapon, trident, mace or an item dealing attack damage)
 * @param armor           the item is an armor piece or a shield
 * @param tool            the item is a tool (digging tool, hoe, shears, fishing rod, flint and steel, ...)
 * @param food            the item has a food component (edible or drinkable food)
 * @param block           the item places a block in the world
 * @param materialTag     the item has one of the common material tags ({@code c:ingots}, {@code c:gems}, ...)
 * @param ore             the item has an ore tag ({@code c:ores}) or a raw ore tag ({@code c:raw_materials})
 * @param wood            the item has a wood tag ({@code minecraft:logs}, {@code minecraft:planks})
 * @param accessory       Curios accepts the item in at least one slot
 * @param enchantingSource the item is a block that gives Quanta, Arcana or Eterna to the enchanting table
 * @param apotheosis      Apotheosis data (affixes, sockets, installed gems, gem item)
 */
public record ItemFacts(
		boolean weapon,
		boolean armor,
		boolean tool,
		boolean food,
		boolean block,
		boolean materialTag,
		boolean ore,
		boolean wood,
		boolean accessory,
		boolean enchantingSource,
		ApotheosisData apotheosis) {

	public static final ItemFacts NONE = new ItemFacts(false, false, false, false, false, false, false, false, false, false, ApotheosisData.NONE);

	public ItemFacts(boolean weapon, boolean armor, boolean tool, boolean food, boolean block, boolean materialTag, ApotheosisData apotheosis) {
		this(weapon, armor, tool, food, block, materialTag, false, false, false, false, apotheosis);
	}

	/**
	 * True if the item belongs to none of the content categories. Ores, wood, accessories and enchanting sources
	 * count as categories, so their items do not also fall into Materials unless they have a material tag.
	 */
	public boolean isUncategorized() {
		return !weapon && !armor && !tool && !food && !block && !ore && !wood && !accessory && !enchantingSource;
	}
}
