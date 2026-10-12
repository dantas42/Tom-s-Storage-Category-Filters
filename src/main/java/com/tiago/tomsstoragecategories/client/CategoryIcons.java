package com.tiago.tomsstoragecategories.client;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.tiago.tomsstoragecategories.category.ItemCategory;

final class CategoryIcons {

	private CategoryIcons() {}

	static ItemStack iconFor(ItemCategory category) {
		return switch (category) {
		case ALL -> new ItemStack(Items.CHEST);
		case WEAPONS -> new ItemStack(Items.IRON_SWORD);
		case ARMOR -> new ItemStack(Items.IRON_CHESTPLATE);
		case TOOLS -> new ItemStack(Items.IRON_PICKAXE);
		case FOOD -> new ItemStack(Items.APPLE);
		case BLOCKS -> new ItemStack(Items.BRICKS);
		case MATERIALS -> new ItemStack(Items.IRON_INGOT);
		case APOTHEOSIS -> new ItemStack(Items.EMERALD);
		case APOTHEOSIS_AFFIXES -> new ItemStack(Items.ENCHANTED_BOOK);
		case APOTHEOSIS_SOCKETS -> new ItemStack(Items.SMITHING_TABLE);
		case APOTHEOSIS_GEMS -> new ItemStack(Items.AMETHYST_SHARD);
		case APOTHEOSIS_SOCKETED_GEMS -> new ItemStack(Items.DIAMOND_SWORD);
		case ORES -> new ItemStack(Items.RAW_IRON);
		case WOOD -> new ItemStack(Items.OAK_LOG);
		case ACCESSORIES -> new ItemStack(Items.TOTEM_OF_UNDYING);
		case APOTHEOSIS_ENCHANTING -> new ItemStack(Items.ENCHANTING_TABLE);
		};
	}
}
