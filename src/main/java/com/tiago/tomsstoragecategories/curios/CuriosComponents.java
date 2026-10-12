package com.tiago.tomsstoragecategories.curios;

import net.minecraft.world.item.ItemStack;

import top.theillusivec4.curios.api.CuriosApi;

/**
 * Uses the Curios API to check whether an item is a curio (an accessory that fits in a Curios slot).
 * Only load this class when Curios is installed (see {@link CuriosSupport}).
 */
final class CuriosComponents {

	private CuriosComponents() {}

	static boolean isAccessory(ItemStack stack) {
		return CuriosApi.getCurio(stack).isPresent();
	}
}
