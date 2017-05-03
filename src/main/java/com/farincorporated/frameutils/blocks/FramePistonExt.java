/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.blocks;

import com.farincorporated.frameutils.tile.TileFramePistonExt;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrame.IFrameBlock;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.trajectory.api.vec.BlockPos;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 *
 * @author coolestbean
 */
public class FramePistonExt extends BlockContainer implements IFrameBlock{

    public FramePistonExt() {
        super(Material.iron);
        this.setHardness(2.0F);
    }
    
    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return null;
    }
    
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public int getRenderType(){
        return -1;
    }
    
    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFramePistonExt();
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_)
    {
        TileFramePistonExt tile = (TileFramePistonExt) world.getTileEntity(x, y, z);
        ForgeDirection direction = ForgeDirection.getOrientation(tile.getDirection());
        if(tile.isExtended()) world.setBlockToAir(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
        super.breakBlock(world, x, y, z, block, p_149749_6_);
    }

    @Override
    public int getMultipartCount(World world, BlockPos position) {
        return 0;
    }

    @Override
    public void cloneFrame(World world, BlockPos position, IFrame frame) {
    }

    @Override
    public void cloneFrameBlock(World world, BlockPos position, World frameWorld, BlockPos framePosition, IFrameBlock frame) {
    }

    @Override
    public void harvest(World world, BlockPos position) {
    }

    @Override
    public boolean canHaveCovers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasPanel(IBlockAccess world, BlockPos position, int side) {
        return false;
    }

    @Override
    public boolean shouldRenderCross(IBlockAccess world, BlockPos position, int side) {
        return false;
    }

    @Override
    public boolean isSideBlocked(IBlockAccess world, BlockPos position, int side) {
        return false;
    }

    @Override
    public boolean isSideHidden(IBlockAccess world, BlockPos position, int side) {
        return false;
    }

    @Override
    public IFrameMaterial getMaterial(IBlockAccess world, BlockPos position) {
        TileFramePistonExt tile = (TileFramePistonExt) world.getTileEntity(position.x, position.y, position.z);
        IFrameMaterial mat = ModifierRegistry.instance.findFrameMaterial(tile.getMaterial());
        return mat != null ? mat : ModifierRegistry.instance.findFrameMaterial("wood");
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {
        return true;
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {
        return true;
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {
        return false;
    }
    
}
