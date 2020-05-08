package hedaox.ninjinkb.network.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import hedaox.ninjinkb.statsInfos.StatsInfos;
import io.netty.buffer.ByteBuf;

public class MessageIntConst implements IMessage {

	  int toSend;	  
	
	  public MessageIntConst(){}
	  
	  public MessageIntConst(int toSend) {
	    this.toSend = toSend;
	  }

	  @Override 
	  public void toBytes(ByteBuf buf) {
		  buf.writeInt(toSend);
	  }

	  @Override 
	  public void fromBytes(ByteBuf buf) 
	  {
		  toSend = buf.readInt();
	  }

	  public static class MyMessageHandler implements IMessageHandler<MessageIntConst, IMessage> {

		  @Override 
		  public IMessage onMessage(MessageIntConst message, MessageContext ctx) {
			  
			  StatsInfos.getConstPlayerMap().put(ctx.getServerHandler().playerEntity.getUniqueID(),message.toSend);;
	
			  return null;
		  }
	  }
}

