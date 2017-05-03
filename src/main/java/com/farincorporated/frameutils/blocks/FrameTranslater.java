/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.blocks;

import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrame.IFrameBlock;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.farincorporated.frameutils.FramezAddon;
import com.farincorporated.frameutils.tile.TileFrameTranslater;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;

/**
 *
 * @author coolestbean
 */
public class FrameTranslater extends BlockContainer implements IFrameBlock{
    
    public FrameTranslater() {
        super(Material.iron);
        this.setHardness(2.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFrameTranslater();
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
    public int getMultipartCount(World world, BlockPos bp) {
        return 0;
    }

    @Override
    public void cloneFrame(World world, BlockPos bp, IFrame iframe) {
    }

    @Override
    public void cloneFrameBlock(World world, BlockPos bp, World world1, BlockPos bp1, IFrameBlock ifb) {
    }

    @Override
    public void harvest(World world, BlockPos bp) {
    }

    @Override
    public boolean canHaveCovers() {
        return false;
    }

    @Override
    public boolean hasPanel(IBlockAccess iba, BlockPos bp, int i) {
        return false;
    }

    @Override
    public boolean shouldRenderCross(IBlockAccess iba, BlockPos bp, int i) {
        return false;
    }

    @Override
    public boolean isSideBlocked(IBlockAccess iba, BlockPos bp, int i) {
        return false;
    }

    @Override
    public boolean isSideHidden(IBlockAccess iba, BlockPos bp, int i) {
        return false;
    }

    @Override
    public IFrameMaterial getMaterial(IBlockAccess iba, BlockPos bp) {
        TileFrameTranslater tile = (TileFrameTranslater) iba.getTileEntity(bp.x, bp.y, bp.z);
        return tile.getMaterial();
    }

    @Override
    public boolean isSideSticky(World world, BlockPos bp, int i, IMovement im) {
        TileFrameTranslater tile = (TileFrameTranslater) world.getTileEntity(bp.x, bp.y, bp.z);
        return tile.getFace() != i;
    }

    @Override
    public boolean canStickToSide(World world, BlockPos bp, int i, IMovement im) {
        TileFrameTranslater tile = (TileFrameTranslater) world.getTileEntity(bp.x, bp.y, bp.z);
        return tile.getFace() != i;
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos bp) {
        return false;
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack)
    {
        TileFrameTranslater tile = (TileFrameTranslater) world.getTileEntity(x, y, z);
        //determine framematerial through itemstack crafting and pass to tile
        if(itemstack.hasTagCompound()){
            NBTTagCompound tag = itemstack.getTagCompound();
            if(tag.hasKey("frame_material")){
                tile.setMaterial(ModifierRegistry.instance.findFrameMaterial(tag.getString("frame_material")));
            }
        }
        
        super.onBlockPlacedBy(world, x, y, z, entity, itemstack);
        this.determineOrientation(tile, world, x, y, z, entity);
        //world.markBlockForUpdate(x, y, z);
    }
    
    public void determineOrientation(TileFrameTranslater tile, World world, int x, int y, int z, EntityLivingBase entity)
    {
        int t = 0;
        if (MathHelper.abs((float)entity.posX - (float)x) < 1.30F && MathHelper.abs((float)entity.posZ - (float)z) < 1.3F)
        {
            double d0 = entity.posY + 1.82D - (double)entity.yOffset;

            if (d0 - (double)y > 2.0D)
            {
                t = 1;
            }

            if ((double)y - d0 > 0.0D)
            {
                t = 0;
            }
        }else{

            int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            t = l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));  
        } 
        
        ForgeDirection face = ForgeDirection.getOrientation(t);
        tile.setDirection(face);
    }
    
    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        TileFrameTranslater tile = (TileFrameTranslater) worldObj.getTileEntity(x, y, z);
        
        ForgeDirection face = ForgeDirection.getOrientation(tile.getFace());
        ForgeDirection newface = face.getRotation(axis);
        tile.setDirection(newface);
        
        tile.markDirty();
        worldObj.markBlockForUpdate(x, y, z);
        return true;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float p_149727_7_, float p_149727_8_, float p_149727_9_){
        if(!player.isSneaking()){
            player.openGui(FramezAddon.instance, 0, world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_)
    {
        if (!p_149690_1_.isRemote && !p_149690_1_.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            ArrayList<ItemStack> items = super.getDrops(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_7_);
            ForgeEventFactory.fireBlockHarvesting(items, p_149690_1_, this, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_7_, p_149690_6_, false, harvesters.get());
        }
    }

    private void dropPiston(World world, int x, int y, int z, ItemStack itemstack)
    {
        if(Minecraft.getMinecraft().playerController.isNotCreative() && !world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots){
            TileFrameTranslater tile = (TileFrameTranslater) world.getTileEntity(x, y, z);
            
            if (captureDrops.get())
            {
                capturedDrops.get().add(itemstack);
                return;
            }
            
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            
            if(tile != null){
                if(!itemstack.hasTagCompound()){
                    NBTTagCompound tag = new NBTTagCompound();
                    itemstack.setTagCompound(tag);
                }
                
                itemstack.getTagCompound().setString("frame_material", tile.getMaterialType());
            }else{
                System.out.println("tile is destoryed");
            }
            
            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, itemstack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        this.dropPiston(world, x, y, z, new ItemStack(Item.getItemFromBlock(this)));
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        TileFrameTranslater tile = (TileFrameTranslater) world.getTileEntity(x, y, z);
        ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("frame_material", tile.getMaterialType());
        itemstack.setTagCompound(tag);
        return itemstack;
    }
}
