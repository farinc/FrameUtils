package com.farincorporated.framezaddon;

import com.farincorporated.items.FocusedEnderlocatePlate;
import com.farincorporated.framezaddon.handlers.FrameTranslaterStructureHandler;
import com.farincorporated.framezaddon.handlers.FrameTranslaterMultiBlockHandler;
import com.farincorporated.framezaddon.blocks.FrameTranslater;
import com.farincorporated.framezaddon.tile.TileFramePiston;
import com.farincorporated.framezaddon.tile.TileFramePistonExt;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.framez.ref.ModInfo;
import com.farincorporated.framezaddon.blocks.FramePiston;
import com.farincorporated.framezaddon.blocks.FramePistonExt;
import com.farincorporated.framezaddon.client.CommonProxy;
import com.farincorporated.framezaddon.client.packet.FrameFieldPacket;
import com.farincorporated.framezaddon.tile.TileFrameTranslater;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;


/**
 *
 * @author farincorporated
 */
@Mod(modid = FramezAddon.MODID, name = FramezAddon.NAME, version = FramezAddon.VERSION, dependencies="required-after:framez;after:CoFHCore;after:ThermalExpansion;after:ThermalFoundation")
public class FramezAddon {
    
    public static final String MODID = "frameutils";
    public static final String NAME = "Frame Utils";
    public static final String VERSION = "1.1";
    public static final String FRAMEZMODID = ModInfo.MODID;
    public static boolean isCofhLibloaded = false;
    public static boolean isThermalExpansionloaded = false;
    public static boolean isThermalFoundationloaded = false;
    
    @Instance(FramezAddon.MODID)
    public static FramezAddon instance;
    
    @SidedProxy(clientSide = "com.farincorporated.framezaddon.client.ClientProxy", serverSide = "com.farincorporated.framezaddon.client.ServerProxy")
    public static CommonProxy proxy;
    
    public static SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    
    public static Block framepiston;
    public static Block framepistonext;
    public static Block frametranslater;
    
    public static Item debugitem;
    public static Item enderplate;

    @EventHandler
    private void preinit(FMLPreInitializationEvent event){
        //declare
        framepiston = new FramePiston().setBlockName("framepiston");
        framepistonext = new FramePistonExt().setBlockName("framepistonext");
        frametranslater = new FrameTranslater().setBlockName("frametranslater");
        enderplate = new FocusedEnderlocatePlate().setUnlocalizedName("enderplate");
        
        GameRegistry.registerBlock(framepiston, "framepiston");
        GameRegistry.registerBlock(framepistonext, "framepistonext");
        GameRegistry.registerBlock(frametranslater, "frametranslater");
        
        GameRegistry.registerItem(enderplate, "enderplate");
               
        //tiles
        GameRegistry.registerTileEntity(TileFramePiston.class, "tileframepiston");
        GameRegistry.registerTileEntity(TileFramePistonExt.class, "tileframepistonext");
        GameRegistry.registerTileEntity(TileFrameTranslater.class, "tileframetranslater");
        
        isCofhLibloaded = Loader.isModLoaded("CoFHCore");
        isThermalExpansionloaded = Loader.isModLoaded("ThermalExpansion");
        isThermalFoundationloaded = Loader.isModLoaded("ThermalFoundation");
        
        proxy.preinit(event);
    }
    
    @EventHandler
    private void init(FMLInitializationEvent event){
        dispatcher.registerMessage(FrameFieldPacket.FrameFieldPacketHandler.class, FrameFieldPacket.class, 0, Side.SERVER);
        this.addRecipes();
        this.addFrameHandlers();
        proxy.init(event);
    }
    
    @EventHandler
    private void postInit(FMLPostInitializationEvent event){
        proxy.postinit(event);
    }

    private void addRecipes(){
        
        if(isThermalFoundationloaded){
            GameRegistry.addRecipe(new ShapedOreRecipe(enderplate,
                "ABA",
                "BEB",
                "ABA",
                'A', "ingotEnderium",
                'B', "ingotElectrum",
                'E', Item.itemRegistry.getObject("ender_pearl")
            ));
        }else{
            GameRegistry.addRecipe(new ItemStack(enderplate), 
                "ABA",
                "BEB",
                "ABA",
                'A', Item.itemRegistry.getObject("iron_ingot"),
                'B', Item.itemRegistry.getObject("gold_ingot"),
                'E', Item.itemRegistry.getObject("ender_pearl")
            );
        }
        
        BlockFrame frame;
        ItemStack outputpiston;
        ItemStack outputfield;
        for (IFrameMaterial mat : ModifierRegistry.instance.frameMaterials) {
            frame = FramezBlocks.frames.get(FrameFactory.getIdentifier("frame0", mat));
            outputpiston = new ItemStack(framepiston, 1);
            outputfield = new ItemStack(frametranslater, 1);
            
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("frame_material", mat.getType());
            outputpiston.setTagCompound(tag);
            
            GameRegistry.addShapedRecipe(outputpiston,
                " A ",
                "ABA",
                "RAR",
                'A', frame,
                'B', Block.blockRegistry.getObject("sticky_piston"),
                'R', Item.itemRegistry.getObject("redstone")
            );
            
            outputfield.setTagCompound(tag);
            GameRegistry.addShapedRecipe(outputfield,
                "AFA",
                "ROR",
                "ACA",
                'A', frame,
                'F', enderplate,
                'R', Item.itemRegistry.getObject("redstone"),
                'O', Block.blockRegistry.getObject("obsidian"),
                'C', isThermalExpansionloaded ? GameRegistry.findItemStack("ThermalExpansion", "capacitorResonant", 1) : Block.blockRegistry.getObject("redstone_block")
            );
        }
    }
    
    private void addFrameHandlers(){
        MovementRegistry.instance.registerMultiblockMovementHandler(new FrameTranslaterMultiBlockHandler());
        MovementRegistry.instance.registerStructureMovementHandler(new FrameTranslaterStructureHandler());
    }
}
