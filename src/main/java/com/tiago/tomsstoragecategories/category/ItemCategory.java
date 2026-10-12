package com.tiago.tomsstoragecategories.category;

/**
 * Filter categories of the terminal.
 * <p>
 * An item may belong to several categories (multi-membership). The rules are documented in
 * {@code docs/CATEGORIAS.md}. Summary:
 * <ul>
 * <li>{@link #ALL}: every item.</li>
 * <li>{@link #WEAPONS}, {@link #ARMOR}, {@link #TOOLS}, {@link #FOOD}, {@link #BLOCKS}: the matching {@link ItemFacts} flag.</li>
 * <li>{@link #MATERIALS}: material tags, plus every item that is not in any other category (catch-all).</li>
 * <li>{@link #ORES}, {@link #WOOD}: ore and wood item tags.</li>
 * <li>{@link #ACCESSORIES}: items that Curios accepts in a slot. Needs Curios.</li>
 * <li>{@link #APOTHEOSIS}: items with Apotheosis affixes, sockets or installed gems.</li>
 * <li>{@link #APOTHEOSIS_AFFIXES}, {@link #APOTHEOSIS_SOCKETS}, {@link #APOTHEOSIS_GEMS}: Apotheosis sub-filters.</li>
 * <li>{@link #APOTHEOSIS_ENCHANTING}: blocks that give Quanta, Arcana or Eterna to the enchanting table. Needs Apothic Enchanting.</li>
 * </ul>
 */
public enum ItemCategory {
	ALL("all", null, false),
	WEAPONS("weapons", null, false),
	ARMOR("armor", null, false),
	TOOLS("tools", null, false),
	FOOD("food", null, false),
	BLOCKS("blocks", null, false),
	MATERIALS("materials", null, false),
	ORES("ores", null, false),
	WOOD("wood", null, false),
	ACCESSORIES("accessories", "curios", false),
	APOTHEOSIS("apotheosis", "apotheosis", false),
	APOTHEOSIS_AFFIXES("apotheosis_affixes", "apotheosis", true),
	APOTHEOSIS_SOCKETS("apotheosis_sockets", "apotheosis", true),
	APOTHEOSIS_GEMS("apotheosis_gems", "apotheosis", true),
	APOTHEOSIS_SOCKETED_GEMS("apotheosis_socketed_gems", "apotheosis", true),
	APOTHEOSIS_ENCHANTING("apotheosis_enchanting", "apothic_enchanting", true);

	private final String id;
	private final String requiredMod;
	private final boolean subFilter;

	ItemCategory(String id, String requiredMod, boolean subFilter) {
		this.id = id;
		this.requiredMod = requiredMod;
		this.subFilter = subFilter;
	}

	public String id() {
		return id;
	}

	/** The mod id that must be installed for this category to be shown, or null if it is always shown. */
	public String requiredMod() {
		return requiredMod;
	}

	/** True if this category is shown in the secondary column next to the Apotheosis button. */
	public boolean isSubFilter() {
		return subFilter;
	}

	public boolean matches(ItemFacts facts) {
		return switch (this) {
		case ALL -> true;
		case WEAPONS -> facts.weapon();
		case ARMOR -> facts.armor();
		case TOOLS -> facts.tool();
		case FOOD -> facts.food();
		case BLOCKS -> facts.block();
		case MATERIALS -> (facts.materialTag() && !facts.block()) || facts.isUncategorized();
		case ORES -> facts.ore();
		case WOOD -> facts.wood();
		case ACCESSORIES -> facts.accessory();
		case APOTHEOSIS -> facts.apotheosis().isRelevant();
		case APOTHEOSIS_AFFIXES -> facts.apotheosis().affixes();
		case APOTHEOSIS_SOCKETS -> facts.apotheosis().sockets();
		case APOTHEOSIS_GEMS -> facts.apotheosis().gemItem();
		case APOTHEOSIS_SOCKETED_GEMS -> facts.apotheosis().gems();
		case APOTHEOSIS_ENCHANTING -> facts.enchantingSource();
		};
	}
}
