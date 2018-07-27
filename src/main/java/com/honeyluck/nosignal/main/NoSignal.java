package com.honeyluck.nosignal.main;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = NoSignal.MODID, name = NoSignal.NAME, version = NoSignal.VERSION)
public class NoSignal {
    public static final String MODID = "ns";
    public static final String NAME = "No Signal";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.honeyluck.nosignal.main.ClientProxy", serverSide = "com.honeyluck.nosignal.main.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {

    }

}
