package com.nosignal.mod.items;

import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.util.BeeBaseConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemBee extends Item {

    public ItemBee() {
        this.setCreativeTab(NoSignal.beesTabs);
    }

    public static void setGeneration(ItemStack stack, int value){
        if(stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger(NBT.GENERATION,value);
    }

    public static void setTamed(ItemStack stack, int value){
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger(NBT.TAMED,value);
    }

    public static int getGeneration(ItemStack stack){
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT.GENERATION))
            return stack.getTagCompound().getInteger(NBT.GENERATION);
        return BeeBaseConfig.GENERATION;
    }

    public static int getTamed(ItemStack stack){
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT.TAMED))
            return stack.getTagCompound().getInteger(NBT.TAMED);
        return BeeBaseConfig.TAMED;
    }

    public class NBT{
        public static final String GENERATION = "bee_generation";
        public static final String TAMED = "bee_tamed";
    }
}
