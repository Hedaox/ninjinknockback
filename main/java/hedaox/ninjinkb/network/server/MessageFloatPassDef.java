package hedaox.ninjinkb.network.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import hedaox.ninjinkb.statsInfos.StatsInfos;
import io.netty.buffer.ByteBuf;

public class MessageFloatPassDef implements IMessage {

      private float toSend;      
    
      public MessageFloatPassDef(){}
      
      public MessageFloatPassDef(float toSend) {
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

      public static class MyMessageHandler implements IMessageHandler<MessageFloatPassDef, IMessage> {

          @Override
          public IMessage onMessage(MessageFloatPassDef message, MessageContext ctx) {
              
              StatsInfos.getPassDefPlayerMap().put(ctx.getServerHandler().playerEntity.getUniqueID(),message.toSend);
    
              return null;
          }
      }

}
