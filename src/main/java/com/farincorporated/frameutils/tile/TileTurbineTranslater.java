/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.tile;

import com.amadornes.framez.api.movement.IMovementLisener;
import com.amadornes.trajectory.api.IMovingStructure;
import erogenousbeef.bigreactors.common.multiblock.MultiblockTurbine;
import erogenousbeef.bigreactors.common.multiblock.MultiblockTurbine.VentStatus;
import erogenousbeef.bigreactors.common.multiblock.tileentity.TileEntityTurbinePartStandard;
import erogenousbeef.core.multiblock.MultiblockControllerBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 *
 * @author coolestbean
 */
public class TileTurbineTranslater extends TileEntityTurbinePartStandard implements IMovementLisener{

    public TurbineData data = null;
    public MultiblockTurbine turbine;
    
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
       this.turbine = (MultiblockTurbine) controller;
       if(this.data != null){
           data.setupTurbine(turbine);
           data = null;
       }
   }
   
    @Override
    public void onMachineBroken() {
        super.onMachineBroken();
        this.turbine = null;
    }

    @Override
    public void onFinishMoving(IMovingStructure ims) {}

    @Override
    public void onStartMoving() {
        if(this.turbine == null) return;
        data = new TurbineData(this.turbine);
    }
    
    public static class TurbineData {
        
        public float energystored;
	public boolean active;
	public boolean inductorengaged;
        public VentStatus ventstatus;
        public int maxIntakeRate;
        
        public TurbineData(MultiblockTurbine turbine){
            this.energystored = turbine.getEnergyStored();
            this.active = turbine.getActive();
            this.inductorengaged = turbine.getInductorEngaged();
            this.ventstatus = turbine.getVentSetting();
            this.maxIntakeRate = turbine.getMaxIntakeRate();
        }
        
        public void writeToNBT(NBTTagCompound tag){
            tag.setFloat("energystored", energystored);
            tag.setBoolean("active", active);
            tag.setBoolean("inductorengaged", inductorengaged);
            tag.setInteger("ventstatus", ventstatus.ordinal());
            tag.setInteger("maxIntakeRate", maxIntakeRate);
        }
        
        public void readFromNBT(NBTTagCompound tag){
            this.energystored = tag.getFloat("energystored");
            this.active = tag.getBoolean("active");
            this.inductorengaged = tag.getBoolean("inductorengaged");
            this.ventstatus = VentStatus.values()[tag.getInteger("ventstatus")];
            this.maxIntakeRate = tag.getInteger("maxIntakeRate");
        }
        
        @Override
        public String toString(){
            return "energy: " + Float.toString(this.energystored) + ", active: " + this.active + ", inductor engaged: " + inductorengaged + ", vent status: " + ventstatus.name() + ", maxIntakeRate: " + this.maxIntakeRate;
        }
        
        public void setupTurbine(MultiblockTurbine turbine){
            turbine.setStoredEnergy(this.energystored);
            turbine.setActive(active);
            turbine.setInductorEngaged(this.inductorengaged, true);
            turbine.setVentStatus(ventstatus, true);
            turbine.setMaxIntakeRate(this.maxIntakeRate);
        }
    }
    
}
