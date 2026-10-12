package com.tiago.tomsstoragecategories.enchanting;

import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

/**
 * Entry point of the optional Apothic Enchanting integration (the enchanting-table stats of Apotheosis).
 * <p>
 * This class does not reference any Apothic Enchanting class. The stats registry is only used by
 * {@link EnchantingComponents}, which is loaded only when Apothic Enchanting is installed.
 */
public final class EnchantingSupport {

	public static final String MOD_ID = "apothic_enchanting";

	private EnchantingSupport() {}

	public static boolean isInstalled() {
		return ModList.get().isLoaded(MOD_ID);
	}

	/** True if the stack is a block item whose block gives Quanta, Arcana or Eterna to an enchanting table. */
	public static boolean isEnchantingSource(ItemStack stack) {
		if (!isInstalled()) {
			return false;
		}
		return EnchantingComponents.isEnchantingSource(stack);
	}
}
