package com.nosignal.mod.items;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.util.Helper;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMarker extends Item {
	
	public ItemMarker() {
		this.setCreativeTab(NoSignal.tab);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		NBTTagCompound tag = Helper.getItemTag(player.getHeldItem(hand));
		if(tag.hasKey(NBT.FIRST_POS)) {
			tag.setLong(NBT.LAST_POS, pos.offset(facing).toLong());
		}
		else tag.setLong(NBT.FIRST_POS, pos.offset(facing).toLong());
		player.getHeldItem(hand).setTagCompound(tag);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(worldIn.isRemote) {
			NBTTagCompound tag = Helper.getItemTag(playerIn.getHeldItem(handIn));
			BlockPos fp = BlockPos.fromLong(tag.getLong(NBT.FIRST_POS));
			BlockPos ep = BlockPos.fromLong(tag.getLong(NBT.LAST_POS));
			BlockPos origin = ep.subtract(fp);
			origin = new BlockPos(origin.getX() / 2, origin.getY() / 2, origin.getZ() / 2);
			origin.add(fp);
			System.out.println(origin);
			try {
				File f = new File(Minecraft.getMinecraft().mcDataDir + "/render.json");
				if(!f.exists())f.createNewFile();
				GsonBuilder gb = new GsonBuilder();
				gb.setPrettyPrinting();
				JsonWriter jw = gb.create().newJsonWriter(new FileWriter(f));
				jw.beginObject();
				
				jw.name("structure");
				jw.beginObject();
				for(BlockPos poss : BlockPos.getAllInBox(fp, ep)) {
					if(worldIn.getBlockState(poss).getMaterial() != Material.AIR) {
						jw.name("map");
						jw.beginObject();
						jw.name("X").value(fp.getX() - poss.getX());
						jw.name("Y").value(fp.getY() - poss.getY());
						jw.name("Z").value(fp.getZ() - poss.getZ());
						jw.name("Block").value(worldIn.getBlockState(poss).getBlock().getRegistryName().toString());
						jw.name("Meta").value(worldIn.getBlockState(poss).getBlock().getMetaFromState(worldIn.getBlockState(poss)));
						jw.endObject();
					}
				}
				jw.endObject();
				
				jw.endObject();
				
				jw.close();
			}
			catch(IOException e) {}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound tag = Helper.getItemTag(stack);
		if(tag.hasKey(NBT.FIRST_POS))tooltip.add("Start pos: " + BlockPos.fromLong(tag.getLong(NBT.FIRST_POS)));
		if(tag.hasKey(NBT.LAST_POS))tooltip.add("End pos: " + BlockPos.fromLong(tag.getLong(NBT.LAST_POS)));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	public static class NBT{
		public static final String FIRST_POS = "first_pos";
		public static final String LAST_POS = "last_pos";
	}
}
