package hedaox.ninjinkb.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import hedaox.ninjinkb.event.EventAttackManager;
import net.minecraftforge.common.MinecraftForge;

/**
 * Do Things on server side
 * 
 * @author Hedaox
 */

public class ServerProxy extends CommonProxy{

    EventAttackManager EAHandler = new EventAttackManager();

    public void preInit(FMLPreInitializationEvent $e)
    {
        MinecraftForge.EVENT_BUS.register(EAHandler);

    	super.preInit($e);

    }
    public void init(FMLInitializationEvent $e)
    {
    	super.init($e);
    	
    }
    
    public void postInit(FMLPostInitializationEvent $e)
    {
    	super.postInit($e);
    	
    }
}
