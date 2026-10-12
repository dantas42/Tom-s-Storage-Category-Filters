package com.tiago.tomsstoragecategories.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.tiago.tomsstoragecategories.apotheosis.ApotheosisData;

class ItemCategoryTest {

	private static final ApotheosisData NO_APOTHEOSIS = ApotheosisData.NONE;

	private static ItemFacts facts(boolean weapon, boolean armor, boolean tool, boolean food, boolean block, boolean materialTag, ApotheosisData apoth) {
		return new ItemFacts(weapon, armor, tool, food, block, materialTag, apoth);
	}

	@Test
	void allMatchesEverything() {
		assertTrue(ItemCategory.ALL.matches(ItemFacts.NONE));
		assertTrue(ItemCategory.ALL.matches(facts(true, false, false, false, false, false, NO_APOTHEOSIS)));
	}

	@Test
	void contentCategoriesUseTheirFlag() {
		ItemFacts sword = facts(true, false, false, false, false, false, NO_APOTHEOSIS);
		assertTrue(ItemCategory.WEAPONS.matches(sword));
		assertFalse(ItemCategory.ARMOR.matches(sword));
		assertFalse(ItemCategory.TOOLS.matches(sword));

		ItemFacts chestplate = facts(false, true, false, false, false, false, NO_APOTHEOSIS);
		assertTrue(ItemCategory.ARMOR.matches(chestplate));
		assertFalse(ItemCategory.WEAPONS.matches(chestplate));

		ItemFacts pickaxe = facts(false, false, true, false, false, false, NO_APOTHEOSIS);
		assertTrue(ItemCategory.TOOLS.matches(pickaxe));

		ItemFacts apple = facts(false, false, false, true, false, false, NO_APOTHEOSIS);
		assertTrue(ItemCategory.FOOD.matches(apple));
		assertFalse(ItemCategory.BLOCKS.matches(apple));

		ItemFacts stone = facts(false, false, false, false, true, false, NO_APOTHEOSIS);
		assertTrue(ItemCategory.BLOCKS.matches(stone));
	}

	@Test
	void axeBelongsToWeaponsAndToolsAtTheSameTime() {
		ItemFacts axe = facts(true, false, true, false, false, false, NO_APOTHEOSIS);
		assertTrue(ItemCategory.WEAPONS.matches(axe));
		assertTrue(ItemCategory.TOOLS.matches(axe));
		assertFalse(ItemCategory.MATERIALS.matches(axe), "a weapon or tool is never a generic material");
	}

	@Test
	void materialsIsCatchAllForUncategorizedItems() {
		assertTrue(ItemCategory.MATERIALS.matches(ItemFacts.NONE), "stick-like items fall back to materials");
		assertFalse(ItemCategory.MATERIALS.matches(facts(true, false, false, false, false, false, NO_APOTHEOSIS)));
	}

	@Test
	void materialTagsMatchNonBlockItemsOnly() {
		assertTrue(ItemCategory.MATERIALS.matches(facts(false, false, false, false, false, true, NO_APOTHEOSIS)));
		assertFalse(ItemCategory.MATERIALS.matches(facts(false, false, false, false, true, true, NO_APOTHEOSIS)),
				"storage blocks made from ingots belong to blocks, not materials");
	}

	@Test
	void apotheosisRequiresAffixesSocketsOrGems() {
		assertFalse(ItemCategory.APOTHEOSIS.matches(facts(true, false, false, false, false, false, NO_APOTHEOSIS)),
				"a plain diamond sword is not an Apotheosis item");
		assertTrue(ItemCategory.APOTHEOSIS.matches(facts(true, false, false, false, false, false, new ApotheosisData(true, false, false))));
		assertTrue(ItemCategory.APOTHEOSIS.matches(facts(true, false, false, false, false, false, new ApotheosisData(false, true, false))));
		assertTrue(ItemCategory.APOTHEOSIS.matches(facts(true, false, false, false, false, false, new ApotheosisData(false, false, true))));
	}

	@Test
	void apotheosisDataRelevance() {
		assertFalse(ApotheosisData.NONE.isRelevant());
		assertTrue(new ApotheosisData(true, true, true).isRelevant());
	}

	@Test
	void applyingAndCombiningIsAnIntersection() {
		ItemFacts diamondSword = facts(true, false, false, false, false, false, new ApotheosisData(true, true, false));
		ItemFacts diamondHelmet = facts(false, true, false, false, false, false, NO_APOTHEOSIS);
		assertTrue(ItemCategory.WEAPONS.matches(diamondSword) && ItemCategory.APOTHEOSIS.matches(diamondSword));
		assertFalse(ItemCategory.WEAPONS.matches(diamondHelmet));
	}

