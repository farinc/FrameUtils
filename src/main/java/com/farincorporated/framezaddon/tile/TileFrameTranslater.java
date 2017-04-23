/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.framezaddon.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.framez.api.movement.MovementIssue;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.util.RedstoneHelper;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.farincorporated.framezaddon.FramezAddon;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.Optional;

/**
 *
 * @author coolestbean
 */
@Optional.Interface(modid = "CoFHCore", iface = "IEnergyHandler", striprefs = true)
public final class TileFrameTranslater extends TileEntity implements ISticky, IEnergyHandler {

    private ForgeDirection face;
    private IFrameMaterial material;
    public static final int MAXLENGTH = 16;
    public static final int RFPERBLOCK = 8;
    public static final int MAXRFNEEDED = (int) Math.pow(MAXLENGTH, 3) * RFPERBLOCK;
    private final List<BlockPos> listpositions;
    private final AxisAlignedBB field;
    private final EnergyStorage storage;
    private final MovementIssue calledIssue;
    
    private int sizeX = 1;
    private int sizeY = 1;
    private int sizeZ = 1;
    private int energyneeded;
    private boolean canMove;
    
    public TileFrameTranslater() {
        listpositions = Lists.newArrayList();
        field = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
        storage = new EnergyStorage(MAXRFNEEDED);
        calledIssue = MovementIssue.BLOCK.withInformation("Not enough energy");
    }

    public IFrameMaterial getMaterial(){
        if(material == null) this.material = ModifierRegistry.instance.findFrameMaterial("wood");
        return material;
    }
    
    public MovementIssue getCalledIssue(){
        return this.calledIssue.at(new BlockPos(this.xCoord, this.yCoord, this.zCoord));
    }
    
    public String getMaterialType(){
        return this.getMaterial().getType();
    }
    
    public int getFace(){
        return face.ordinal();
    }
    
    public int getSizeX(){
        return this.sizeX;
    }
    
    public int getSizeY(){
        return this.sizeY;
    }
    
    public int getSizeZ(){
        return this.sizeZ;
    }
    
    public void setDirection(ForgeDirection newface){
        face = newface;
    }
    
    public void setMaterial(IFrameMaterial newmaterial){
        material = newmaterial;
    }
    
    @Override
    public boolean isSideSticky(World world, BlockPos bp, int i, IMovement im) {
        return face.ordinal() != i;
    }

    @Override
    public boolean canStickToSide(World world, BlockPos bp, int i, IMovement im) {
        return face.ordinal() != i;
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos bp) {
        return false;
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
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        nbt.setInteger("face", face.ordinal());
        nbt.setString("material", this.getMaterialType());
        nbt.setInteger("sizeX", sizeX);
        nbt.setInteger("sizeY", sizeY);
        nbt.setInteger("sizeZ", sizeZ);
        nbt.setInteger("energyneeded", energyneeded);
        nbt.setBoolean("canMove", canMove);
        NBTTagCompound energy = new NBTTagCompound();
        storage.writeToNBT(energy);
        nbt.setTag("energy", energy);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        this.face = ForgeDirection.getOrientation(nbt.getInteger("face"));
        this.material = ModifierRegistry.instance.findFrameMaterial(nbt.getString("material"));
        this.sizeX = nbt.getInteger("sizeX");
        this.sizeY = nbt.getInteger("sizeY");
        this.sizeZ = nbt.getInteger("sizeZ");
        this.energyneeded = nbt.getInteger("energyneeded");
        this.canMove = nbt.getBoolean("canMove");
        this.storage.readFromNBT(nbt.getCompoundTag("energy"));
    }
    
