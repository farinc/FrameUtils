/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.blocks;

import com.farincorporated.frameutils.tile.TileAlvearyTranslater;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 *
 * @author coolestbean
 */
public class AlvearyTranslater extends BlockContainer {

    IIcon sides;
    IIcon topbottom;
    
    public AlvearyTranslater() {
        super(Material.wood);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileAlvearyTranslater();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
        if(side == 1 || side == 0){
            return topbottom;
        }else{
            return sides;
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg){
        this.topbottom = reg.registerIcon("forestry:apiculture/alveary.bottom");
        this.sides = reg.registerIcon("frameutils:alvearytranslater");
    }
}
