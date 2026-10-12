package com.tiago.tomsstoragecategories.apotheosis;

import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

/**
 * Entry point of the optional Apotheosis integration.
 * <p>
 * This class does not reference any Apotheosis class. All Apotheosis access is delegated to
 * {@link ApotheosisComponents}, which is only loaded after {@link #isInstalled()} returned true.
 * Without Apotheosis the addon keeps working and the Apotheosis category is hidden.
 */
public final class ApotheosisSupport {

	public static final String MOD_ID = "apotheosis";

	private ApotheosisSupport() {}

	public static boolean isInstalled() {
		return ModList.get().isLoaded(MOD_ID);
	}

	/**
	 * Reads the Apotheosis data components of the stack directly (no cache, no events).
	 *
	 * @return the Apotheosis data, or {@link ApotheosisData#NONE} when Apotheosis is not installed
	 */
	public static ApotheosisData read(ItemStack stack) {
		if (!isInstalled()) {
			return ApotheosisData.NONE;
		}
		return ApotheosisComponents.read(stack);
	}
}
