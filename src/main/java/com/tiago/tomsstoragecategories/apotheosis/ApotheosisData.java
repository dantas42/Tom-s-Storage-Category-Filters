package com.tiago.tomsstoragecategories.apotheosis;

/**
 * Apotheosis data found on one item stack.
 *
 * @param affixes  true if the stack has a non-empty {@code apotheosis:affixes} component
 * @param sockets  true if the stack has at least one socket ({@code apotheosis:sockets} &gt; 0)
 * @param gems     true if at least one gem is installed in a socket ({@code apotheosis:socketed_gems})
 * @param gemItem  true if the stack is itself a gem item ({@code apotheosis:gem} component)
 */
public record ApotheosisData(boolean affixes, boolean sockets, boolean gems, boolean gemItem) {

	public static final ApotheosisData NONE = new ApotheosisData(false, false, false, false);

	public ApotheosisData(boolean affixes, boolean sockets, boolean gems) {
		this(affixes, sockets, gems, false);
	}

	/**
	 * True if the item should appear in the Apotheosis category: equipment with affixes, sockets or installed gems.
	 * Loose gem items are reached through their own sub-filter.
	 */
	public boolean isRelevant() {
		return affixes || sockets || gems;
	}
}
