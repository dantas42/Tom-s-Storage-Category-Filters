package com.tiago.tomsstoragecategories.mixin;

import net.minecraft.client.gui.GuiGraphics;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.tom.storagemod.screen.AbstractStorageTerminalScreen;

import com.tiago.tomsstoragecategories.category.ItemCategory;
import com.tiago.tomsstoragecategories.client.CategoryFilterBar;

/**
 * Adds the category buttons to the Tom's Storage terminal screens (storage and crafting terminal).
 * <p>
 * Only members declared in {@link AbstractStorageTerminalScreen} itself are shadowed. Members inherited from the
 * Minecraft screen classes (for example {@code addRenderableWidget} or {@code leftPos}) are not shadowed, because
 * Mixin does not resolve them in this setup. The screen geometry is read through public methods instead.
 * <ul>
 * <li>{@code render()}: draws the buttons on the right side of the GUI.</li>
 * <li>{@code mouseClicked()}: selects the category of a clicked button.</li>
 * <li>{@code updateSearch()}: right before the client list is sorted, removes the items outside the selected category.
 * The text search has already filtered the list at this point, so both filters are combined (AND).</li>
 * </ul>
 * Only {@code itemListClientSorted}, a client-side list, is changed. Storage and inventories are never touched.
 */
@Mixin(AbstractStorageTerminalScreen.class)
public abstract class AbstractStorageTerminalScreenMixin {

	@Shadow
	private boolean refreshItemList;

	@Shadow
	protected abstract void updateSearch();

	@Unique
	private CategoryFilterBar tcfBar;

	@Inject(method = "render", at = @At("TAIL"))
	private void tcfRender(GuiGraphics st, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		AbstractStorageTerminalScreen<?> screen = (AbstractStorageTerminalScreen<?>) (Object) this;
		int x = screen.getGuiLeft() + screen.getXSize() + 4;
		int y = screen.getGuiTop() + 6;
		tcfCategoryBar().render(st, mouseX, mouseY, partialTicks, x, y, screen.getFont());
	}

	@Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
	private void tcfMouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir) {
		if (mouseButton != 0) {
			return;
		}
		ItemCategory category = tcfCategoryBar().categoryAt(mouseX, mouseY);
		if (category != null) {
			tcfCategoryBar().select(category);
			refreshItemList = true;
			updateSearch();
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "updateSearch", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V"))
	private void tcfFilterCategory(CallbackInfo ci) {
		if (tcfBar != null && tcfBar.isFiltering()) {
			tcfBar.filter(((AbstractStorageTerminalScreen<?>) (Object) this).getMenu().itemListClientSorted);
		}
	}

	@Unique
	private CategoryFilterBar tcfCategoryBar() {
		if (tcfBar == null) {
			tcfBar = new CategoryFilterBar();
		}
		return tcfBar;
	}
}
