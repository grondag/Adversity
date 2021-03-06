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

import me.shedaniel.rei.api.EntryStack;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import grondag.adversity.registry.AdversityBlocks;

public class BasinCategory extends AlchemicalCategory<BasinDisplay> {
	@Override
	public Identifier getIdentifier() {
		return AdversityReiPlugin.BASIN;
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(AdversityBlocks.BASIN_BLOCK);
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("category.doomtree.basin");
	}
}
