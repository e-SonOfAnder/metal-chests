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
package T145.metalchests.containers;

import T145.metalchests.api.IInventoryHandler;
import T145.metalchests.api.SupportedInterfaces;
import T145.metalchests.api.SupportedMods;
import T145.metalchests.blocks.BlockMetalChest.ChestType;
import invtweaks.api.container.ChestContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.quark.api.IChestButtonCallback;

@ChestContainer(isLargeChest = true)
@Optional.Interface(modid = SupportedMods.QUARK, iface = SupportedInterfaces.CHEST_BUTTON_CALLBACK, striprefs = true)
public class ContainerMetalChest extends Container implements IChestButtonCallback {

	private final IInventoryHandler handler;
	private final ChestType type;
	private final ChestType.GUI gui;

	public ContainerMetalChest(IInventoryHandler handler, EntityPlayer player, ChestType type) {
		this.handler = handler;
		this.type = type;
		this.gui = ChestType.GUI.byType(type);
		handler.openInventory(player);
		layoutInventory();
		layoutPlayerInventory(player);
	}

	public ChestType.GUI getGuiType() {
		return gui;
	}

	protected void layoutInventory() {
		for (int chestRow = 0; chestRow < type.getRowCount(); ++chestRow) {
			for (int chestCol = 0; chestCol < type.getRowLength(); ++chestCol) {
				addSlotToContainer(new SlotItemHandler(handler.getInventory(), chestCol + chestRow * type.getRowLength(), 12 + chestCol * 18, 8 + chestRow * 18));
			}
		}
	}

	protected void layoutPlayerInventory(EntityPlayer player) {
		int leftCol = (gui.getSizeX() - 162) / 2 + 1;
		int ySize = gui.getSizeY();

		for (int playerInvRow = 0; playerInvRow < 3; ++playerInvRow) {
			for (int playerInvCol = 0; playerInvCol < 9; ++playerInvCol) {
				addSlotToContainer(new Slot(player.inventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18 - 10));
			}
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
			addSlotToContainer(new Slot(player.inventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 24));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		handler.closeInventory(player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return handler.isUsableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if (!this.mergeItemStack(slotStack, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(slotStack, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (slotStack.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, slotStack);
		}

		return stack;
	}

	@ChestContainer.RowSizeCallback
	public int getNumColumns() {
		return type.getRowLength();
	}

	@Optional.Method(modid = SupportedMods.QUARK)
	@Override
	public boolean onAddChestButton(GuiButton button, int buttonType) {
		return true;
	}
}
