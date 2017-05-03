/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farincorporated.frameutils.client.packet;

import com.farincorporated.frameutils.tile.TileFrameTranslater;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 *
 * @author coolestbean
 */
public class FrameFieldPacket implements IMessage {

    public String type;
    public int x;
    public int y;
    public int z;
    
    public FrameFieldPacket() {}
    
    public FrameFieldPacket(String type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = ByteBufUtils.readUTF8String(buf);
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, type);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
    
    public static class FrameFieldPacketHandler implements IMessageHandler<FrameFieldPacket, IMessage>{
        
        @Override
        public IMessage onMessage(FrameFieldPacket message, MessageContext ctx) {
            TileFrameTranslater tile = (TileFrameTranslater) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
            tile.changeField(message.type);
            ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
            return null;
        }
        
    }
    
}
