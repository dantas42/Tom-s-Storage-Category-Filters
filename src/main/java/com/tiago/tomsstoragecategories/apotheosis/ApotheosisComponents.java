package com.tiago.tomsstoragecategories.apotheosis;

import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.affix.ItemAffixes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * Reads the Apotheosis 8.9.0 data components (namespace {@code apotheosis}) defined in
 * {@code dev.shadowsoffire.apotheosis.Apoth.Components}:
 * <ul>
 * <li>{@code apotheosis:affixes} - {@link ItemAffixes}, affix id to level.</li>
 * <li>{@code apotheosis:sockets} - Integer, number of sockets (0 to 16).</li>
 * <li>{@code apotheosis:socketed_gems} - {@link ItemContainerContents}, one slot per socket.</li>
 * <li>{@code apotheosis:gem} - the gem of a loose gem item (set by {@code GemItem.setGem}).</li>
 * </ul>
 * Only load this class when Apotheosis is installed (see {@link ApotheosisSupport}).
 */
final class ApotheosisComponents {

	private ApotheosisComponents() {}

	static ApotheosisData read(ItemStack stack) {
		ItemAffixes affixes = stack.get(Apoth.Components.AFFIXES);
		boolean hasAffixes = affixes != null && !affixes.isEmpty();

		boolean hasSockets = stack.getOrDefault(Apoth.Components.SOCKETS, 0) > 0;

		ItemContainerContents gems = stack.get(Apoth.Components.SOCKETED_GEMS);
		boolean hasGems = gems != null && gems.nonEmptyStream().findAny().isPresent();

		boolean gemItem = stack.has(Apoth.Components.GEM);

		return new ApotheosisData(hasAffixes, hasSockets, hasGems, gemItem);
	}
}
