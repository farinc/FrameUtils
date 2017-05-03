/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.client;

import codechicken.nei.api.API;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.farincorporated.frameutils.FramezAddon;
import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author coolestbean
 */
public class NEIntergration {
    
    @SideOnly(Side.CLIENT)
    public static void postInitClient(){
        API.hideItem(new ItemStack(FramezAddon.framepistonext, 1, OreDictionary.WILDCARD_VALUE));
        API.hideItem(new ItemStack(FramezAddon.framepiston));
        API.hideItem(new ItemStack(FramezAddon.frametranslater));
        for(ItemStack item : FramezAddon.pistons.values()){
            API.addItemListEntry(item);
        }
        for(ItemStack item : FramezAddon.translaters.values()){
            API.addItemListEntry(item);
        }
        API.addSubset(FramezAddon.NAME + ".Blocks.Pistons", FramezAddon.pistons.values());
        API.addSubset(FramezAddon.NAME + ".Blocks.Translaters", FramezAddon.translaters.values());
    }
    
}
