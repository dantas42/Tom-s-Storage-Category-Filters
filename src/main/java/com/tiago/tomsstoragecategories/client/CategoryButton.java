package com.tiago.tomsstoragecategories.client;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.tom.storagemod.screen.widget.IconButton;

import com.tiago.tomsstoragecategories.category.ItemCategory;

/**
 * A 16x16 terminal button with the item icon of a category. It reuses the sprites of the original
 * Tom's Storage {@link IconButton}, so it looks like the other terminal buttons.
 * <p>
 * The button is not added to the screen's widget list. It is drawn and clicked by {@link CategoryFilterBar},
 * which is called from the screen mixin.
 */
public class CategoryButton extends IconButton {
	private static final ResourceLocation UNUSED_ICON = ResourceLocation.fromNamespaceAndPath("toms_storage", "icons/search_mode");
	private static final int SELECTED_OUTLINE = 0xFFFFD35A;

	private final ItemCategory category;
	private final ItemStack icon;
	private final Supplier<ItemCategory> selectedCategory;

	CategoryButton(int x, int y, ItemCategory category, Supplier<ItemCategory> selectedCategory) {
		super(x, y, Component.translatable(nameKey(category)), UNUSED_ICON, b -> {});
		this.category = category;
		this.icon = CategoryIcons.iconFor(category);
		this.selectedCategory = selectedCategory;
	}

	public ItemCategory getCategory() {
		return category;
	}

	public List<Component> tooltipLines() {
		return List.of(
				Component.translatable(nameKey(category)),
				Component.translatable(nameKey(category) + ".desc").withStyle(ChatFormatting.GRAY));
	}

	@Override
	protected void drawIcon(GuiGraphics st, int mouseX, int mouseY, float partialTicks) {
		int x = getX();
		int y = getY();
		if (selectedCategory.get() == category) {
			st.renderOutline(x - 1, y - 1, 18, 18, SELECTED_OUTLINE);
		}
		st.renderItem(icon, x, y);
	}

	private static String nameKey(ItemCategory category) {
		return "tooltip.toms_storage_category_filters.category." + category.id();
	}
}
