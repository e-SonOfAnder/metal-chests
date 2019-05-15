/*******************************************************************************
 * Copyright 2019 T145
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
package T145.metalchests.core;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.ItemsMC;
import T145.metalchests.api.chests.UpgradeRegistry;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.client.gui.GuiHandler;
import T145.metalchests.config.ModConfig;
import T145.metalchests.entities.EntityBoatMetalChest;
import T145.metalchests.entities.EntityMinecartMetalChest;
import T145.metalchests.network.PacketHandler;
import T145.metalchests.tiles.TileMetalChest;
import T145.metalchests.tiles.TileMetalSortingChest;
import net.blay09.mods.refinedrelocation.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.blocks.BlocksTC;
import vazkii.quark.decoration.feature.VariedChests;

@Mod(modid = RegistryMC.ID, name = RegistryMC.NAME, version = MetalChests.VERSION, updateJSON = MetalChests.UPDATE_JSON, dependencies = "after:thaumcraft;after:refinedrelocation;after:quark")
public class MetalChests {

	static final String VERSION = "@VERSION@";
	static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metalchests/master/update.json";

	public static final Logger LOG = LogManager.getLogger(RegistryMC.ID);

	public static final CreativeTabs TAB = new CreativeTabs(RegistryMC.ID) {

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(BlocksMC.METAL_CHEST, 1, 1);
		}

		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(NonNullList<ItemStack> items) {
			BlocksMC.METAL_CHEST.getSubBlocks(this, items);
			ItemsMC.CHEST_UPGRADE.getSubItems(this, items);

			if (BlocksMC.METAL_HUNGRY_CHEST != null) {
				BlocksMC.METAL_HUNGRY_CHEST.getSubBlocks(this, items);
			}

			if (BlocksMC.METAL_SORTING_CHEST != null) {
				BlocksMC.METAL_SORTING_CHEST.getSubBlocks(this, items);
			}

			if (BlocksMC.METAL_HUNGRY_SORTING_CHEST != null) {
				BlocksMC.METAL_HUNGRY_SORTING_CHEST.getSubBlocks(this, items);
			}

			if (ModConfig.GENERAL.enableMinecarts) {
				ItemsMC.MINECART_METAL_CHEST.getSubItems(this, items);
			}
		}
	}.setBackgroundImageName("item_search.png");

	@Instance(RegistryMC.ID)
	public static MetalChests instance;

	public static final DataSerializer<ChestType> CHEST_TYPE = new DataSerializer<ChestType>() {

		@Override
		public void write(PacketBuffer buf, ChestType value) {
			buf.writeEnumValue(value);
		}

		@Override
		public ChestType read(PacketBuffer buf) throws IOException {
			return buf.readEnumValue(ChestType.class);
		}

		@Override
		public DataParameter<ChestType> createKey(int id) {
			return new DataParameter<ChestType>(id, this);
		}

		@Override
		public ChestType copyValue(ChestType value) {
			return value;
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModMetadata meta = event.getModMetadata();
		meta.authorList.add("T145");
		meta.autogenerated = false;
		meta.credits = "The fans!";
		meta.description = "The better alternative to IronChests";
		meta.logoFile = "logo.png";
		meta.modId = RegistryMC.ID;
		meta.name = RegistryMC.NAME;
		meta.url = "https://github.com/T145/metalchests";
		meta.useDependencyInformation = false;
		meta.version = VERSION;
		DataSerializers.registerSerializer(CHEST_TYPE);
	}

	private void registerFixes(DataFixer fixer, Class tileClass) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(tileClass, new String[] { "Items" }));
	}

	private void registerEntityFixes(DataFixer fixer, Class entityClass) {
		fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(entityClass, new String[] { "Items" }));
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MetalChests.instance, new GuiHandler());

		DataFixer fixer = FMLCommonHandler.instance().getDataFixer();

		registerFixes(fixer, TileMetalChest.class);
		EntityMinecartContainer.addDataFixers(fixer, EntityMinecartMetalChest.class);
		registerEntityFixes(fixer, EntityBoatMetalChest.class);

		if (ModConfig.hasRefinedRelocation()) {
			registerFixes(fixer, TileMetalSortingChest.class);
		}

		if (ModConfig.hasThaumcraft() && ModConfig.hasRefinedRelocation()) {
			registerFixes(fixer, TileMetalSortingChest.class);
		}

		PacketHandler.registerMessages();
	}

	@Optional.Method(modid = RegistryMC.ID_QUARK)
	@EventHandler
	public void quark$postInit(FMLPostInitializationEvent event) {
		UpgradeRegistry.registerChest(VariedChests.custom_chest_trap, BlocksMC.METAL_CHEST);
	}

	@Optional.Method(modid = RegistryMC.ID_RR2)
	@EventHandler
	public void refinedrelocation$postInit(FMLPostInitializationEvent event) {
		UpgradeRegistry.registerChest(ModBlocks.sortingChest, BlocksMC.METAL_SORTING_CHEST);
	}

	@Optional.Method(modid = RegistryMC.ID_THAUMCRAFT)
	@EventHandler
	public void thaumcraft$postInit(FMLPostInitializationEvent event) {
		UpgradeRegistry.registerChest(BlocksTC.hungryChest, BlocksMC.METAL_HUNGRY_CHEST);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for (ItemStack stack : OreDictionary.getOres("chestWood")) {
			UpgradeRegistry.registerChest(Block.getBlockFromItem(stack.getItem()), BlocksMC.METAL_CHEST);
		}

		UpgradeRegistry.registerChest(Blocks.TRAPPED_CHEST, BlocksMC.METAL_CHEST);
	}
}
