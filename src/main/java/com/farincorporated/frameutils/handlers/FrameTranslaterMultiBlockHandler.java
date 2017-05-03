/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.handlers;

import com.amadornes.framez.api.movement.IMultiblockMovementHandler;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.farincorporated.frameutils.tile.TileFrameTranslater;
import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 *
 * @author coolestbean
 */
public class FrameTranslaterMultiBlockHandler implements IMultiblockMovementHandler {

    public FrameTranslaterMultiBlockHandler() {
    }

    @Override
    public void addBlocks(World world, BlockSet bs) {
        for(IMovingBlock b : bs){
            TileEntity ti = b.getTileEntity();
            if(ti instanceof TileFrameTranslater){
                TileFrameTranslater tile = (TileFrameTranslater) ti;
                tile.updateEntity();
                if(tile.getCanMove()){
                    List<BlockPos> list = tile.getPositions();
                    for(BlockPos pos : list){
                        bs.add(pos);
                    }
                    tile.useEnergy();
                }
                tile.clearList();
            }
        }
    }
    
}
