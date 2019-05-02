package com.Mrbysco.UHC.init;

import com.Mrbysco.UHC.gui.GuiUHCBook;
import com.Mrbysco.UHC.packets.ModPackethandler;
import com.Mrbysco.UHC.packets.UHCPacketMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public static final int GUI_UHC_BOOK = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUI_UHC_BOOK)
		{
			if(DimensionManager.getWorld(0) != null)
			{
				ModPackethandler.INSTANCE.sendTo(new UHCPacketMessage(UHCSaveData.getForWorld(DimensionManager.getWorld(0))), (EntityPlayerMP) player);
			}
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUI_UHC_BOOK)
		{
			return new GuiUHCBook(player);
		}

		return null;
	}

}