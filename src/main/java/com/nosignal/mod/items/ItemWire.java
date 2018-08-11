package com.nosignal.mod.items;

import java.util.List;

import com.nosignal.mod.interfaces.IEnergyConnector;
import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.util.Helper;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemWire extends Item {
	
	public ItemWire() {
		this.setCreativeTab(NoSignal.tab);
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(worldIn.getTileEntity(pos) instanceof IEnergyConnector) {
			NBTTagCompound tag = Helper.getItemTag(player.getHeldItem(hand));
			if(tag.hasKey(NBT.START_POINT)) {
				BlockPos startPos = BlockPos.fromLong(tag.getLong(NBT.START_POINT));
				if(startPos.distanceSq(pos) < Math.pow(16, 2)) {
					((IEnergyConnector)worldIn.getTileEntity(pos)).addConnection(BlockPos.fromLong(tag.getLong(NBT.START_POINT)));
					((IEnergyConnector)worldIn.getTileEntity(BlockPos.fromLong(tag.getLong(NBT.START_POINT)))).addConnection(pos);
					player.getHeldItem(hand).setTagCompound(new NBTTagCompound());
					player.getHeldItem(hand).shrink(1);
				}
				
			}
			else {
				tag.setLong(NBT.START_POINT, pos.toLong());
			}
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if(!worldIn.isRemote && isSelected && worldIn.getWorldTime() % 10 == 0 && entityIn instanceof EntityPlayer) {
			NBTTagCompound tag = Helper.getItemTag(stack);
			if(tag.hasKey(NBT.START_POINT)) {
				BlockPos pos = BlockPos.fromLong(tag.getLong(NBT.START_POINT));
				TextFormatting form = entityIn.getDistanceSq(pos) < Math.pow(16, 2) ? TextFormatting.GOLD : TextFormatting.RED;
				((EntityPlayer)entityIn).sendStatusMessage(new TextComponentString(form + "Connected at " + Helper.formatBlockPos(pos) + TextFormatting.RESET), true);
			}
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(Helper.getItemTag(stack).hasKey(NBT.START_POINT)) {
			tooltip.add(new TextComponentTranslation("connection.nosignal.point").getFormattedText() + " " + Helper.formatBlockPos(BlockPos.fromLong(Helper.getItemTag(stack).getLong(NBT.START_POINT))));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	public static class NBT{
		public static final String START_POINT = "connection_point";
	}
}
