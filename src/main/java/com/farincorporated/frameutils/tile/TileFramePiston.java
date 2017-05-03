/*
 * Copyright (C) 2017 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.farincorporated.frameutils.tile;

import com.farincorporated.frameutils.tile.TileFramePistonExt;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.util.RedstoneHelper;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.farincorporated.frameutils.FramezAddon;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


/**
 *
 * @author farincorporated
 */
public class TileFramePiston extends TileEntity implements ISticky {

    private ForgeDirection meta;
    private IFrameMaterial material;
    private float progress;
    private boolean isExtended;

    public TileFramePiston() {
        this.meta = ForgeDirection.NORTH;
        this.progress = 0;
        this.isExtended = false;
    }
    
    public int getFace(){
        return meta.ordinal();
    }
    
    public int getX(){
        return this.xCoord + this.meta.offsetX;
    }
    
    public int getY(){
        return this.yCoord + this.meta.offsetY;
    }
    
    public int getZ(){
        return this.zCoord + this.meta.offsetZ;
    }
    
    public double getProgress(){
        return (double)this.progress;
    }
    
    public IFrameMaterial getMaterial(){
        return material;
    }
    
    public String getMaterialType(){
        return material != null ? material.getType() : "wood";
    }
    
    public void setMaterial(IFrameMaterial material){
        this.material = material;
    }
    
    public TileFramePiston setDirection(ForgeDirection direction){
        this.meta = direction;
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return this;
    }
    
    @Override
    public void updateEntity(){
        
        if(RedstoneHelper.getInput(worldObj, xCoord, yCoord, zCoord) > 0){
            if(!this.worldObj.isAirBlock(this.getX(), this.getY(), this.getZ()) || this.isExtended) return;
            if(progress < 1.0F) progress += 0.1F;
            if(progress >= 1.0F && !isExtended){
                this.worldObj.setBlock(this.getX(), this.getY(), this.getZ(), FramezAddon.framepistonext);
                TileFramePistonExt ti = (TileFramePistonExt) this.worldObj.getTileEntity(this.getX(), this.getY(), this.getZ());
                if(ti != null) ti.setPistonDirection(this.meta.getOpposite().ordinal(), this.getMaterialType());
                this.isExtended = true;
                this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }else{
            if(progress > 0F) progress -= 0.1F;
            if(progress <= 0F && isExtended){
                TileFramePistonExt ti = (TileFramePistonExt) this.worldObj.getTileEntity(this.getX(), this.getY(), this.getZ());
                if(ti != null) ti.setNotExtended();
                this.isExtended = false;
                this.worldObj.setBlockToAir(this.getX(), this.getY(), this.getZ());
                this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
        
        if(0 < this.progress && this.progress < 1) this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    public boolean isStill(){
        return !this.isExtended && this.progress <= 0;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("direction", this.meta.ordinal());
        nbt.setFloat("progress", progress);
        nbt.setBoolean("isExtended", isExtended);
        if(material != null) nbt.setString("material", material.getType());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.meta = ForgeDirection.getOrientation(nbt.getInteger("direction"));
        this.progress = nbt.getFloat("progress");
        this.isExtended = nbt.getBoolean("isExtended");
        this.material = ModifierRegistry.instance.findFrameMaterial(nbt.getString("material"));
    }
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
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
