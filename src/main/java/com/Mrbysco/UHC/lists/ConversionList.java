package com.Mrbysco.UHC.lists;

import java.util.ArrayList;

import com.Mrbysco.UHC.lists.info.ItemConversionInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ConversionList {
	public static ArrayList<ItemConversionInfo> conversionList = new ArrayList<>();

	public static ItemConversionInfo conversion_info;
	
	public static void initializeConversion() {
		
	}
	
	public static void addConversion(Item itemIn, int meta, ItemStack result)
	{
		// Check if the info doesn't already exist
		conversion_info = new ItemConversionInfo(itemIn, meta, result);
		if(conversionList.contains(conversion_info))
			return;
		else
			conversionList.add(conversion_info);
	}
	
	public static void addConversion(Item itemIn, int meta, ItemStack result, ItemStack result2) {
		// Check if the info doesn't already exist
		conversion_info = new ItemConversionInfo(itemIn, meta, result, result2);
		if(conversionList.contains(conversion_info))
			return;
		else
			conversionList.add(conversion_info);
	}
	
	public static void addConversion(Item itemIn, int meta, ItemStack result, ItemStack result2, ItemStack result3) {
		// Check if the info doesn't already exist
		conversion_info = new ItemConversionInfo(itemIn, meta, result, result2, result3);
		if(conversionList.contains(conversion_info))
			return;
		else
			conversionList.add(conversion_info);
	}
	
	public static void addConversion(Item itemIn, int meta, ItemStack result, ItemStack result2, ItemStack result3, ItemStack result4) {
		// Check if the info doesn't already exist
		conversion_info = new ItemConversionInfo(itemIn, meta, result, result2, result3, result4);
		if(conversionList.contains(conversion_info))
			return;
		else
			conversionList.add(conversion_info);
	}
	
	public static void addConversion(Item itemIn, int meta, ItemStack result, ItemStack result2, ItemStack result3, ItemStack result4, 
			ItemStack result5) {
		// Check if the info doesn't already exist
		conversion_info = new ItemConversionInfo(itemIn, meta, result, result2, result3, result4, result5);
		if(conversionList.contains(conversion_info))
			return;
		else
			conversionList.add(conversion_info);
	}
	
	public static void addConversion(Item itemIn, int meta, ItemStack result, ItemStack result2, ItemStack result3, ItemStack result4, 
			ItemStack result5, ItemStack result6) {
		// Check if the info doesn't already exist
		conversion_info = new ItemConversionInfo(itemIn, meta, result, result2, result3, result4, result5, result6);
		if(conversionList.contains(conversion_info))
			return;
		else
			conversionList.add(conversion_info);
	}
	
	public static void addConversion(Item itemIn, int meta, ItemStack result, ItemStack result2, ItemStack result3, ItemStack result4, 
			ItemStack result5, ItemStack result6, ItemStack result7) {
		// Check if the info doesn't already exist
		conversion_info = new ItemConversionInfo(itemIn, meta, result, result2, result3, result4, result5, result6, result7);
		if(conversionList.contains(conversion_info))
			return;
		else
			conversionList.add(conversion_info);
	}
	
	public static void addConversion(Item itemIn, int meta, ItemStack result, ItemStack result2, ItemStack result3, ItemStack result4, 
			ItemStack result5, ItemStack result6, ItemStack result7, ItemStack result8) {
		// Check if the info doesn't already exist
		conversion_info = new ItemConversionInfo(itemIn, meta, result, result2, result3, result4, result5, result6, result7, result8);
		if(conversionList.contains(conversion_info))
			return;
		else
			conversionList.add(conversion_info);
	}
	
	public static void addConversion(Item itemIn, int meta, ItemStack result, ItemStack result2, ItemStack result3, ItemStack result4, 
			ItemStack result5, ItemStack result6, ItemStack result7, ItemStack result8, ItemStack result9) {
		// Check if the info doesn't already exist
		conversion_info = new ItemConversionInfo(itemIn, meta, result, result2, result3, result4, result5, result6, result7, result8,
				result9);
		if(conversionList.contains(conversion_info))
			return;
		else
			conversionList.add(conversion_info);
	}
}
