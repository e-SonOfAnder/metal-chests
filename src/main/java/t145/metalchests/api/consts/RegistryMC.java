/*******************************************************************************
 * Copyright 2018-2019 T145
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package t145.metalchests.api.consts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import t145.metalchests.api.objs.BlocksMC;
import t145.metalchests.api.objs.ItemsMC;

public class RegistryMC {

	public static final String NAME = "MetalChests";
	public static final String ID = "metalchests";
	public static final Logger LOG = LogManager.getLogger(ID);
	public static final ResourceLocation RECIPE_GROUP = new ResourceLocation(ID);

	public static final ResourceLocation[] METAL_CHEST_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/copper.png"),
			getResource("textures/entity/chest/iron.png"),
			getResource("textures/entity/chest/silver.png"),
			getResource("textures/entity/chest/gold.png"),
			getResource("textures/entity/chest/diamond.png"),
			getResource("textures/entity/chest/obsidian.png")
	};

	public static final ResourceLocation[] HOLLOW_METAL_CHEST_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/copper_h.png"),
			getResource("textures/entity/chest/iron_h.png"),
			getResource("textures/entity/chest/silver_h.png"),
			getResource("textures/entity/chest/gold_h.png"),
			getResource("textures/entity/chest/diamond_h.png"),
			getResource("textures/entity/chest/obsidian_h.png")
	};

	public static final ResourceLocation[] METAL_HUNGRY_CHEST_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/hungry/copper.png"),
			getResource("textures/entity/chest/hungry/iron.png"),
			getResource("textures/entity/chest/hungry/silver.png"),
			getResource("textures/entity/chest/hungry/gold.png"),
			getResource("textures/entity/chest/hungry/diamond.png"),
			getResource("textures/entity/chest/hungry/obsidian.png")
	};

	public static final ResourceLocation[] SORTING_OVERLAY_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/overlay/sorting_copper.png"),
			getResource("textures/entity/chest/overlay/sorting_iron.png"),
			getResource("textures/entity/chest/overlay/sorting_silver.png"),
			getResource("textures/entity/chest/overlay/sorting_gold.png"),
			getResource("textures/entity/chest/overlay/sorting_diamond.png"),
			getResource("textures/entity/chest/overlay/sorting_obsidian.png")
	};

	public static final ResourceLocation[] SORTING_HUNGRY_OVERLAY_MODELS = new ResourceLocation[] {
			getResource("textures/entity/chest/hungry/overlay/sorting_copper.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_iron.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_silver.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_gold.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_diamond.png"),
			getResource("textures/entity/chest/hungry/overlay/sorting_obsidian.png")
	};

	public static final ResourceLocation OVERLAY_ENCHANT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	public static final ResourceLocation OVERLAY_TRAP = getResource("textures/entity/chest/overlay/trap.png");
	public static final ResourceLocation OVERLAY_TRAP_DOUBLE = getResource("textures/entity/chest/overlay/trap_double.png");

	public static final String ID_HOLOINVENTORY = "holoinventory";
	public static final String IFACE_NAMED_ITEM_HANDLER = "net.dries007.holoInventory.api.INamedItemHandler";

	public static final String ID_QUARK = "quark";
	public static final String IFACE_CHEST_BUTTON_CALLBACK = "vazkii.quark.api.IChestButtonCallback";
	public static final String IFACE_DROPOFF_MANAGER = "vazkii.quark.api.IDropoffManager";
	public static final String IFACE_SEARCH_BAR = "vazkii.quark.api.IItemSearchBar";

	public static final String ID_RAILCRAFT = "railcraft";
	public static final String IFACE_FLUID_CART = "mods.railcraft.api.carts.IFluidCart";
	public static final String IFACE_ITEM_CART = "mods.railcraft.api.carts.IItemCart";

	public static final String ID_THERMALEXPANSION = "thermalexpansion";
	public static final String IFACE_ENCHANTABLE_ITEM = "cofh.core.item.IEnchantableItem";

	public static final String ID_CHESTTRANSPORTER = "chesttransporter";
	public static final String ID_THAUMCRAFT = "thaumcraft";

	public static final String ID_RR2 = "refinedrelocation";
	public static final String IFACE_NAME_TAGGABLE = "net.blay09.mods.refinedrelocation.api.INameTaggable";

	public static final String KEY_METAL_CHEST = "metal_chest";
	public static final String KEY_METAL_HUNGRY_CHEST = "metal_hungry_chest";
	public static final String KEY_METAL_SORTING_CHEST = "metal_sorting_chest";
	public static final String KEY_METAL_SORTING_HUNGRY_CHEST = "metal_sorting_hungry_chest";

	public static final ResourceLocation RESOURCE_METAL_CHEST = getResource(KEY_METAL_CHEST);
	public static final ResourceLocation RESOURCE_METAL_HUNGRY_CHEST = getResource(KEY_METAL_HUNGRY_CHEST);
	public static final ResourceLocation RESOURCE_METAL_SORTING_CHEST = getResource(KEY_METAL_SORTING_CHEST);
	public static final ResourceLocation RESOURCE_METAL_SORTING_HUNGRY_CHEST = getResource(KEY_METAL_SORTING_HUNGRY_CHEST);

	public static final String KEY_CHEST_UPGRADE = "chest_upgrade";
	public static final String KEY_HUNGRY_CHEST_UPGRADE = "hungry_chest_upgrade";

	public static final ResourceLocation RESOURCE_CHEST_UPGRADE = getResource(KEY_CHEST_UPGRADE);
	public static final ResourceLocation RESOURCE_HUNGRY_CHEST_UPGRADE = getResource(KEY_HUNGRY_CHEST_UPGRADE);

	private RegistryMC() {}

	public static ResourceLocation getResource(String path) {
		return new ResourceLocation(ID, path);
	}

	public static final CreativeTabs TAB = new CreativeTabs(ID) {

		@Override
		public boolean hasSearchBar() {
			return true;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public ItemStack createIcon() {
			return new ItemStack(BlocksMC.METAL_CHEST, 1, 1);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> items) {
			BlocksMC.METAL_CHEST.getSubBlocks(this, items);

			if (BlocksMC.METAL_SORTING_CHEST != null) {
				BlocksMC.METAL_SORTING_CHEST.getSubBlocks(this, items);
			}

			ItemsMC.CHEST_UPGRADE.getSubItems(this, items);

			if (BlocksMC.METAL_HUNGRY_CHEST != null) {
				BlocksMC.METAL_HUNGRY_CHEST.getSubBlocks(this, items);

				if (BlocksMC.METAL_SORTING_HUNGRY_CHEST != null) {
					BlocksMC.METAL_SORTING_HUNGRY_CHEST.getSubBlocks(this, items);
				}

				ItemsMC.HUNGRY_CHEST_UPGRADE.getSubItems(this, items);
			}
		}
	}.setBackgroundImageName("item_search.png");
}