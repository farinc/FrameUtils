package com.farincorporated.frameutils;

import com.farincorporated.frameutils.items.FocusedEnderlocatePlate;
import com.farincorporated.frameutils.handlers.FrameTranslaterStructureHandler;
import com.farincorporated.frameutils.handlers.FrameTranslaterMultiBlockHandler;
import com.farincorporated.frameutils.blocks.FrameTranslater;
import com.farincorporated.frameutils.tile.TileFramePiston;
import com.farincorporated.frameutils.tile.TileFramePistonExt;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezCreativeTab;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.framez.ref.ModInfo;
import com.farincorporated.frameutils.blocks.FramePiston;
import com.farincorporated.frameutils.blocks.FramePistonExt;
import com.farincorporated.frameutils.client.CommonProxy;
import com.farincorporated.frameutils.client.packet.FrameFieldPacket;
import com.farincorporated.frameutils.tile.TileFrameTranslater;
import com.google.common.collect.Maps;
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
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
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
    
    public static final Map<String, ItemStack> pistons = Maps.newHashMap();
    public static final Map<String, ItemStack> translaters = Maps.newHashMap();
    private static CreativeTabs tab;
    
    @Instance(FramezAddon.MODID)
    public static FramezAddon instance;
    
    @SidedProxy(clientSide = "com.farincorporated.frameutils.client.ClientProxy", serverSide = "com.farincorporated.frameutils.client.ServerProxy")
    public static CommonProxy proxy;
    
    private static class FrameUtilsCreativeTab extends CreativeTabs {

        public FrameUtilsCreativeTab(String lable) {
            super(lable);
        }
        
        @Override 
        public Item getTabIconItem(){
            return FramezAddon.enderplate;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public String getTranslatedTabLabel()
        {
            return this.getTabLabel();
        }
        
        @Override
        public void displayAllReleventItems(List list){
            for(Map.Entry<String, ItemStack> entry : pistons.entrySet()){
                list.add(entry.getValue());
            }
            for(Map.Entry<String, ItemStack> entry : translaters.entrySet()){
                list.add(entry.getValue());
            }
            list.add(new ItemStack(enderplate));
        }
    };
    
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
        enderplate = new FocusedEnderlocatePlate().setUnlocalizedName("enderplate").setCreativeTab(tab);
        
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
        this.createBlocks();
        tab = new FrameUtilsCreativeTab(NAME);
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
        
        //enderplate
        
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
        
        //pisotons and translaters
        
        BlockFrame frame;
        ItemStack outputpiston;
        ItemStack outputfield;
        
        for (Map.Entry<String, ItemStack> entry : pistons.entrySet()) {
            String mat = entry.getKey();
            outputpiston = entry.getValue();
            frame = FramezBlocks.frames.get(FrameFactory.getIdentifier("frame0", ModifierRegistry.instance.findFrameMaterial(mat)));
            
            GameRegistry.addShapedRecipe(outputpiston,
                " A ",
                "ABA",
                "RAR",
                'A', frame,
                'B', Block.blockRegistry.getObject("sticky_piston"),
                'R', Item.itemRegistry.getObject("redstone")
            );
        }
        for (Map.Entry<String, ItemStack> entry : translaters.entrySet()) {
            String mat = entry.getKey();
            outputfield = entry.getValue();
            frame = FramezBlocks.frames.get(FrameFactory.getIdentifier("frame0", ModifierRegistry.instance.findFrameMaterial(mat)));
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
    
    private void createBlocks(){
        ItemStack itempiston;
        ItemStack itemtranslater;
        for(IFrameMaterial mat : ModifierRegistry.instance.frameMaterials){
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("frame_material", mat.getType());
            itempiston = new ItemStack(framepiston);
            itempiston.setTagCompound(tag);
            itemtranslater = new ItemStack(frametranslater);
            itemtranslater.setTagCompound(tag);
            
            pistons.put(mat.getType(), itempiston);
            translaters.put(mat.getType(), itemtranslater);
            
        }
    }
}