	@Test
	void apotheosisSubFiltersMatchOnlyTheirOwnData() {
		ItemFacts affixed = facts(true, false, false, false, false, false, new ApotheosisData(true, false, false));
		ItemFacts socketed = facts(false, true, false, false, false, false, new ApotheosisData(false, true, false));
		ItemFacts gemmed = facts(false, true, false, false, false, false, new ApotheosisData(false, true, true));
		ItemFacts plain = facts(true, false, false, false, false, false, NO_APOTHEOSIS);

		assertTrue(ItemCategory.APOTHEOSIS_AFFIXES.matches(affixed));
		assertFalse(ItemCategory.APOTHEOSIS_AFFIXES.matches(socketed));
		assertFalse(ItemCategory.APOTHEOSIS_AFFIXES.matches(plain));

		assertTrue(ItemCategory.APOTHEOSIS_SOCKETS.matches(socketed));
		assertTrue(ItemCategory.APOTHEOSIS_SOCKETS.matches(gemmed));
		assertFalse(ItemCategory.APOTHEOSIS_SOCKETS.matches(affixed));

		assertFalse(ItemCategory.APOTHEOSIS_GEMS.matches(gemmed), "equipment with an installed gem is not a gem item");
		assertFalse(ItemCategory.APOTHEOSIS_GEMS.matches(plain));
	}

	@Test
	void socketedGemsSubFilterMatchesEquipmentWithAGemInstalled() {
		ItemFacts gemmed = facts(false, true, false, false, false, false, new ApotheosisData(false, true, true));
		ItemFacts emptySocket = facts(false, true, false, false, false, false, new ApotheosisData(false, true, false));

		assertTrue(ItemCategory.APOTHEOSIS_SOCKETED_GEMS.matches(gemmed));
		assertFalse(ItemCategory.APOTHEOSIS_SOCKETED_GEMS.matches(emptySocket));
		assertTrue(ItemCategory.APOTHEOSIS_SOCKETS.matches(emptySocket));
		assertTrue(ItemCategory.APOTHEOSIS_SOCKETS.matches(gemmed));
	}

	@Test
	void gemsSubFilterMatchesLooseGemItems() {
		ItemFacts looseGem = facts(false, false, false, false, false, false, new ApotheosisData(false, false, false, true));
		ItemFacts plainDiamond = facts(false, false, false, false, false, true, NO_APOTHEOSIS);

		assertTrue(ItemCategory.APOTHEOSIS_GEMS.matches(looseGem));
		assertFalse(ItemCategory.APOTHEOSIS_GEMS.matches(plainDiamond));
		assertFalse(ItemCategory.APOTHEOSIS.matches(looseGem), "loose gems are reached through the Gems sub-filter only");
	}

	@Test
	void subFiltersAreMarkedAndRequireTheirMod() {
		assertTrue(ItemCategory.APOTHEOSIS_AFFIXES.isSubFilter());
		assertEquals("apotheosis", ItemCategory.APOTHEOSIS_SOCKETS.requiredMod());
		assertEquals("apothic_enchanting", ItemCategory.APOTHEOSIS_ENCHANTING.requiredMod());
		assertFalse(ItemCategory.APOTHEOSIS.isSubFilter());
	}

	@Test
	void oresAndWoodUseTheirTagFlags() {
		ItemFacts ore = new ItemFacts(false, false, false, false, true, true, true, false, false, false, NO_APOTHEOSIS);
		ItemFacts log = new ItemFacts(false, false, false, false, true, false, false, true, false, false, NO_APOTHEOSIS);

		assertTrue(ItemCategory.ORES.matches(ore));
		assertFalse(ItemCategory.WOOD.matches(ore));
		assertTrue(ItemCategory.WOOD.matches(log));
		assertTrue(ItemCategory.BLOCKS.matches(log), "a log is still a block");
		assertFalse(ItemCategory.MATERIALS.matches(log), "logs belong to Wood, not to the Materials catch-all");
	}

	@Test
	void ironOreItemsAreAlsoMaterials() {
		ItemFacts rawIron = new ItemFacts(false, false, false, false, false, true, true, false, false, false, NO_APOTHEOSIS);
		assertTrue(ItemCategory.MATERIALS.matches(rawIron));
		assertTrue(ItemCategory.ORES.matches(rawIron));
	}

	@Test
	void accessoriesUseTheCuriosFlag() {
		ItemFacts ring = new ItemFacts(false, false, false, false, false, false, false, false, true, false, NO_APOTHEOSIS);
		assertTrue(ItemCategory.ACCESSORIES.matches(ring));
		assertFalse(ItemCategory.ARMOR.matches(ring));
		assertEquals("curios", ItemCategory.ACCESSORIES.requiredMod());
	}

	@Test
	void enchantingSourcesAreASubFilterOfApotheosis() {
		ItemFacts bookshelf = new ItemFacts(false, false, false, false, true, false, false, false, false, true, NO_APOTHEOSIS);
		ItemFacts diamond = new ItemFacts(false, false, false, false, false, true, NO_APOTHEOSIS);

		assertTrue(ItemCategory.APOTHEOSIS_ENCHANTING.matches(bookshelf));
		assertFalse(ItemCategory.APOTHEOSIS_ENCHANTING.matches(diamond));
		assertTrue(ItemCategory.APOTHEOSIS_ENCHANTING.isSubFilter());
	}

	@Test
	void categoryIdsAreStable() {
		assertEquals("apotheosis", ItemCategory.APOTHEOSIS.id());
		assertEquals("apotheosis", ItemCategory.APOTHEOSIS.requiredMod());
		assertEquals(null, ItemCategory.WEAPONS.requiredMod());
	}
}
