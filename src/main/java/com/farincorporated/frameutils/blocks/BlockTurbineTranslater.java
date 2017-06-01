/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.blocks;

import com.farincorporated.frameutils.tile.TileReactorTranslater;
import com.farincorporated.frameutils.tile.TileTurbineTranslater;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 *
 * @author coolestbean
 */
public class BlockTurbineTranslater extends BlockContainer{

    public BlockTurbineTranslater() {
        super(Material.iron);
        this.setHardness(2.0f);
        this.setStepSound(soundTypeMetal);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileTurbineTranslater();
    }
    
    @Override    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("frameutils:turbinetranslater");
    }
}
