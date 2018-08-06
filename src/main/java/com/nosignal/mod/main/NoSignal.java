package com.nosignal.mod.main;

import com.nosignal.mod.events.Registries;
import com.nosignal.mod.events.Registries.Blocks;
import com.nosignal.mod.tileentity.TileEntityConnector;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = NoSignal.MODID, name = NoSignal.NAME, version = NoSignal.VERSION)
public class NoSignal {
    public static final String MODID = "nosignal";
    public static final String NAME = "No Signal";
    public static final String VERSION = "1.0";
    
    public static CreativeTabs tab;
    public static CreativeTabs beesTabs;

    @SidedProxy(clientSide = "com.nosignal.mod.main.ClientProxy", serverSide = "com.nosignal.mod.main.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {

    	tab = new CreativeTabs(MODID) {
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(Blocks.connector);
			}};
    	beesTabs = new CreativeTabs(MODID + " bees") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Blocks.hive);
            }
        };
    	Registries.registerTileEntity(TileEntityConnector.class, "TileEntityConnector");
    	
    	proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {

    	proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {

    	proxy.postInit();
    }

}
