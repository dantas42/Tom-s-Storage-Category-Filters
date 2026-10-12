package com.tiago.tomsstoragecategories.enchanting;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import dev.shadowsoffire.apothic_enchanting.table.EnchantingStatRegistry;
import dev.shadowsoffire.apothic_enchanting.table.EnchantingStatRegistry.BlockStats;

/**
 * Reads the enchanting-table stats of Apothic Enchanting. The stats are data-driven ({@code enchanting_stats} data
 * registry): each entry lists blocks and the Eterna, Quanta and Arcana they provide.
 * Only load this class when Apothic Enchanting is installed (see {@link EnchantingSupport}).
 */
final class EnchantingComponents {

	private EnchantingComponents() {}

	static boolean isEnchantingSource(ItemStack stack) {
		if (!(stack.getItem() instanceof BlockItem blockItem)) {
			return false;
		}
		Block block = blockItem.getBlock();
		for (BlockStats entry : EnchantingStatRegistry.INSTANCE.getValues()) {
			if (entry.blocks.contains(block) && providesStats(entry)) {
				return true;
			}
		}
		return false;
	}

	private static boolean providesStats(BlockStats entry) {
		return entry.stats.eterna() > 0 || entry.stats.quanta() > 0 || entry.stats.arcana() > 0;
	}
}
