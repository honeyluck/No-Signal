package com.nosignal.mod.events;

import java.util.ArrayList;
import java.util.List;

import com.nosignal.mod.blocks.BlockConnector;
import com.nosignal.mod.blocks.BlockHive;
import com.nosignal.mod.blocks.BlockLaser;
import com.nosignal.mod.items.ItemWire;
import com.nosignal.mod.items.bee.ItemBeeDrone;
import com.nosignal.mod.items.bee.ItemBeeQueen;
import com.nosignal.mod.items.bee.ItemHoneyComb;
import com.nosignal.mod.tileentity.TileEntityConnector;
import com.nosignal.mod.tileentity.TileEntityHive;
import com.nosignal.mod.tileentity.TileEntityLaser;
import com.nosignal.mod.util.common.RegUtils;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class Registries {
	
	public static List<Item> ITEMS = new ArrayList<>();
	public static List<Block> BLOCKS = new ArrayList<>();
	
	//Items
	public static Item ITEM_WIRE = RegUtils.createItem(new ItemWire(), "wire");
	public static Item ITEM_BEE_DRONE = RegUtils.createItem(new ItemBeeDrone(), "drone");
	public static Item ITEM_BEE_QUEEN = RegUtils.createItem(new ItemBeeQueen(), "queen");
	public static Item ITEM_HONEY_COMB = RegUtils.createItem(new ItemHoneyComb(), "honeycomb");
	
	//Blocks
	public static Block BLOCK_CONNECTOR = RegUtils.createBlock(new BlockConnector(), "connector");
	public static Block BLOCK_HIVE = RegUtils.createBlock(new BlockHive(), "hive");
	public static Block BLOCK_LASER_EMITTER = RegUtils.createBlock(new BlockLaser(), "laser_emmiter");
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> e) {
		IForgeRegistry<Block> reg = e.getRegistry();
		regTiles();
		reg.registerAll(BLOCKS.toArray(new Block[BLOCKS.size()]));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e) {
		IForgeRegistry<Item> reg = e.getRegistry();
		reg.registerAll(ITEMS.toArray(new Item[ITEMS.size()]));
	}
	
	private static void regTiles() {
		RegUtils.addTile(TileEntityConnector.class, "TileEntityConnector");
		RegUtils.addTile(TileEntityLaser.class, "TileEntityLaser");
		RegUtils.addTile(TileEntityHive.class,"TileEntityHive");
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (Block block : BLOCKS) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "normal"));
		}
		for (Item item : ITEMS) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}
}
