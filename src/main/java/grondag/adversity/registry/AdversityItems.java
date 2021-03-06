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
package grondag.adversity.registry;

import static grondag.adversity.Adversity.REG;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.sound.SoundEvents;

import grondag.adversity.item.MilkPotion;
import grondag.adversity.item.NoteItem;
import grondag.adversity.item.SalvationPotion;
import grondag.adversity.item.WardingPotion;
import grondag.fermion.block.sign.OpenSignItem;
import grondag.fermion.registrar.SimpleArmorMaterial;
import grondag.fermion.registrar.SimpleToolMaterial;

public enum AdversityItems {
	;

	public static final BucketItem ICHOR_BUCKET = REG.item("ichor_bucket", new BucketItem(AdversityFluids.ICHOR, REG.itemSettings().recipeRemainder(Items.BUCKET).maxCount(1)));
	public static Item PLACED_DOOM_LOG_ITEM = Item.fromBlock(AdversityBlocks.PLACED_DOOM_LOG);
	public static Item DOOM_LOG_ITEM = Item.fromBlock(AdversityBlocks.DOOM_LOG);
	public static Item DOOM_LEAF_ITEM = Item.fromBlock(AdversityBlocks.DOOM_LEAF);
	public static Item WARDING_ESSENCE_BLOCK_ITEM = Item.fromBlock(AdversityBlocks.WARDING_ESSENCE_BLOCK);
	public static Item DOOMED_RESIDUE_ITEM = REG.item("doomed_residue");
	public static Item WARDING_ESSENCE_ITEM = REG.item("warding_essence");
	public static Item DOOM_FRAGMENT_ITEM = REG.item("doom_fragment");
	public static Item ALCHEMICAL_BASIN_FRAME = REG.item("alchemical_basin_frame");
	public static Item ALCHEMICAL_BRAZIER_FRAME = REG.item("alchemical_brazier_frame");
	public static Item WARDED_IRON_INGOT = REG.item("warded_iron_ingot");
	public static Item WARDED_IRON_NUGGET = REG.item("warded_iron_nugget");
	public static Item WARDED_DIAMOND = REG.item("warded_diamond");
	public static Item WARDED_STICK = REG.item("warded_stick");
	public static Item WARDED_BARREL_SHELL = REG.item("warded_barrel_shell");
	public static Item HEART_OF_DOOM = REG.item("heart_of_doom", new Item(REG.itemSettings().maxCount(1)));
	public static Item ALCHEMICAL_ENGINE = REG.item("alchemical_engine", new Item(REG.itemSettings().maxCount(1)));

	public static Item WARDING_POTION = REG.item("warding_potion", new WardingPotion(REG.itemSettings().maxCount(16)));
	public static Item MILK_POTION = REG.item("milk_potion", new MilkPotion(REG.itemSettings().maxCount(16)));
	public static Item SALVATION_POTION = REG.item("salvation_potion", new SalvationPotion(REG.itemSettings().maxCount(16)));

	public static Item MYSTERIOUS_NOTE = REG.item("mysterious_note", new NoteItem(REG.itemSettings().maxCount(1), "This am some msyterious shit.  Wooo!"));

	public static final ArmorMaterial WARDED_IRON_ARMOR_MATERIAL = SimpleArmorMaterial.of("warded_iron", 17, new int[]{2, 5, 6, 2}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F, WARDED_IRON_INGOT);
	public static final Item WARDED_IRON_HELMET =  REG.armorItem("warded_iron_helmet", WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.HEAD);
	public static final Item WARDED_IRON_CHESTPLATE =  REG.armorItem("warded_iron_chestplate", WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.CHEST);
	public static final Item WARDED_IRON_LEGGINGS =  REG.armorItem("warded_iron_leggings", WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.LEGS);
	public static final Item WARDED_IRON_BOOTS =  REG.armorItem("warded_iron_boots", WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.FEET);

