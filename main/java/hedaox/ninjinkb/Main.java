package hedaox.ninjinkb;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import hedaox.ninjinkb.lib.ModVars;
import hedaox.ninjinkb.proxy.CommonProxy;
/*
 * Main code
 */

@Mod(modid = ModVars.MOD_ID, name = ModVars.MOD_name)
public class Main {

    @Instance
    public static Main instance = new Main();

    @SidedProxy(clientSide = "hedaox.ninjinkb.proxy.ClientProxy", serverSide = "hedaox.ninjinkb.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent $e) {
        proxy.preInit($e);
    }

    @EventHandler
    public void init(FMLInitializationEvent $e) {
        proxy.init($e);

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent $e) {
        proxy.postInit($e);

    }
}