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
package grondag.adversity.block.player;

import static grondag.adversity.block.player.AlchemicalBlockEntity.UNITS_PER_BUCKET;
import static grondag.adversity.block.player.AlchemicalBlockEntity.UNITS_PER_INGOT;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import grondag.adversity.block.player.AlchemicalBlockEntity.Mode;
import grondag.adversity.registry.AdversityItems;
import grondag.adversity.registry.AdversityRecipes;
import grondag.fermion.recipe.AbstractSimpleRecipe;

public class BasinBlock extends AlchemicalBlock {
	public static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	public static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);

	public BasinBlock(Block.Settings settings) {
		super(settings, AdversityRecipes.BASIN_RECIPE_TYPE);
		setDefaultState(stateManager.getDefaultState().with(LIT, false));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
		return OUTLINE_SHAPE;
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return RAY_TRACE_SHAPE;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1) {
		return new BasinBlockEntity();
	}

	@Override
	int fuelValue(Item item) {
		return item == AdversityItems.WARDING_ESSENCE_ITEM ? UNITS_PER_INGOT
				: item == AdversityItems.WARDING_ESSENCE_BLOCK_ITEM ? UNITS_PER_BUCKET : 0;
	}

	@Override
	protected boolean handleActiveRecipe(BlockState blockState, World world, BlockPos pos, AlchemicalBlockEntity blockEntity, PlayerEntity player, Hand hand, ItemStack stack, int currentUnits) {
		if (stack.isEmpty()) {
			return false;
		}

		final AbstractSimpleRecipe recipe = AdversityRecipes.HELPER.get(AdversityRecipes.BASIN_REPAIR_RECIPE_TYPE, stack);

		if (recipe == null) {
			return false;
		}

		final float basecost = recipe.cost;
		final int maxCost = Math.round(basecost * stack.getDamage() / stack.getMaxDamage());
		final int cost = Math.min(currentUnits, maxCost);

		if (cost == 0) {
			return true;
		}

		if (!world.isClient) {
			final int newUnits = currentUnits - cost;

			final int repair = Math.max(1, stack.getDamage() * cost / maxCost);
			stack.setDamage(cost == maxCost ? 0 : stack.getDamage() - repair);
			player.setStackInHand(hand, stack);
			blockEntity.setState(newUnits == 0 ? Mode.IDLE : Mode.ACTIVE, newUnits);
			blockEntity.sendCraftingParticles();

			if (newUnits == 0) {
				world.setBlockState(pos, blockState.with(LIT, false), 3);
			}
		}

		return true;
	}
}
