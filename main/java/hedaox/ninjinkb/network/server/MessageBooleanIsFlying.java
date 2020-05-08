package hedaox.ninjinkb.network.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import hedaox.ninjinkb.statsInfos.StatsInfos;
import io.netty.buffer.ByteBuf;

public class MessageBooleanIsFlying implements IMessage {

    private Boolean toSend;      
    
    public MessageBooleanIsFlying(){}
    
    public MessageBooleanIsFlying(Boolean toSend) {
      this.toSend = toSend;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(toSend);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        toSend = buf.readBoolean();
    }

    public static class MyMessageHandler implements IMessageHandler<MessageBooleanIsFlying, IMessage> {

        @Override
        public IMessage onMessage(MessageBooleanIsFlying message, MessageContext ctx) {
            
            StatsInfos.getIsFlyingPlayerMap().put(ctx.getServerHandler().playerEntity.getUniqueID(),message.toSend);
  
            return null;
        }
    }

}
