package com.nosignal.mod.events;

import com.nosignal.mod.blocks.BlockConnector;
import com.nosignal.mod.blocks.BlockHive;
import com.nosignal.mod.items.bee.ItemBeeDrone;
import com.nosignal.mod.items.bee.ItemBeeQueen;
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

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class Registries {

    public static List<Item> ITEMS = new ArrayList<>();
    public static List<Block> BLOCKS = new ArrayList<>();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        Blocks.init();
        e.getRegistry().registerAll(BLOCKS.toArray(new Block[BLOCKS.size()]));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        Items.init();
        e.getRegistry().registerAll(ITEMS.toArray(new Item[ITEMS.size()]));
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
    	
    	public static Item wire, bee_drone, bee_queen;
    	
        public static void init() {
        	wire = register(new ItemWire(), "wire");
        	bee_drone = register(new ItemBeeDrone(),"drone");
        	bee_queen = register(new ItemBeeQueen(),"queen");
        }
        
        public static Item register(Item item, String name) {
        	item.setUnlocalizedName(NoSignal.MODID + ":" + name);
        	item.setRegistryName(new ResourceLocation(NoSignal.MODID, name));
        	ITEMS.add(item);
        	return item;
        }
    }

    //this init stuff kills me!!!! use objectholders pls!!!!!! ~sub

    public static class Blocks {
    	
    	public static Block connector, hive;
    	
        public static void init() {
        	connector = register(new BlockConnector(), "connector");
        	hive = register(new BlockHive(),"hive");
        }
        
        public static Block register(Block block, String name) {
        	ResourceLocation rl = new ResourceLocation(NoSignal.MODID, name);
        	block.setUnlocalizedName(rl.toString());
        	block.setRegistryName(rl);
        	BLOCKS.add(block);
        	ITEMS.add(new ItemBlock(block).setRegistryName(rl));
        	return block;
        }
    }
    
    public static void registerTileEntity(Class<? extends TileEntity> clazz, String name) {
    	GameRegistry.registerTileEntity(clazz, new ResourceLocation(NoSignal.MODID, name));
    }
}
