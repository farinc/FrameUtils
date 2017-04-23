/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.framezaddon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 *
 * @author coolestbean
 */
public class DebugItem extends Item {

    public DebugItem() {
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World world, EntityPlayer player)
    {
        if(!world.isRemote){
            ItemStack stack = player.inventory.getStackInSlot(0);
            if(stack != null){
                System.out.println(stack.toString());
                if(stack.hasTagCompound()) System.out.println(stack.getTagCompound().toString());
            }
        }
        return p_77659_1_;
    }
}
