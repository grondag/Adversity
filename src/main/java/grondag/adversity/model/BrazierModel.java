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
package grondag.adversity.model;

import java.util.List;
import java.util.function.Function;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;

import grondag.adversity.AdversityClient;

public class BrazierModel extends AlchemicalModel {
	public static final List<SpriteIdentifier> TEXTURES = AdversityClient.REGISTRAR.spriteIdList(SpriteAtlasTexture.BLOCK_ATLAS_TEX,
			"block/brazier_base",
			"block/brazier_frame",
			"block/brazier_inlay",
			"block/brazier_side",
			"block/basin_burning_top",
			"block/brazier_rim");

	protected static final int BASE = 0;
	protected static final int FRAME = 1;
	protected static final int INLAY = 2;
	protected static final int SIDE = 3;
	protected static final int BURNING = 4;
	protected static final int RIM = 5;

	public static final int ACTIVE_COLOR = 0xFFFF4040;

	protected BrazierModel(Sprite sprite, Function<SpriteIdentifier, Sprite> spriteMap, boolean isFrame) {
		super(sprite,spriteMap, TEXTURES, isFrame, ACTIVE_COLOR, PX9);
	}

	@Override
	protected void renderFluidOverlay(QuadEmitter qe, int level) {
		final float depth = bottomDepth + levelMultiplier * level;
		qe.material(matTranslucent)
		.square(Direction.UP, PX1, PX1, PX15, PX15, depth)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[BURNING], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();
	}

	@Override
	protected final void emitQuads(QuadEmitter qe, boolean lit) {
		emitFrameQuads(qe);
		emitInlayQuads(qe, lit);
	}

	private final void emitInlayQuads(QuadEmitter qe, boolean lit) {
		emitInlayQuadsInner(qe, PX15, lit);
		emitInlayQuadsInner(qe, 0, lit);
	}

	private final void emitInlayQuadsInner(QuadEmitter qe, float depth, boolean lit) {
		final RenderMaterial mat = lit ? matCutoutGlow : matCutout;

		for (final Direction face : SIDES) {
			qe.material(mat)
			.square(face, PX1, PX9, PX15, PX15, depth)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[INLAY], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();
		}
	}

	@Override
	protected final void emitFrameQuads(QuadEmitter qe) {
		//BASE BOTTOM
		qe.material(matSolid)
		.square(Direction.DOWN, 0, 0, 1, 1, 0)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[BASE], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		//BASE TOP
		qe.material(matSolid)
		.square(Direction.UP, 0, 0, 1, 1, PX14)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[BASE], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		// FLOOR BOTTOM
		qe.material(matSolid)
		.square(Direction.DOWN, PX1, PX1, PX15, PX15, PX8)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[BASE], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		// FLOOR TOP
		qe.material(matSolid)
		.square(Direction.UP, 0, 0, 1, 1, PX7)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[BASE], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		// RIM TOP
		qe.material(matCutout)
		.square(Direction.UP, 0, 0, 1, 1, 0)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[RIM], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		final int sideSprite = isFrame ? FRAME : SIDE;

		for (final Direction face : SIDES) {
			// TOP OUTER SIDES
			qe.material(matCutout)
			.square(face, 0, PX8, 1, 1, 0)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[sideSprite], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();

			// TOP INNER SIDES
			qe.material(matCutout)
			.square(face, PX1, PX9, PX15, 1, PX15)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[sideSprite], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();

			// SUPPORT SIDES
			qe.material(matCutout)
			.square(face, PX6, PX2, PX12, PX8, PX6)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[sideSprite], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();

			// BASE SIDES
			qe.material(matCutout)
			.square(face, 0, 0, 1, PX2, 0)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[sideSprite], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();
		}
	}

	public static BrazierModel create(Function<SpriteIdentifier, Sprite> spriteMap) {
		return new BrazierModel(spriteMap.apply(TEXTURES.get(FRAME)), spriteMap, false);
	}

	public static BrazierModel createFrame(Function<SpriteIdentifier, Sprite> spriteMap) {
		return new BrazierModel(spriteMap.apply(TEXTURES.get(FRAME)), spriteMap, true);
	}
}
