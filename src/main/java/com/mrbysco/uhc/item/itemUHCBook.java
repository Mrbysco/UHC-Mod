package com.mrbysco.uhc.item;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.UltraHardCoremod;
import com.mrbysco.uhc.init.GuiHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class itemUHCBook extends Item{
	public itemUHCBook(String registryName) {
		this.setTranslationKey(Reference.MOD_PREFIX + registryName.replaceAll("_", ""));
		this.setRegistryName(registryName);
		this.setMaxStackSize(1);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + "Ultra Hard Coremod");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {	
		BlockPos pos = playerIn.getPosition();
		
		playerIn.openGui(UltraHardCoremod.instance, GuiHandler.GUI_UHC_BOOK, worldIn, pos.getX(), pos.getY(), pos.getZ());
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