    private void getBlockInAABB(boolean countAirBlocks){
        this.listpositions.clear();
        this.reset();
        int i = MathHelper.floor_double(field.minX);
        int j = MathHelper.floor_double(field.maxX + 1.0D);
        int k = MathHelper.floor_double(field.minY);
        int l = MathHelper.floor_double(field.maxY + 1.0D);
        int i1 = MathHelper.floor_double(field.minZ);
        int j1 = MathHelper.floor_double(field.maxZ + 1.0D);

        for (int x = i; x < j; ++x)
        {
            for (int y = k; y < l; ++y)
            {
                for (int z = i1; z < j1; ++z)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = this.worldObj.getBlock(x, y, z);
                    if(block.isAir(worldObj, x, y, z)){
                        if(countAirBlocks){
                            listpositions.add(pos);
                        }
                    }else{
                        listpositions.add(pos);
                    }
                }
            }
        }

    }
    
    public List<BlockPos> getPositions(){
        return this.listpositions;
    }
    
    public AxisAlignedBB getField(){
        this.setField();
        return field;
    }
    
    public boolean getCanMove(){
        return this.canMove;
    }
    
    public void setField(){
        switch(this.getFace()){
            case 0:
                //Down
                field.maxX = this.xCoord + this.sizeX;
                field.minX = this.xCoord - this.sizeX;
                field.minY = this.yCoord - this.sizeY;
                field.maxY = this.yCoord - 1;
                field.maxZ = this.zCoord + this.sizeZ;
                field.minZ = this.zCoord - this.sizeZ;
                break;
            case 1:
                //Up
                field.maxX = this.xCoord + this.sizeX;
                field.minX = this.xCoord - this.sizeX;
                field.maxY = this.yCoord + this.sizeY;
                field.minY = this.yCoord + 1;
                field.maxZ = this.zCoord + this.sizeZ;
                field.minZ = this.zCoord - this.sizeZ;
                break;
            case 2:
                //North
                field.maxX = this.xCoord + this.sizeX;
                field.minX = this.xCoord - this.sizeX;
                field.maxY = this.yCoord + this.sizeY;
                field.minY = this.yCoord - this.sizeY;
                field.minZ = this.zCoord - this.sizeZ;
                field.maxZ = this.zCoord - 1;
                break;
            case 3:
                //South
                field.maxX = this.xCoord + this.sizeX;
                field.minX = this.xCoord - this.sizeX;
                field.maxY = this.yCoord + this.sizeY;
                field.minY = this.yCoord - this.sizeY;
                field.maxZ = this.zCoord + this.sizeZ;
                field.minZ = this.zCoord + 1;
                break;
            case 4:
                //West
                field.minX = this.xCoord - this.sizeX;
                field.maxX = this.xCoord - 1;
                field.maxY = this.yCoord + this.sizeY;
                field.minY = this.yCoord - this.sizeY;
                field.maxZ = this.zCoord + this.sizeZ;
                field.minZ = this.zCoord - this.sizeZ;
                break;
            case 5:
                //East
                field.maxX = this.xCoord + this.sizeX;
                field.minX = this.xCoord + 1;
                field.maxY = this.yCoord + this.sizeY;
                field.minY = this.yCoord - this.sizeY;
                field.maxZ = this.zCoord + this.sizeZ;
                field.minZ = this.zCoord - this.sizeZ;
                break;
        }
    }
    
    public void changeField(String type){
        if(type.equals("+x")){
            this.sizeX++;
        }else if(type.equals("-x")){
            this.sizeX--;
        }else if(type.equals("+y")){
            this.sizeY++;
        }else if(type.equals("-y")){
            this.sizeY--;
        }else if(type.equals("+z")){
            this.sizeZ++;
        }else if(type.equals("-z")){
            this.sizeZ--;
        }
    }
    
    private boolean canMove(){
        if(FramezAddon.isCofhLibloaded){
            this.energyneeded = this.listpositions.size() * RFPERBLOCK;
            int energystored = this.storage.getEnergyStored();
            return energystored >= this.energyneeded;
        }else{
            return true;
        }
    }
    
    public void clearList(){
        this.listpositions.clear();
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    public void useEnergy(){
        this.storage.extractEnergy(this.energyneeded, false);
        this.reset();
    }
    
    public void reset(){
        this.energyneeded = 0;
        this.canMove = false;
    }
    
    @Override
    public void updateEntity(){
        if(RedstoneHelper.getInput(worldObj, this.xCoord, this.yCoord, this.zCoord) <= 0){
            this.setField();
            this.getBlockInAABB(false);
            this.canMove = this.canMove();
        }
    }
    
    @Override
    public boolean canUpdate(){
        return false;
    }

    @Override
    @Optional.Method(modid = "CoFHCore")
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int energy = this.storage.receiveEnergy(maxReceive, simulate);
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return energy;
    }

    @Override
    @Optional.Method(modid = "CoFHCore")
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        int energy = this.storage.extractEnergy(maxExtract, simulate);
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return energy;
    }

    @Override
    @Optional.Method(modid = "CoFHCore")
    public int getEnergyStored(ForgeDirection from) {
        return this.storage.getEnergyStored();
    }

    @Override
    @Optional.Method(modid = "CoFHCore")
    public int getMaxEnergyStored(ForgeDirection from) {
        return this.storage.getMaxEnergyStored();
    }

    @Override
    @Optional.Method(modid = "CoFHCore")
    public boolean canConnectEnergy(ForgeDirection from) {
        return from != face;
    }
}
