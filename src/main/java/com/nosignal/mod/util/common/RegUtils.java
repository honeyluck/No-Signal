package com.nosignal.mod.util.common;

import com.nosignal.mod.blocks.INeedItem;
import com.nosignal.mod.events.Registries;
import com.nosignal.mod.main.NoSignal;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegUtils {
	
	public static Item createItem(Item item, String name) {
		item.setUnlocalizedName(NoSignal.MODID + ":" + name);
		item.setRegistryName(new ResourceLocation(NoSignal.MODID, name));
		Registries.ITEMS.add(item);
		return item;
	}
	
	public static Block createBlock(Block block, String name) {
		ResourceLocation rl = new ResourceLocation(NoSignal.MODID, name);
		block.setUnlocalizedName(rl.toString());
		block.setRegistryName(rl);
		Registries.BLOCKS.add(block);
		if(!(block instanceof INeedItem))Registries.ITEMS.add(new ItemBlock(block).setRegistryName(rl));
		else Registries.ITEMS.add(((INeedItem)block).getItem().setRegistryName(rl));
		return block;
	}
	
	public static void addTile(Class<? extends TileEntity> clazz, String name) {
		GameRegistry.registerTileEntity(clazz, new ResourceLocation(NoSignal.MODID, name));
	}
	
}
