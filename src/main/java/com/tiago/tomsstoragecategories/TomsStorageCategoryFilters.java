package com.tiago.tomsstoragecategories;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

/**
 * Entry point of the addon. It is client side only: the terminal GUI only exists on the client.
 * The integration itself is done by {@link com.tiago.tomsstoragecategories.mixin.AbstractStorageTerminalScreenMixin}.
 */
@Mod(value = TomsStorageCategoryFilters.MOD_ID, dist = Dist.CLIENT)
public final class TomsStorageCategoryFilters {

	public static final String MOD_ID = "toms_storage_category_filters";

	public TomsStorageCategoryFilters() {}
}
