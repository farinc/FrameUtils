package com.farincorporated.framezaddon.blocks;

import com.farincorporated.framezaddon.tile.TileFramePiston;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrame.IFrameBlock;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.trajectory.api.vec.BlockPos;
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
 * @author farincorporated
 */
public class FramePiston extends BlockContainer implements IFrameBlock{

    public FramePiston() {
        super(Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFramePiston();
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileFramePiston tile = (TileFramePiston) world.getTileEntity(x, y, z);
        world.setBlockToAir(tile.getX(), tile.getY(), tile.getZ());
        this.dropPiston(world, x, y, z, new ItemStack(Item.getItemFromBlock(this)));
        super.breakBlock(world, x, y, z, block, meta);
    }   
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack)
    {
        TileFramePiston tile = (TileFramePiston) world.getTileEntity(x, y, z);
        //determine framematerial through itemstack crafting and pass to tile
        if(itemstack.hasTagCompound()){
            NBTTagCompound tag = itemstack.getTagCompound();
            if(tag.hasKey("frame_material")){
                tile.setMaterial(ModifierRegistry.instance.findFrameMaterial(tag.getString("frame_material")));
            }
        }
        
        super.onBlockPlacedBy(world, x, y, z, entity, itemstack);
        this.determineOrientation(tile, world, x, y, z, entity);
    }

    public void determineOrientation(TileFramePiston tile, World world, int x, int y, int z, EntityLivingBase entity)
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
        
        tile.setDirection(ForgeDirection.getOrientation(t));
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
            TileFramePiston tile = (TileFramePiston) world.getTileEntity(x, y, z);
            
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
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        TileFramePiston tile = (TileFramePiston) world.getTileEntity(x, y, z);
        ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("frame_material", tile.getMaterialType());
        itemstack.setTagCompound(tag);
        return itemstack;
    }
    
    //cordanates passed are pre-move
    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        TileFramePiston tile = (TileFramePiston) worldObj.getTileEntity(x, y, z);
        
        ForgeDirection face = ForgeDirection.getOrientation(tile.getFace());
        ForgeDirection newface = face.getRotation(axis);
        tile.setDirection(newface);
        
        tile.markDirty();
        worldObj.markBlockForUpdate(x, y, z);
        return true;
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
        TileFramePiston tile = (TileFramePiston) iba.getTileEntity(bp.x, bp.y, bp.z);
        IFrameMaterial mat = tile.getMaterial();
        return mat != null ? mat : ModifierRegistry.instance.findFrameMaterial("wood");
    }

    @Override
    public boolean isSideSticky(World world, BlockPos bp, int i, IMovement im) {
        return true;
    }

    @Override
    public boolean canStickToSide(World world, BlockPos bp, int i, IMovement im) {
        return true;
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos bp) {
        return false;
    }
}