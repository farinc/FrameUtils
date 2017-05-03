/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.client.rendering;

import com.farincorporated.frameutils.FramezAddon;
import com.farincorporated.frameutils.tile.TileFrameTranslater;
import java.awt.Color;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author coolestbean
 */
public class FrameTranslaterRenderer extends FramezTileEntityRenderer implements IItemRenderer{

    ResourceLocation fieldtexture;
    ResourceLocation objModelLocation;
    WavefrontObject modelpiston;
    double scale;
    
    public FrameTranslaterRenderer() {       
        fieldtexture = new ResourceLocation(FramezAddon.MODID, "textures/blocks/fieldtexture.png");
        objModelLocation = new ResourceLocation(FramezAddon.MODID, "models/framefield.obj");
        this.modelpiston = (WavefrontObject) AdvancedModelLoader.loadModel(objModelLocation);
        this.scale = 0.5;
    }
    
    protected double[] directionRotate(int meta){
        switch(meta){
            //Down
            case 0: return new double[] {180, 1, 0, 0};
            //North
            case 2: return new double[] {-90, 1, 0, 0};
            //South
            case 3: return new double[] {90, 1, 0, 0};
            //West
            case 4: return new double[] {90, 0, 0, 1};
            //East
            case 5: return new double[] {-90, 0, 0, 1};
            default: return new double[] {0,0,0,0};
        }
    }
    
    private void renderTranslater(ResourceLocation framemap){
        GL11.glPushMatrix();
        this.bindTexture(framemap);
        modelpiston.renderPart("innerframe");
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        this.bindTexture(this.fieldtexture);
        modelpiston.renderPart("top");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        this.bindTexture(framemap);
        modelpiston.renderPart("outterframe");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        this.bindTexture(this.fieldtexture);
        modelpiston.renderPart("machine");
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderTileEntityAt(TileEntity ti, double x, double y, double z, float timesincelasttick) {
        TileFrameTranslater tile = (TileFrameTranslater) ti;
        ResourceLocation framemap = super.getFrame(tile.getMaterialType());
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor3d(1.0, 1.0, 1.0);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glScaled(scale, scale, scale);
        double[] dir = this.directionRotate(tile.getFace());
        GL11.glRotated(dir[0], dir[1], dir[2], dir[3]);
        this.renderTranslater(framemap);
        GL11.glPopMatrix();
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
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        this.renderTranslater(super.getFrame(item.hasTagCompound() ? item.getTagCompound().getString("frame_material") : "wood"));
        GL11.glPopMatrix();
    }
    
}
