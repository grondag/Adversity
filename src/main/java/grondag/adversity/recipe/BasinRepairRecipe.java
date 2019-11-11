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
package grondag.adversity.recipe;

import grondag.adversity.registry.AdversityBlocks;
import grondag.adversity.registry.AdversityRecipes;
import grondag.fermion.recipe.AbstractSimpleRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class BasinRepairRecipe extends AbstractSimpleRecipe {
	public BasinRepairRecipe(Identifier id, String group, Ingredient ingredient, int cost, ItemStack result) {
		super(id, group, ingredient, cost, result);
	}

	@Override
	public RecipeType<?> getType() {
		return AdversityRecipes.BASIN_REPAIR_RECIPE_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AdversityRecipes.BASIN_REPAIR_RECIPE_SERIALIZER;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AdversityBlocks.BASIN_BLOCK);
	}
}
