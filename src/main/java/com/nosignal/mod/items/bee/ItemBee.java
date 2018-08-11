package com.nosignal.mod.items.bee;

import java.util.List;

import javax.annotation.Nullable;

import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.util.BeeBaseConfig;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBee extends Item {
	
	public ItemBee() {
		this.setCreativeTab(NoSignal.beesTabs);
	}
	
	public static void setGeneration(ItemStack stack, int value) {
		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger(NBT.GENERATION, value);
	}
	
	public static void setTamed(ItemStack stack, int value) {
		if (value < 100) {
			if (stack.getTagCompound() == null)
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger(NBT.TAMED, value);
		}
	}
	
	public static int getGeneration(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT.GENERATION))
			return stack.getTagCompound().getInteger(NBT.GENERATION);
		return BeeBaseConfig.GENERATION;
	}
	
	public static int getTamed(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT.TAMED))
			return stack.getTagCompound().getInteger(NBT.TAMED);
		return BeeBaseConfig.TAMED;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey(NBT.GENERATION)) {
				tooltip.add("Generation : " + getGeneration(stack));
			}
			if (stack.getTagCompound().hasKey(NBT.TAMED)) {
				tooltip.add("Tamed : " + getTamed(stack) + "%");
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		ItemBee bee = (ItemBee) stack.getItem();
		setGeneration(stack, BeeBaseConfig.GENERATION);
		setTamed(stack, BeeBaseConfig.TAMED);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote && !stack.hasTagCompound()) {
			this.onCreated(stack, worldIn, (EntityPlayer) entityIn);
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	public class NBT {
		public static final String GENERATION = "bee_generation";
		public static final String TAMED = "bee_tamed";
	}
}
