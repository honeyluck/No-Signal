package com.nosignal.mod.events;

import java.util.ArrayList;
import java.util.List;

import com.nosignal.mod.blocks.BlockConnector;
import com.nosignal.mod.blocks.BlockLaser;
import com.nosignal.mod.blocks.INeedItem;
import com.nosignal.mod.items.ItemWire;
import com.nosignal.mod.main.NoSignal;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class Registries {

    public static List<Item> ITEMS = new ArrayList<>();
    public static List<Block> BLOCKS = new ArrayList<>();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        Blocks.init();
        for(Block block : BLOCKS) {
            e.getRegistry().register(block);
            System.out.println(block);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        Items.init();
        for(Item item : ITEMS) {
            e.getRegistry().register(item);
            System.out.println(item);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        // Blocks
        for (Block block : BLOCKS) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "normal"));
        }
        for (Item item : ITEMS) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }

    }



    public static class Items {
    	
    	public static Item wire;
    	
        public static void init() {
        	wire = register(new ItemWire(), "wire");
        }
        
        public static Item register(Item item, String name) {
        	item.setUnlocalizedName(NoSignal.MODID + ":" + name);
        	item.setRegistryName(new ResourceLocation(NoSignal.MODID, name));
        	ITEMS.add(item);
        	return item;
        }
    }

    public static class Blocks {
    	
    	public static Block connector;
    	public static Block laser_emmiter;
    	
        public static void init() {
        	
        	connector = register(new BlockConnector(), "connector");
        	
        	laser_emmiter = register(new BlockLaser(), "laser_emmiter");

        }
        
        public static Block register(Block block, String name) {
        	ResourceLocation rl = new ResourceLocation(NoSignal.MODID, name);
        	block.setUnlocalizedName(rl.toString());
        	block.setRegistryName(rl);
        	BLOCKS.add(block);
        	if(!(block instanceof INeedItem))ITEMS.add(new ItemBlock(block).setRegistryName(rl));
        	else ITEMS.add(((INeedItem)block).getItem().setRegistryName(rl));
        	return block;
        }
    }
    
    public static void registerTileEntity(Class<? extends TileEntity> clazz, String name) {
    	GameRegistry.registerTileEntity(clazz, new ResourceLocation(NoSignal.MODID, name));
    }
}
