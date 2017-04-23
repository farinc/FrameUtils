/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.framezaddon.client;

import com.farincorporated.framezaddon.client.gui.GUIFrameField;
import com.farincorporated.framezaddon.FramezAddon;
import static com.farincorporated.framezaddon.FramezAddon.enderplate;
import static com.farincorporated.framezaddon.FramezAddon.framepiston;
import static com.farincorporated.framezaddon.FramezAddon.frametranslater;
import com.farincorporated.framezaddon.client.rendering.FramePistonRenderer;
import com.farincorporated.framezaddon.client.rendering.FrameTranslaterRenderer;
import com.farincorporated.framezaddon.tile.TileFramePiston;
import com.farincorporated.framezaddon.tile.TileFrameTranslater;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 *
 * @author coolestbean
 */
public class ClientProxy extends CommonProxy implements IGuiHandler{
    
    @Override
    public void preinit(FMLPreInitializationEvent event) {
        super.preinit(event);
        LanguageRegistry.addName(framepiston, "Frame Piston");
        LanguageRegistry.addName(frametranslater, "Frame Translater");
        LanguageRegistry.addName(enderplate, "Focused Enderlocate Plate");
        
        //renderers
        FramePistonRenderer pistonrenderer = new FramePistonRenderer();
        FrameTranslaterRenderer translaterenderer = new FrameTranslaterRenderer();
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileFramePiston.class, pistonrenderer);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(framepiston), pistonrenderer);
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileFrameTranslater.class, translaterenderer);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(frametranslater), translaterenderer);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        NetworkRegistry.INSTANCE.registerGuiHandler(FramezAddon.instance, this);
    }
    
    @Override
    public void postinit(FMLPostInitializationEvent event){
        super.postinit(event);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID){
            case 0:
                return new GUIFrameField(world, x, y, z);
            default:
                return null;
        }
    }
}