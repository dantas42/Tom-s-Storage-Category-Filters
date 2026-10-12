package com.tiago.tomsstoragecategories.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.fml.ModList;

import com.tom.storagemod.inventory.TerminalItemStack;

import com.tiago.tomsstoragecategories.category.ItemCategory;
import com.tiago.tomsstoragecategories.classify.ItemClassifier;

/**
 * Holds the selected category of one terminal screen and its buttons. It draws the buttons, handles clicks and
 * applies the category to the client-side list of items. It never touches the storage or the player inventory.
 * <p>
 * Layout: the main categories form a column. The sub-filters (Apotheosis affixes, sockets, gems and enchanting) form a
 * second column, starting at the top, so the bar fits inside the screen at small GUI scales.
 */
public final class CategoryFilterBar {
	/** Vertical distance between two buttons (16 px button + 2 px gap). */
	public static final int BUTTON_STEP = 18;

	private final List<CategoryButton> mainButtons = new ArrayList<>();
	private final List<CategoryButton> subButtons = new ArrayList<>();
	private ItemCategory selected = ItemCategory.ALL;

	public CategoryFilterBar() {
		Arrays.stream(ItemCategory.values())
				.filter(CategoryFilterBar::isAvailable)
				.forEach(c -> (c.isSubFilter() ? subButtons : mainButtons).add(new CategoryButton(0, 0, c, this::getSelected)));
	}

	private static boolean isAvailable(ItemCategory category) {
		return category.requiredMod() == null || ModList.get().isLoaded(category.requiredMod());
	}

	public ItemCategory getSelected() {
		return selected;
	}

	public void select(ItemCategory category) {
		this.selected = category;
	}

	public boolean isFiltering() {
		return selected != ItemCategory.ALL;
	}

	/** Draws the buttons starting at (x, y) and the tooltip of the hovered button. */
	public void render(GuiGraphics st, int mouseX, int mouseY, float partialTicks, int x, int y, Font font) {
		for (int i = 0; i < mainButtons.size(); i++) {
			CategoryButton button = mainButtons.get(i);
			button.setX(x);
			button.setY(y + i * BUTTON_STEP);
			button.render(st, mouseX, mouseY, partialTicks);
		}

		for (int i = 0; i < subButtons.size(); i++) {
			CategoryButton button = subButtons.get(i);
			button.setX(x + BUTTON_STEP);
			button.setY(y + i * BUTTON_STEP);
			button.render(st, mouseX, mouseY, partialTicks);
		}

		for (CategoryButton button : allButtons()) {
			if (button.isMouseOver(mouseX, mouseY)) {
				st.renderComponentTooltip(font, button.tooltipLines(), mouseX, mouseY);
				break;
			}
		}
	}

	/** Returns the category of the button under the mouse, or null if no button is under it. */
	public ItemCategory categoryAt(double mouseX, double mouseY) {
		for (CategoryButton button : allButtons()) {
			if (button.isMouseOver(mouseX, mouseY)) {
				return button.getCategory();
			}
		}
		return null;
	}

	/**
	 * Removes from the displayed list every item that does not belong to the selected category.
	 * This runs after the text search, so the result is the intersection of both filters.
	 */
	public void filter(List<TerminalItemStack> items) {
		if (!isFiltering()) {
			return;
		}
		items.removeIf(item -> item == null
				|| item.getStack() == null
				|| !selected.matches(ItemClassifier.classify(item.getStack())));
	}

	private List<CategoryButton> allButtons() {
		List<CategoryButton> all = new ArrayList<>(mainButtons);
		all.addAll(subButtons);
		return all;
	}

}
