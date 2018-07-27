package com.honeyluck.nosignal.events;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class Registries {

    public static List<Item> ITEMS = new ArrayList<>();
    public static List<Block> BLOCKS = new ArrayList<>();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        Blocks.init();
        for(Block block : BLOCKS) {
            e.getRegistry().register(block);
            ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
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
        public static void init() {

        }
    }

    public static class Blocks {
        public static void init() {

        }
    }
}
