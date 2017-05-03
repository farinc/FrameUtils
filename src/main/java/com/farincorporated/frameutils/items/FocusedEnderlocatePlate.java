/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.items;

import com.farincorporated.frameutils.FramezAddon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

/**
 *
 * @author coolestbean
 */
public class FocusedEnderlocatePlate extends Item{

    public FocusedEnderlocatePlate() {}
    
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister reg)
    {
        this.itemIcon = reg.registerIcon(FramezAddon.MODID + ":enderplate");
    }
}
