/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.framezaddon.client.rendering;

import com.farincorporated.framezaddon.FramezAddon;
import com.farincorporated.framezaddon.tile.TileFramePiston;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author farincorporated
 */
public class FramePistonRenderer extends FramezTileEntityRenderer implements IItemRenderer{

    ResourceLocation pistonmap;
    ResourceLocation objModelLocation;
    WavefrontObject modelpiston;
    WavefrontObject modelpistonext;
    double scale;
    
    public FramePistonRenderer() {       
        pistonmap = new ResourceLocation(FramezAddon.MODID, "textures/blocks/pistontexture.png");
        objModelLocation = new ResourceLocation(FramezAddon.MODID, "models/framepiston.obj");
        this.modelpiston = (WavefrontObject) AdvancedModelLoader.loadModel(objModelLocation);
        objModelLocation = new ResourceLocation(FramezAddon.MODID, "models/framepistonext.obj");
        this.modelpistonext = (WavefrontObject) AdvancedModelLoader.loadModel(objModelLocation);
        this.scale = 0.5;
    }
    
    protected double[] directionRotate(int meta){
        switch(meta){
            //Down
            case 0: return new double[] {-90, 1, 0, 0};
            //Up
            case 1: return new double[] {90, 1, 0, 0};
            //South
            case 3: return new double[] {180, 0, 1, 0};
            //West
            case 4: return new double[] {90, 0, 1, 0};
            //East
            case 5: return new double[] {-90, 0, 1, 0};
            default: return new double[] {0,0,0,0};
        }
    }
    
    private void TranslateModel(double x, double y, double z, int face, double progress){
        switch(face){
            case 0: GL11.glTranslated(x + 0.5, y + 0.5 - progress, z + 0.5); break;
            case 1: GL11.glTranslated(x + 0.5, y + 0.5 + progress, z + 0.5); break;
            case 2: GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5 - progress); break;
            case 3: GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5 + progress); break;
            case 4: GL11.glTranslated(x + 0.5 - progress, y + 0.5, z + 0.5); break;
            case 5: GL11.glTranslated(x + 0.5 + progress, y + 0.5, z + 0.5); break;
        }
    }
    
    public void renderPistonBase(ResourceLocation framemap){
        GL11.glPushMatrix();
        this.bindTexture(framemap);
        modelpiston.renderPart("innerframe");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        this.bindTexture(this.pistonmap);
        modelpiston.renderPart("shaft");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        this.bindTexture(framemap);
        modelpiston.renderPart("outterframe");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        this.bindTexture(this.pistonmap);
        modelpiston.renderPart("pistonbase");
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderTileEntityAt(TileEntity ti, double x, double y, double z, float timeSinceLastTick) {
        TileFramePiston tile = (TileFramePiston) ti;
        ResourceLocation framemap = super.getFrame(tile.getMaterialType());
        GL11.glDisable(GL11.GL_CULL_FACE);
        
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glScaled(scale, scale, scale);
        
        this.directionRotate(tile.getFace());

        this.renderPistonBase(framemap);
        
        if(tile.isStill()){
            GL11.glPushMatrix();
            this.bindTexture(this.pistonmap);
            modelpiston.renderPart("pistsontop");
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }else{
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            
            this.TranslateModel(x, y, z, tile.getFace(), tile.getProgress());
            
            GL11.glScaled(scale, scale, scale);
            this.directionRotate(tile.getFace());
            
            GL11.glPushMatrix();
            this.bindTexture(framemap);
            this.modelpistonext.renderPart("outterframe");
            GL11.glPopMatrix();
            
            GL11.glPushMatrix();
            this.bindTexture(framemap);
            this.modelpistonext.renderPart("innerframe");
            GL11.glPopMatrix();
            
            GL11.glPushMatrix();
            this.bindTexture(pistonmap);
            this.modelpistonext.renderPart("shaft");
            GL11.glPopMatrix();
            
            GL11.glPushMatrix();
            this.bindTexture(pistonmap);
            this.modelpistonext.renderPart("face");
            GL11.glPopMatrix();
            
            GL11.glPopMatrix();
        }
        
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        //RenderBlocks render = (RenderBlocks) data[0];
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        this.renderPistonBase(this.frametextures.get(item.hasTagCompound() ? item.getTagCompound().getString("frame_material") : "wood"));
        
        GL11.glPushMatrix();
        this.bindTexture(this.pistonmap);
        modelpiston.renderPart("pistsontop");
        GL11.glPopMatrix();
        
        GL11.glPopMatrix();
    }

    
}
