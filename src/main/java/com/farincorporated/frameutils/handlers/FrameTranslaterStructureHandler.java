/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.handlers;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IStructureMovementHandler;
import com.amadornes.framez.api.movement.MovementIssue;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.farincorporated.frameutils.tile.TileFrameTranslater;
import java.util.Set;
import net.minecraft.tileentity.TileEntity;

/**
 *
 * @author coolestbean
 */
public class FrameTranslaterStructureHandler implements IStructureMovementHandler {

    public FrameTranslaterStructureHandler() {
    }

    @Override
    public void addIssues(BlockSet bs, IMovement im, Set<MovementIssue> set) {
        
        for(IMovingBlock b : bs){
            TileEntity ti = b.getTileEntity();
            if(ti instanceof TileFrameTranslater){
                TileFrameTranslater tile = (TileFrameTranslater) ti;
                tile.updateEntity();
                if(!tile.getCanMove()){
                    set.add(tile.getCalledIssue());
                    tile.reset();
                }
                tile.clearList();
            }
        }
    }
    
}
