/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.framezaddon.tile;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.trajectory.api.vec.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 *
 * @author farincorporated
 */
public class TileFramePistonExt extends TileEntity implements ISticky{

    private int direction;
    private boolean isExtended = false;
    private String material;
    public TileFramePistonExt() {}
    
    public void setPistonDirection(int direction, String material){
        this.direction = direction;
        this.isExtended = true;
        this.material = material;
    }
    
    public int getDirection(){
        return this.direction;
    }
    
    public String getMaterial(){
        return this.material;
    }
    
    public void setNotExtended(){
        this.isExtended = false;
    }
    
    public boolean isExtended(){
        return this.isExtended;
    }
    
    @Override
    public boolean canUpdate()
    {
        return false;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("pos", this.direction);
        nbt.setBoolean("isextended", isExtended);
        nbt.setString("material", material);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.direction = nbt.getInteger("pos");
        this.isExtended = nbt.getBoolean("isextended");
        this.material = nbt.getString("material");
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
