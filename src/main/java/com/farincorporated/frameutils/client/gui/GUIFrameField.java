/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.client.gui;

import com.farincorporated.frameutils.FramezAddon;
import com.farincorporated.frameutils.client.packet.FrameFieldPacket;
import com.farincorporated.frameutils.tile.TileFrameTranslater;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author coolestbean
 */
public class GUIFrameField extends GuiScreen{

    private final ResourceLocation guilocation = new ResourceLocation(FramezAddon.MODID, "textures/gui/framefieldgui.png");
    private final int xSize = 186;
    private final int ySize = 74;
    private int guiTop;
    private int guiLeft;
    private final TileFrameTranslater tile;
    private final List<String> list;
    
    public GUIFrameField(World world, int x, int y, int z) {
        tile = (TileFrameTranslater) world.getTileEntity(x, y, z);
        list = Lists.newArrayList();
    }
    
    @Override
    public void initGui() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.addButtons();
    }
    
    private void addButtons(){
        //x
        this.buttonList.add(new ButtonFrameField(0, this.guiLeft + 26, this.guiTop + 14, 15, 10, this, "+x"));
        this.buttonList.add(new ButtonFrameField(1, this.guiLeft + 26, this.guiTop + 25, 15, 10, this, "-x"));
        //y
        this.buttonList.add(new ButtonFrameField(2, this.guiLeft + 88, this.guiTop + 14, 15, 10, this, "+y"));
        this.buttonList.add(new ButtonFrameField(3, this.guiLeft + 88, this.guiTop + 25, 15, 10, this, "-y"));
        //z
        this.buttonList.add(new ButtonFrameField(4, this.guiLeft + 145, this.guiTop + 14, 15, 10, this, "+z"));
        this.buttonList.add(new ButtonFrameField(5, this.guiLeft + 145, this.guiTop + 25, 15, 10, this, "-z"));
    }
    
    @Override
    public void drawScreen(int mousex, int mousey, float par4){
        this.drawDefaultBackground();
        this.drawGUI();
        this.drawEnergy();
        super.drawScreen(mousex, mousey, par4);
        this.drawText(mousex, mousey);
    }
    
    public void drawEnergy(){
        int energy = (int) ((double)tile.getEnergyStored(null) / tile.getMaxEnergyStored(null) * 132);
        this.drawTexturedModalRect(this.guiLeft + 27, this.guiTop + 58, 0, this.ySize, energy, 9);
    }
    
    private void drawText(int mousex, int mousey){
        
        this.fontRendererObj.drawString("X", this.guiLeft + 18, this.guiTop + 20, 4210752);
        this.fontRendererObj.drawString("Y", this.guiLeft + 80, this.guiTop + 20, 4210752);
        this.fontRendererObj.drawString("Z", this.guiLeft + 136, this.guiTop + 20, 4210752);
        
        this.fontRendererObj.drawString(Integer.toString(tile.getSizeX()), this.guiLeft + 43, this.guiTop + 20, 4210752);
        this.fontRendererObj.drawString(Integer.toString(tile.getSizeY()), this.guiLeft + 106, this.guiTop + 20, 4210752);
        this.fontRendererObj.drawString(Integer.toString(tile.getSizeZ()), this.guiLeft + 163, this.guiTop + 20, 4210752);
                
        if(mousex >= this.guiLeft + 27 && mousex <= this.guiLeft + 159 && mousey >= this.guiTop + 58 && mousey <= this.guiTop + 67){
            list.add(String.valueOf(tile.getEnergyStored(null)) + " / " + String.valueOf(tile.getMaxEnergyStored(null)) + " RF");
            this.drawHoveringText(list, mousex, mousey, fontRendererObj);
            list.clear();
        }
    }
    
    private void drawGUI(){
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.guilocation);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    @Override
    protected void actionPerformed(GuiButton b) {
        ButtonFrameField button = (ButtonFrameField) b;
        if(!this.isMaxORMin(button.type)){
            FramezAddon.dispatcher.sendToServer(new FrameFieldPacket(button.type, tile.xCoord, tile.yCoord, tile.zCoord));
        }
    }
    
    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }
    
    private int getSize(String type){
        if(type.endsWith("x")){
            return tile.getSizeX();
        }else if(type.endsWith("y")){
            return tile.getSizeY();
        }else if(type.endsWith("z")){
            return tile.getSizeZ();
        }
        return 0;
    }
    
    private boolean isMaxORMin(String type){
        int max = TileFrameTranslater.MAXLENGTH;
        int size = this.getSize(type);
        if(type.startsWith("+")){
            return size == max;
        }else if(type.startsWith("-")){
            return size == 1;
        }
        return true;
    }
    
    public static class ButtonFrameField extends GuiButton{
        
        GUIFrameField gui;
        String type;
        
        public ButtonFrameField(int id, int x, int y, int width, int height, GUIFrameField gui, String type) {
            super(id, x, y, width, height, "");
            this.gui = gui;
            this.type = type;
        }
        
        public int getV(boolean mouseover, boolean maxormin){
            int v = 0;
            if(type.startsWith("+")){
                if(maxormin) return v;
                v += 21;
            }else if(type.startsWith("-")){
                if(maxormin) return 11;
                v += 32;
            }
            if(mouseover) v += 21;
            return v;
        }
        
        @Override
        public void drawButton(Minecraft minecraft, int mousex, int mousey) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean mouseover = mousex >= this.xPosition && mousey >= this.yPosition && mousex < this.xPosition + this.width && mousey < this.yPosition + this.height;
            boolean maxormin = gui.isMaxORMin(type);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, gui.xSize, this.getV(mouseover, maxormin), this.width, this.height);
        }
    }
}