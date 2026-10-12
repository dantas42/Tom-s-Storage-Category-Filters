package com.tiago.tomsstoragecategories.curios;

import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

/**
 * Entry point of the optional Curios integration.
 * <p>
 * This class does not reference any Curios class. The Curios API is only used by {@link CuriosComponents}, which is
 * loaded only when Curios is installed. Without Curios the Accessories category is hidden.
 */
public final class CuriosSupport {

	public static final String MOD_ID = "curios";

	private CuriosSupport() {}

	public static boolean isInstalled() {
		return ModList.get().isLoaded(MOD_ID);
	}

	/** True if Curios accepts the stack in at least one accessory slot. */
	public static boolean isAccessory(ItemStack stack) {
		if (!isInstalled()) {
			return false;
		}
		return CuriosComponents.isAccessory(stack);
	}
}
