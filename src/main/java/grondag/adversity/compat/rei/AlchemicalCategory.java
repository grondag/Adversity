/*******************************************************************************
 * Copyright (C) 2019 grondag
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package grondag.adversity.compat.rei;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.widget.EntryWidget;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.plugin.DefaultPlugin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;

public abstract class AlchemicalCategory<T extends AlchemicalDisplay<?>> implements RecipeCategory<T> {
	@Override
	public List<Widget> setupDisplay(Supplier<T> recipeDisplaySupplier, Rectangle bounds) {
		final Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
		final List<Widget> widgets = new LinkedList<>(Arrays.asList(new RecipeBaseWidget(bounds) {
			@Override
			public void render(int mouseX, int mouseY, float delta) {
				super.render(mouseX, mouseY, delta);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				DiffuseLighting.disable();
				MinecraftClient.getInstance().getTextureManager().bindTexture(DefaultPlugin.getDisplayTexture());
				blit(startPoint.x, startPoint.y, 0, 221, 82, 26);
			}
		}));
		widgets.add(EntryWidget.create(startPoint.x + 4, startPoint.y + 5).entries(recipeDisplaySupplier.get().getInputEntries().get(0)).noBackground());
		widgets.add(EntryWidget.create(startPoint.x + 61, startPoint.y + 5).entry(recipeDisplaySupplier.get().getOutputEntries().get(0)).noBackground());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 36;
	}
}