	public static final ArmorMaterial ENCRUSTED_ARMOR_MATERIAL = SimpleArmorMaterial.of("encrusted", 37, new int[]{3, 6, 8, 3}, 11, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F, WARDED_DIAMOND);
	public static final Item ENCRUSTED_HELMET =  REG.armorItem("encrusted_helmet", ENCRUSTED_ARMOR_MATERIAL, EquipmentSlot.HEAD);
	public static final Item ENCRUSTED_CHESTPLATE =  REG.armorItem("encrusted_chestplate", ENCRUSTED_ARMOR_MATERIAL, EquipmentSlot.CHEST);
	public static final Item ENCRUSTED_LEGGINGS =  REG.armorItem("encrusted_leggings", ENCRUSTED_ARMOR_MATERIAL, EquipmentSlot.LEGS);
	public static final Item ENCRUSTED_BOOTS =  REG.armorItem("encrusted_boots", ENCRUSTED_ARMOR_MATERIAL, EquipmentSlot.FEET);

	public static final ToolMaterial WARDED_IRON_TOOL_MATERIAL = SimpleToolMaterial.of(2, 300, 6.6F, 2.2F, 15, WARDED_IRON_INGOT);
	public static final Item WARDED_IRON_HOE = REG.item("warded_iron_hoe", new HoeItem(WARDED_IRON_TOOL_MATERIAL, -1.0F, REG.itemSettings()){});
	public static final Item WARDED_IRON_SHOVEL = REG.item("warded_iron_shovel", new ShovelItem(WARDED_IRON_TOOL_MATERIAL, 1.5F, -3.0F, REG.itemSettings()){});
	public static final Item WARDED_IRON_PICKAXE = REG.item("warded_iron_pickaxe", new PickaxeItem(WARDED_IRON_TOOL_MATERIAL, 1, -2.8F, REG.itemSettings()){});
	public static final Item WARDED_IRON_AXE = REG.item("warded_iron_axe", new AxeItem(WARDED_IRON_TOOL_MATERIAL, 6.0F, -3.1F, REG.itemSettings()){});
	public static final Item WARDED_IRON_SWORD = REG.item("warded_iron_sword", new SwordItem(WARDED_IRON_TOOL_MATERIAL, 3, -2.4F, REG.itemSettings()){});

	public static final ToolMaterial ENCRUSTED_TOOL_MATERIAL = SimpleToolMaterial.of(3, 1800, 8.8F, 3.3F, 11, WARDED_DIAMOND);
	public static final Item ENCRUSTED_HOE = REG.item("encrusted_hoe", new HoeItem(ENCRUSTED_TOOL_MATERIAL, 0.0F, REG.itemSettings()){});
	public static final Item ENCRUSTED_SHOVEL = REG.item("encrusted_shovel", new ShovelItem(ENCRUSTED_TOOL_MATERIAL, 1.5F, -3.0F, REG.itemSettings()){});
	public static final Item ENCRUSTED_PICKAXE = REG.item("encrusted_pickaxe", new PickaxeItem(ENCRUSTED_TOOL_MATERIAL, 1, -2.8F, REG.itemSettings()){});
	public static final Item ENCRUSTED_AXE = REG.item("encrusted_axe", new AxeItem(ENCRUSTED_TOOL_MATERIAL, 5.0F, -3.0F, REG.itemSettings()){});
	public static final Item ENCRUSTED_SWORD = REG.item("encrusted_sword", new SwordItem(ENCRUSTED_TOOL_MATERIAL, 3, -2.4F, REG.itemSettings()){});

	public static final Item WARDED_TORCH = REG.item("warded_torch", new WallStandingBlockItem(AdversityBlocks.WARDED_TORCH, AdversityBlocks.WARDED_WALL_TORCH, REG.itemSettings()));
	public static final Item WARDED_WOOD_SIGN = REG.item("warded_wood_sign", new OpenSignItem(REG.itemSettings().maxCount(16), AdversityBlocks.WARDED_WOOD_SIGN, AdversityBlocks.WARDED_WOOD_WALL_SIGN));
}
