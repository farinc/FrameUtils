/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.tile;

import com.amadornes.framez.api.movement.IMovementLisener;
import com.amadornes.trajectory.api.IMovingStructure;
import erogenousbeef.bigreactors.common.multiblock.MultiblockReactor;
import erogenousbeef.bigreactors.common.multiblock.tileentity.TileEntityReactorPart;
import erogenousbeef.core.multiblock.MultiblockControllerBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 *
 * @author coolestbean
 */
public class TileReactorTranslater extends TileEntityReactorPart implements IMovementLisener {

    public ReactorData data = null;
    public MultiblockReactor reactor;
    
    @Override
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        if(data != null) this.data.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        if(data != null) this.data.readFromNBT(nbt);
    }
    
   @Override
   public void onMachineAssembled(MultiblockControllerBase controller) {
       super.onMachineAssembled(controller);
       this.reactor = (MultiblockReactor) controller;
       if(this.data != null){
           data.setupReactor(reactor);
           data = null;
       }
   }
   
    @Override
    public void onMachineBroken() {
        super.onMachineBroken();
        this.reactor = null;
    }

    @Override
    public void onFinishMoving(IMovingStructure ims) {}

    @Override
    public void onStartMoving() {
        if(this.reactor == null) return;
        data = new ReactorData(this.reactor);
    }
    
    public static class ReactorData {
        
        public float energystored;
        public boolean active;
        public float reactorheat;
        public MultiblockReactor.WasteEjectionSetting setting;
        
        public ReactorData(MultiblockReactor reactor){
            this.energystored = reactor.getEnergyStored();
            this.active = reactor.getActive();
            this.reactorheat = reactor.getReactorHeat();
            this.setting = reactor.getWasteEjection();
        }
        
        public void writeToNBT(NBTTagCompound tag){
            tag.setFloat("energystored", energystored);
            tag.setBoolean("active", active);
            tag.setFloat("reactorheat", reactorheat);
            tag.setInteger("setting", this.setting.ordinal());
        }
        
        public void readFromNBT(NBTTagCompound tag){
            this.energystored = tag.getFloat("energystored");
            this.active = tag.getBoolean("active");
            this.reactorheat = tag.getFloat("reactorheat");
            this.setting = MultiblockReactor.WasteEjectionSetting.values()[tag.getInteger("setting")];
        }
        
        @Override
        public String toString(){
            return "energy: " + Float.toString(this.energystored) + ", active: " + this.active + ", heat: " + Float.toString(this.reactorheat) + ", setting: " + this.setting.name();
        }
        
        public void setupReactor(MultiblockReactor reactor){
            reactor.setEnergyStored(energystored);
            reactor.setActive(active);
            reactor.setReactorHeat(reactorheat);
            reactor.setWasteEjection(setting);
        }
    }
}