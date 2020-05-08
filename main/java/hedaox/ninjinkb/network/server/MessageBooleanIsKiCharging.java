package hedaox.ninjinkb.network.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import hedaox.ninjinkb.statsInfos.StatsInfos;
import io.netty.buffer.ByteBuf;

public class MessageBooleanIsKiCharging implements IMessage {

    private Boolean toSend;      
    
    public MessageBooleanIsKiCharging(){}
    
    public MessageBooleanIsKiCharging(Boolean toSend) {
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

    public static class MyMessageHandler implements IMessageHandler<MessageBooleanIsKiCharging, IMessage> {

        @Override
        public IMessage onMessage(MessageBooleanIsKiCharging message, MessageContext ctx) {
            
            StatsInfos.getIsKiChargingPlayerMap().put(ctx.getServerHandler().playerEntity.getUniqueID(),message.toSend);
  
            return null;
        }
    }

}
