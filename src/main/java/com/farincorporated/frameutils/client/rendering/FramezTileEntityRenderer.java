/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.client.rendering;

import com.farincorporated.frameutils.FramezAddon;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author coolestbean
 */
public abstract class FramezTileEntityRenderer extends TileEntitySpecialRenderer{
    protected final Map<String, ResourceLocation> frametextures;
    
    public FramezTileEntityRenderer(){
        frametextures = new HashMap();
        frametextures.put("wood", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/wood.png")); 
        frametextures.put("iron", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/iron.png"));
        frametextures.put("gold", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/gold.png"));
        frametextures.put("copper", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/copper.png"));
        frametextures.put("tin", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/tin.png"));
        frametextures.put("bronze", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/bronze.png"));
        frametextures.put("electrum", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/electrum.png"));
        frametextures.put("invar", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/invar.png"));
        frametextures.put("silver", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/silver.png"));
        frametextures.put("enderium", new ResourceLocation(FramezAddon.FRAMEZMODID, "textures/blocks/frame/enderium.png"));
    }
    
    protected final ResourceLocation getFrame(String frame){
        ResourceLocation res = this.frametextures.get(frame);
        return res != null ? res : this.frametextures.get("wood");
    }
}

