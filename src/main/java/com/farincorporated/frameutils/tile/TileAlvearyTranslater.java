/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.tile;

import com.amadornes.framez.api.movement.IMovementLisener;
import com.amadornes.trajectory.api.IMovingStructure;
import com.mojang.authlib.GameProfile;
import forestry.api.multiblock.IAlvearyComponent;
import forestry.api.multiblock.IMultiblockController;
import forestry.api.multiblock.IMultiblockLogicAlveary;
import forestry.api.multiblock.MultiblockManager;
import forestry.api.multiblock.MultiblockTileEntityBase;
import forestry.apiculture.multiblock.AlvearyController;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;

/**
 *
 * @author coolestbean
 */
public class TileAlvearyTranslater extends MultiblockTileEntityBase<IMultiblockLogicAlveary> implements IAlvearyComponent, IMovementLisener{

    public AlvearyController alveary = null;
    public AlvearyData data;

    public TileAlvearyTranslater() {
        super(MultiblockManager.logicFactory.createAlvearyLogic());
    }

    @Override
    public GameProfile getOwner() {
        return this.alveary != null ? this.alveary.getOwner() : null;
    }

    @Override
    public void onMachineAssembled(IMultiblockController imc, ChunkCoordinates cc, ChunkCoordinates cc1) {
        this.alveary = (AlvearyController) imc;
        if(this.data != null){
            data.setupAlveary(alveary);
            data = null;
        }
    }

    @Override
    public void onMachineBroken() {
        this.alveary = null;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        if(data != null) data.writeToNBT(nbt);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        if(data != null) data.readFromNBT(nbt);
    }

    @Override
    public void onFinishMoving(IMovingStructure ims) {}

    @Override
    public void onStartMoving() {
        if(this.alveary == null) return;
        data = new AlvearyData(this.alveary);
    }
    
    public static class AlvearyData {
    
        public ItemStack drone;
        public ItemStack queen;
        public NBTTagCompound logic;
        public NBTTagCompound internal;
        
        public AlvearyData(AlvearyController alveary){
            this.drone = alveary.getBeeInventory().getDrone();
            this.queen = alveary.getBeeInventory().getQueen();
            NBTTagCompound logictag = new NBTTagCompound(), internaltag = new NBTTagCompound();
            alveary.getBeekeepingLogic().writeToNBT(logictag);
            alveary.getInternalInventory().writeToNBT(internaltag);
            this.logic = logictag;
            this.internal = internaltag;
        }
        
        public void writeToNBT(NBTTagCompound tag){
            NBTTagCompound dronenbt = new NBTTagCompound(), queennbt = new NBTTagCompound();
            drone.writeToNBT(dronenbt);
            queen.writeToNBT(queennbt);
            
            tag.setTag("drone", dronenbt);
            tag.setTag("queen", queennbt);
            tag.setTag("logic", logic);
            tag.setTag("internal", internal);
        }
        
        public void readFromNBT(NBTTagCompound tag){
            this.drone = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("drone"));
            this.queen = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("queen"));
            this.logic = tag.getCompoundTag("logic");
            this.internal = tag.getCompoundTag("internal");
        }
        
        public void setupAlveary(AlvearyController alveary){
            alveary.getBeeInventory().setDrone(drone);
            alveary.getBeeInventory().setQueen(queen);
            alveary.getBeekeepingLogic().readFromNBT(logic);
            alveary.getInternalInventory().readFromNBT(internal);
        }
    }
    
}
