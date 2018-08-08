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
package T145.metalchests.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import T145.metalchests.core.proxies.IProxy;
import T145.metalchests.core.proxies.ServerProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MetalChests.MOD_ID, name = MetalChests.MOD_NAME, version = MetalChests.VERSION, updateJSON = MetalChests.UPDATE_JSON)
public class MetalChests implements IProxy {

	public static final String MOD_ID = "metalchests";
	public static final String MOD_NAME = "MetalChests";

	static final String VERSION = "@VERSION@";
	static final String UPDATE_JSON = "https://raw.githubusercontent.com/T145/metalchests/master/update.json";

	private static final String COMMON_PROXY = "T145.metalchests.core.proxies.CommonProxy";
	private static final String CLIENT_PROXY = "T145.metalchests.core.proxies.ClientProxy";

	public static final Logger LOG = LogManager.getLogger(MOD_ID);

	public static final CreativeTabs TAB = new CreativeTabs(MOD_ID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModLoader.METAL_CHEST, 1, 1);
		}
	};

	@Instance(MOD_ID)
	public static MetalChests instance;

	@SidedProxy(serverSide = COMMON_PROXY, clientSide = CLIENT_PROXY)
	public static ServerProxy proxy;

	@Metadata
	private ModMetadata meta;

	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		meta.authorList.add("T145");
		meta.autogenerated = false;
		meta.credits = "The fans!";
		meta.description = "The better alternative to IronChests";
		meta.logoFile = "logo.png";
		meta.modId = MOD_ID;
		meta.name = MOD_NAME;
		meta.url = "https://github.com/T145/metalchests";
		meta.useDependencyInformation = false;
		meta.version = VERSION;
		proxy.preInit(event);
	}

	@Override
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
