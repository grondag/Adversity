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
package grondag.adversity.block.tree;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import grondag.adversity.block.treeheart.DoomTreeTracker;
import grondag.adversity.registry.AdversityBlocks;
import grondag.adversity.registry.AdversityItems;

public class ForebodingShrubBlock extends PlantBlock {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D);

	public ForebodingShrubBlock(final Block.Settings settings) {
		super(settings);
		setDefaultState(stateManager.getDefaultState());
	}

	@Override
	public VoxelShape getOutlineShape(final BlockState blockState, final BlockView blockView, final BlockPos blockPos, final EntityContext entityContext) {
		return SHAPE;
	}

	@Override
	public void onEntityCollision(final BlockState blockState, final World world, final BlockPos pos, final Entity entity) {
		if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX) {
			entity.slowMovement(blockState, new Vec3d(0.800000011920929D, 0.75D, 0.800000011920929D));

			if (!world.isClient && (entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ())) {
				final double dx = Math.abs(entity.getX() - entity.lastRenderX);
				final double dz = Math.abs(entity.getZ() - entity.lastRenderZ);
				if (dx >= 0.003000000026077032D || dz >= 0.003000000026077032D) {
					entity.damage(DamageSource.MAGIC, 1.0F);
				}
			}

		}
	}

	@Override
	protected boolean canPlantOnTop(final BlockState blockState, final BlockView blockView, final BlockPos pos) {
		if (super.canPlantOnTop(blockState, blockView, pos)) {
			return true;
		}

		final Block block = blockState.getBlock();
		return BlockTags.SAND.contains(block) || block == Blocks.TERRACOTTA
				|| block == Blocks.GRAVEL || block == Blocks.CLAY  || block == AdversityBlocks.GENERATIVE_MATRIX;
	}

	static long lastMessageTime = 0;

	@Override
	public ActionResult onUse(final BlockState blockState, final World world, final BlockPos blockPos, final PlayerEntity player, final Hand hand, final BlockHitResult blockhit) {
		if  (!world.isClient && player != null &&  player.getMainHandStack().getItem() ==  AdversityItems.ALCHEMICAL_ENGINE) {
			final BlockPos.Mutable mPos = new BlockPos.Mutable();

			if (!isMatrixInAndAround(world, blockPos, mPos)) {
				sendMessageNoSpam(player, "help.doomtree.missing_matrix");
				return ActionResult.SUCCESS;
			}

			if (!hasSurroundingBlocks(world, blockPos, mPos)) {
				sendMessageNoSpam(player, "help.doomtree.missing_blocks");
				return ActionResult.SUCCESS;
			}

			if (!DoomTreeTracker.canGrow(world, blockPos)) {
				sendMessageNoSpam(player, "help.doomtree.bad_position");
				return ActionResult.SUCCESS;
			}

			//			if (world.getBiome(blockPos).getPrecipitation() != Precipitation.RAIN) {
			//				sendMessageNoSpam(player, "help.doomtree.bad_biome");
			//				return true;
			//			}

			if (!world.isSkyVisible(blockPos)) {
				sendMessageNoSpam(player, "help.doomtree.no_sky");
				return ActionResult.SUCCESS;
			}

			world.setBlockState(blockPos, AdversityBlocks.DOOM_SAPLING_BLOCK.getDefaultState());
			player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
			return  ActionResult.SUCCESS;
		}

		return super.onUse(blockState, world, blockPos, player, hand, blockhit);
	}

	private static void sendMessageNoSpam(final PlayerEntity player, final String key) {
		final long newTime = player.world.getTime();

		if (Math.abs(lastMessageTime - newTime) >= 20) {
			player.sendMessage(new TranslatableText(key));
			lastMessageTime = newTime;
		}
	}

	public static boolean isMatrixInAndAround(final World world, final BlockPos pos, final BlockPos.Mutable mPos) {
		final int x = pos.getX();
		final int y = pos.getY() - 1;
		final int z = pos.getZ();

		return isMatrix(world, mPos.set(x, y, z))
				&& isMatrix(world, mPos.set(x - 1, y, z - 1))
				&& isMatrix(world, mPos.set(x - 1, y, z))
				&& isMatrix(world, mPos.set(x - 1, y, z + 1))
				&& isMatrix(world, mPos.set(x, y, z - 1))
				&& isMatrix(world, mPos.set(x, y, z + 1))
				&& isMatrix(world, mPos.set(x + 1, y, z - 1))
				&& isMatrix(world, mPos.set(x + 1, y, z))
				&& isMatrix(world, mPos.set(x + 1, y, z + 1));
	}

	private static boolean isMatrix(final World world, final BlockPos pos) {
		return world.getBlockState(pos) == AdversityBlocks.GENERATIVE_MATRIX.getDefaultState();
	}

	static final int PER_TARGET_COUNT = 8;
	static final int TARGET_SHIFT = 8;
	static final int COMBINED_TARGET_COUNT = (PER_TARGET_COUNT | (PER_TARGET_COUNT << TARGET_SHIFT));

	public static boolean hasSurroundingBlocks(final World world, final BlockPos pos, final BlockPos.Mutable mPos) {
		final int x = pos.getX();
		final int y = pos.getY() - 1;
		final int z = pos.getZ();

		int counts = 0;

		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z - 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z - 1), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z + 1), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z + 2), counts);

		counts = checkSurroundingBlocks(world, mPos.set(x - 1, y, z - 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x, y, z - 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 1, y, z - 2), counts);

		counts = checkSurroundingBlocks(world, mPos.set(x - 1, y, z + 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x, y, z + 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 1, y, z + 2), counts);

		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z - 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z - 1), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z + 1), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z + 2), counts);

		return counts == COMBINED_TARGET_COUNT;
	}

	private static int checkSurroundingBlocks(final World world, final BlockPos.Mutable mPos, int priorCounts) {
		final Block block = world.getBlockState(mPos).getBlock();

		if (block == Blocks.REDSTONE_BLOCK) {
			priorCounts += 1;
		} else if (block == Blocks.PRISMARINE || block == Blocks.NETHERRACK) {
			priorCounts += 1 << TARGET_SHIFT;
		}

		return priorCounts;
	}
}
