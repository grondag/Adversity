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

import java.util.Random;

import grondag.adversity.block.treeheart.DoomHeartBlockEntity;
import grondag.adversity.block.treeheart.DoomTreeTracker;
import grondag.adversity.block.treeheart.TrunkDesigner;
import grondag.adversity.registry.AdversityBlockStates;
import grondag.adversity.registry.AdversityBlocks;
import grondag.adversity.registry.AdversityParticles;
import grondag.adversity.registry.AdversitySounds;
import io.netty.util.internal.ThreadLocalRandom;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelProperties;

public class DoomSaplingBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	public enum Mode {
		NEW,

		/** validating build area */
		CHECKING,

		/** validation failed */
		STALLED,

		/** consuming starter blocks and summoning thunder storm */
		STARTING,

		/** do light show, waiting for storm */
		GLOOMING,

		/** summing lightning, waiting for it to strike */
		LIGHTNING,

		/** placing the heart */
		DONE,

		/** should be gone */
		REMOVED
	}

	protected static Mode[] MODES = Mode.values();

	protected static final String TAG_MORPH = "morph";

	protected final BlockPos.Mutable mPos = new BlockPos.Mutable();
	protected Mode mode = Mode.NEW;
	protected int workIndex = 0;
	protected LongArrayList logs = null;

	public final Random renderRand = new Random();
	public long renderSeed = renderRand.nextLong();

	public DoomSaplingBlockEntity(final BlockEntityType<?> entityType) {
		super(entityType);
	}

	public DoomSaplingBlockEntity() {
		this(AdversityBlocks.DOOM_SAPLING);
	}

	@Override
	public void tick() {
		if (world == null) return;

		if (world.isClient) {
			tickClient();
		} else {
			tickServer();
		}
	}

	protected void tickClient() {
		final Random rand = ThreadLocalRandom.current();

		switch (mode) {
		case NEW:
		case CHECKING:
		case STARTING:
			if (workIndex++ == 0) {
				final double x0 = pos.getX() - 1.5;
				final double y = pos.getY() + 0.5;
				final double z0 = pos.getZ() - 1.5;

				for (int x = 0; x < 5; x++) {
					for (int z = 0; z < 5; z++) {
						if(x == 2 && z == 2) continue;

						world.addParticle(ParticleTypes.SMOKE, x + x0, y, z + z0, 0.0D, 0.0D, 0.0D);
					}
				}
			}


			if (rand.nextInt(4) == 0) {
				final double x = pos.getX() + rand.nextDouble();
				final double y = pos.getY() + rand.nextDouble();
				final double z = pos.getZ() + rand.nextDouble();
				world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
			}
			break;

		case GLOOMING:
		case LIGHTNING:
			doLightShow(rand);

			if (rand.nextInt(10) == 0) {
				renderSeed = rand.nextLong();
			}

			if (rand.nextInt(TOTAL_TICKS) < workIndex++) {
				doLightShow(rand);
			}

			break;

		default:
			break;

		}
	}

	private void doLightShow(final Random rand) {
		final double azimuth = Math.PI * 2 * rand.nextDouble();
		final double altitude = Math.PI * 0.5 * rand.nextDouble();

		final double dx = Math.cos(azimuth);
		final double dy = Math.sin(altitude);
		final double dz = Math.sin(azimuth);

		final double len = 4 + rand.nextDouble() * 2;//  + (rand.nextDouble() + 1) * factor;
		final double x = pos.getX() + 0.5 + dx * len;
		final double y = pos.getY() + 0.5 + dy * len;
		final double z = pos.getZ() + 0.5 + dz * len;

		final double v = -0.05; //0.05 - rand.nextDouble() * 0.05 * factor;

		world.addParticle(AdversityParticles.SUMMONING, x, y, z, dx * v, dy * v, dz * v);
	}

	protected void tickServer() {
		final Mode startMode = mode;

		switch (mode) {
		case NEW:
			mode = doNew();
			break;

		case STARTING:
			mode = doStarting();
			break;

		case CHECKING:
			mode = doChecking();
			break;

		case GLOOMING:
			mode = doGlooming();
			break;

		case LIGHTNING:
			mode = doLightning();
			break;

		case DONE:
			mode = doDone();
			break;

		case STALLED:
		default:
			break;

		}

		if (mode != startMode) {
			markDirty();
			sync();
		}
	}

	protected Mode doNew() {
		workIndex = 0;
		if (DoomTreeTracker.canGrow(world, pos)) {
			return Mode.CHECKING;
		} else {
			return Mode.STALLED;
		}
	}

	protected TrunkDesigner trunk;

	protected Mode doChecking() {
		if (trunk ==  null) {
			trunk = new TrunkDesigner(pos, world);
		}

		if (trunk.designTrunk()) {
			logs = trunk.blocks();
			trunk = null;

			if (logs == null || logs.isEmpty()){
				return Mode.STALLED;
			} else {
				workIndex = 0;
				return Mode.STARTING;
			}
		} else {
			return Mode.CHECKING;
		}
	}

	protected Mode doStarting() {
		if (!(ForebodingShrubBlock.hasSurroundingBlocks(world, pos, mPos) && ForebodingShrubBlock.isMatrixInAndAround(world, pos, mPos))) {
			return Mode.STALLED;
		}

		consumeBlocks();
		world.playSound(null, pos, AdversitySounds.DOOM_START, SoundCategory.HOSTILE, 1, 1.2f);

		workIndex = 0;
		return Mode.GLOOMING;
	}

	protected void consumeBlocks() {
		final int x0 = pos.getX() - 2;
		final int y = pos.getY() - 1;
		final int z0 = pos.getZ() - 2;
		final World world = this.world;

		for (int x = 0; x < 5; x++) {
			for (int z = 0; z < 5; z++) {
				world.setBlockState(mPos.set(x0 + x, y, z0 + z), AdversityBlockStates.DOOMED_EARTH_STATE);
			}
		}
	}

	protected void setWeather() {
		final LevelProperties props = world.getLevelProperties();
		props.setClearWeatherTime(0);
		props.setRainTime(Math.max(props.getRainTime(), 600));
		props.setThunderTime(Math.max(props.getThunderTime(), 400));
		props.setRaining(true);
		props.setThundering(true);
	}

	private static final int RAIN_START_TICK = 60;
	private static final int TOTAL_TICKS = 200;

	protected Mode doGlooming() {
		final int w = workIndex++;

		if (w == RAIN_START_TICK || (w > RAIN_START_TICK && (w & 15) == 0)) {
			setWeather();
		}

		if (w >= TOTAL_TICKS) {
			workIndex = 0;
			return Mode.LIGHTNING;
		}

		return Mode.GLOOMING;
	}

	private Mode doLightning() {
		final int w = workIndex++;

		if (w == 0) {
			final LightningEntity entity = new LightningEntity(world, pos.getX(), pos.getY(), pos.getZ(), true);
			((ServerWorld) world).addLightning(entity);
		}

		return w < 10 ? Mode.LIGHTNING : Mode.DONE;
	}

	protected Mode doDone() {
		world.playSound(null, pos, AdversitySounds.DOOM_SUMMON, SoundCategory.HOSTILE, 1, 2);

		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
		world.setBlockState(pos, AdversityBlocks.DOOM_HEART_BLOCK.getDefaultState(), 3);

		final BlockEntity be = world.getBlockEntity(pos);
		final int y = pos.getY();

		if (be != null && be instanceof DoomHeartBlockEntity) {
			final DoomHeartBlockEntity heart = (DoomHeartBlockEntity) be;
			logs.sort((l0, l1) -> Integer.compare(
				Math.abs(BlockPos.unpackLongY(l0) - y),
				Math.abs(BlockPos.unpackLongY(l1) - y)));
			heart.setTemplate(logs.toLongArray());
		}
		return Mode.REMOVED;
	}

	protected CompoundTag writeTag(final CompoundTag tag) {
		tag.putInt(TAG_MORPH, mode.ordinal());
		return tag;
	}

	protected void readTag(final CompoundTag tag) {
		mode = MODES[tag.getInt(TAG_MORPH)];
	}

	@Override
	public void fromTag(final CompoundTag tag) {
		super.fromTag(tag);
		readTag(tag);
	}

	@Override
	public CompoundTag toTag(final CompoundTag tag) {
		return writeTag(super.toTag(tag));
	}

	@Override
	public void fromClientTag(final CompoundTag tag) {
		readTag(tag);
		workIndex = 0;
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		if (tag == null) {
			tag = new CompoundTag();
		}
		return writeTag(tag);
	}


}
