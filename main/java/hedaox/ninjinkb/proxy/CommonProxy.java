package hedaox.ninjinkb.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import hedaox.ninjinkb.config.Config;
import hedaox.ninjinkb.event.EventAttackManager;
import hedaox.ninjinkb.knockbackPlus.EnergyKnockback;
import hedaox.ninjinkb.lib.ModVars;
import hedaox.ninjinkb.network.server.*;
import hedaox.ninjinkb.statsInfos.StatsInfos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Do Things on server and client side at the same time
 *
 * @author Hedaox
 */
public class CommonProxy {

    EventAttackManager EAHandler = new EventAttackManager();

    EnergyKnockback EKHandler = new EnergyKnockback();

    StatsInfos SPHandler = new StatsInfos();

    // Config instance
    public static Configuration config;

    public static SimpleNetworkWrapper network;

    public void preInit(FMLPreInitializationEvent $e) {
        MinecraftForge.EVENT_BUS.register(EAHandler);

        FMLCommonHandler.instance().bus().register(EKHandler);

        FMLCommonHandler.instance().bus().register(SPHandler);

        File directory = $e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "ninjinknockback.cfg"));
        Config.readConfig();

        network = NetworkRegistry.INSTANCE.newSimpleChannel(ModVars.MOD_ID + " Channel 1");

        //Server Side packet
        network.registerMessage(MessageFloatMeleeDmg.MyMessageHandler.class, MessageFloatMeleeDmg.class, 0, Side.SERVER);
        network.registerMessage(MessageFloatPassDef.MyMessageHandler.class, MessageFloatPassDef.class, 1, Side.SERVER);
        network.registerMessage(MessageFloatSpirit.MyMessageHandler.class, MessageFloatSpirit.class, 2, Side.SERVER);
        network.registerMessage(MessageFloatWill.MyMessageHandler.class, MessageFloatWill.class, 3, Side.SERVER);
        network.registerMessage(MessageIntConst.MyMessageHandler.class, MessageIntConst.class, 4, Side.SERVER);
        network.registerMessage(MessageBooleanIsKiCharging.MyMessageHandler.class, MessageBooleanIsKiCharging.class, 5, Side.SERVER);
        network.registerMessage(MessageBooleanIsFlying.MyMessageHandler.class, MessageBooleanIsFlying.class, 6, Side.SERVER);

    }

    public void init(FMLInitializationEvent $e) {

    }

    public void postInit(FMLPostInitializationEvent $e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
}


