package hedaox.ninjinkb.network.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import hedaox.ninjinkb.statsInfos.StatsInfos;
import io.netty.buffer.ByteBuf;

public class MessageFloatSpirit implements IMessage {

    private float toSend;      
  
    public MessageFloatSpirit(){}
    
    public MessageFloatSpirit(float toSend) {
      this.toSend = toSend;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(toSend);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        toSend = buf.readFloat();
    }

    public static class MyMessageHandler implements IMessageHandler<MessageFloatSpirit, IMessage> {

        @Override
        public IMessage onMessage(MessageFloatSpirit message, MessageContext ctx) {
            
            StatsInfos.getSpiritPlayerMap().put(ctx.getServerHandler().playerEntity.getUniqueID(),message.toSend);
  
            return null;
        }
    }

}