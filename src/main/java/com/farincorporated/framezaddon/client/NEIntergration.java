/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.framezaddon.client;

import codechicken.nei.api.API;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.farincorporated.framezaddon.FramezAddon;
import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
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
        ItemStack framepiston;
        ItemStack frametranslater;
        List<ItemStack> listpiston = Lists.newArrayList();
        List<ItemStack> listranslater = Lists.newArrayList();
        for (IFrameMaterial mat : ModifierRegistry.instance.frameMaterials) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("frame_material", mat.getType());
            framepiston = new ItemStack(FramezAddon.framepiston, 1, 0);
            frametranslater = new ItemStack(FramezAddon.frametranslater, 1, 0);
            framepiston.setTagCompound(tag);
            frametranslater.setTagCompound(tag);
            API.addItemListEntry(framepiston);
            API.addItemListEntry(frametranslater);
            listpiston.add(framepiston);
            listranslater.add(frametranslater);
        }
        API.addSubset(FramezAddon.NAME + ".Blocks.Pistons", listpiston);
        API.addSubset(FramezAddon.NAME + ".Blocks.Translaters", listranslater);
    }
    
}
