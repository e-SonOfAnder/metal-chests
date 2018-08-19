/*******************************************************************************
 * Copyright 2018 T145
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
package T145.metalchests.items;

import java.util.Map;

import javax.annotation.Nullable;

import T145.metalchests.api.chests.IFacing;
import T145.metalchests.api.chests.IInventoryHandler;
import T145.metalchests.api.chests.IUpgradeableChest;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.config.ModConfig;
import T145.metalchests.core.MetalChests;
import T145.metalchests.lib.items.ItemMod;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemChestUpgrade extends ItemMod {

	public enum ChestUpgrade implements IStringSerializable {

		WOOD_COPPER(ChestType.COPPER),
		WOOD_IRON(ChestType.IRON),
		WOOD_SILVER(ChestType.SILVER),
		WOOD_GOLD(ChestType.GOLD),
		WOOD_DIAMOND(ChestType.DIAMOND),
		WOOD_OBSIDIAN(ChestType.OBSIDIAN),
		COPPER_IRON(ChestType.COPPER, ChestType.IRON),
		COPPER_SILVER(ChestType.COPPER, ChestType.SILVER),
		COPPER_GOLD(ChestType.COPPER, ChestType.GOLD),
		COPPER_DIAMOND(ChestType.COPPER, ChestType.DIAMOND),
		COPPER_OBSIDIAN(ChestType.COPPER, ChestType.OBSIDIAN),
		IRON_SILVER(ChestType.IRON, ChestType.SILVER),
		IRON_GOLD(ChestType.IRON, ChestType.GOLD),
		IRON_DIAMOND(ChestType.IRON, ChestType.DIAMOND),
		IRON_OBSIDIAN(ChestType.IRON, ChestType.OBSIDIAN),
		SILVER_GOLD(ChestType.SILVER, ChestType.GOLD),
		SILVER_DIAMOND(ChestType.SILVER, ChestType.DIAMOND),
		SILVER_OBSIDIAN(ChestType.SILVER, ChestType.OBSIDIAN),
		GOLD_DIAMOND(ChestType.GOLD, ChestType.DIAMOND),
		GOLD_OBSIDIAN(ChestType.GOLD, ChestType.OBSIDIAN),
		DIAMOND_OBSIDIAN(ChestType.DIAMOND, ChestType.OBSIDIAN);

		@Nullable
		private ChestType base;
		private ChestType upgrade;

		ChestUpgrade(ChestType base, ChestType upgrade) {
			this.base = base;
			this.upgrade = upgrade;
		}

		ChestUpgrade(ChestType upgrade) {
			this(null, upgrade);
		}

		public ChestType getBase() {
			return base;
		}

		public ChestType getUpgrade() {
			return upgrade;
		}

		public static ChestUpgrade byMetadata(int meta) {
			return values()[meta];
		}

		public boolean isRegistered() {
			return base == null ? upgrade.isRegistered() : base.isRegistered() && upgrade.isRegistered();
		}

		public ChestUpgrade getPriorUpgrade() {
			return values()[ordinal() > 0 ? ordinal() - 1 : 0];
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}
	}

	public static final String NAME = "chest_upgrade";

	private final Map<Block, TileEntity> defaultChests;

	public ItemChestUpgrade(Map<Block, TileEntity> defaultChests) {
		super(NAME, ChestUpgrade.values());
		this.defaultChests = defaultChests;
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote || !player.isSneaking()) {
			return EnumActionResult.PASS;
		}

		TileEntity te = world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		ChestUpgrade upgrade = ChestUpgrade.byMetadata(stack.getItemDamage());

		if (te instanceof IUpgradeableChest) {
			IUpgradeableChest chest = (IUpgradeableChest) te;

			if (chest.getChestType() == upgrade.getBase()) {
				chest.setChestType(upgrade.getUpgrade());
			} else {
				return EnumActionResult.FAIL;
			}
		} else if (defaultChests.containsKey(te.getBlockType()) && defaultChests.get(te.getBlockType()) instanceof IUpgradeableChest) {
			EnumFacing front = getFrontFromProperties(world, pos);

			te.updateContainingBlockInfo();

			if (te instanceof TileEntityChest) {
				((TileEntityChest) te).checkForAdjacentChests();
			}

			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
			world.setTileEntity(pos, ((IUpgradeableChest) te).createTileEntity());

			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof IUpgradeableChest) {
				IUpgradeableChest upgradedChest = (IUpgradeableChest) tile;
				upgradedChest.setChestType(upgrade.getUpgrade());
				IBlockState state = upgradedChest.createBlockState();
				world.setBlockState(pos, state, 3);
				world.notifyBlockUpdate(pos, state, state, 3);
			}

			if (tile instanceof IFacing) {
				((IFacing) tile).setFront(front);
			}

			if (tile instanceof IInventoryHandler) {
				((IInventoryHandler) tile).setInventory(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, front));
			}
		} else {
			return EnumActionResult.PASS;
		}

		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 0.8F);

		return EnumActionResult.SUCCESS;
	}

	@Nullable
	private EnumFacing getFrontFromProperties(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);

		for (IProperty<?> prop : state.getProperties().keySet()) {
			if ((prop.getName().equals("facing") || prop.getName().equals("rotation")) && prop.getValueClass() == EnumFacing.class) {
				IProperty<EnumFacing> facingProperty = (IProperty<EnumFacing>) prop;
				return state.getValue(facingProperty);
			}
		}

		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == MetalChests.TAB) {
			if (hasSubtypes) {
				for (ChestUpgrade upgrade : ChestUpgrade.values()) {
					if (!ModConfig.GENERAL.showEverthingInCreative || upgrade.isRegistered()) {
						items.add(new ItemStack(this, 1, upgrade.ordinal()));
					}
				}
			} else {
				items.add(new ItemStack(this));
			}
		}
	}
}
