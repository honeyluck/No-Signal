package com.nosignal.mod.main;

import com.nosignal.mod.events.Registries;
import com.nosignal.mod.interfaces.IProxy;
import com.nosignal.mod.world.gen.HiveGen;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = NoSignal.MODID, name = NoSignal.NAME, version = NoSignal.VERSION)
public class NoSignal {
	public static final String MODID = "nosignal";
	public static final String NAME = "No Signal";
	public static final String VERSION = "1.0";
	public static final String CLIENTPROXY = "com.nosignal.mod.main.ClientProxy";
	public static final String SERVERPROXY = "com.nosignal.mod.main.ServerProxy";
	
	
	public static CreativeTabs tab, beesTabs;
	
	@SidedProxy(modId = MODID,clientSide = CLIENTPROXY,serverSide = SERVERPROXY)
	public static IProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		
		tab = new CreativeTabs(MODID) {
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(Registries.BLOCK_CONNECTOR);
			}};
			
			beesTabs = new CreativeTabs(MODID + " bees") {
				@Override
				public ItemStack getTabIconItem() {
					return new ItemStack(Registries.BLOCK_HIVE);
				}
			};
			
			proxy.preInit();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init();
		GameRegistry.registerWorldGenerator(new HiveGen(), 0);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit();
	}
	
}
