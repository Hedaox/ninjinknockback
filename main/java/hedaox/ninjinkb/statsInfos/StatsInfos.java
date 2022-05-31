package hedaox.ninjinkb.statsInfos;

import JinRyuu.DragonBC.common.DBCKiTech;
import JinRyuu.JRMCore.JRMCoreConfig;
import JinRyuu.JRMCore.JRMCoreH;
import JinRyuu.JRMCore.JRMCoreKeyHandler;
import JinRyuu.JRMCore.i.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import hedaox.ninjinkb.network.server.*;
import hedaox.ninjinkb.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.Sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The purpose of this class is to send packets to do Client-Serv and Serv-Client sync
 *
 * @author Hedaox
 */

public class StatsInfos {

    int slowEvent = 0;
    private List<EntityPlayer> listPlayers;

    // Hash-Map de caractéristiques joueur à envoyé au serveur
    private static Map<UUID, Float> MeleeDmgPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Float> PassDefPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Float> SpiritPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Float> WillPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Integer> ConstPlayerMap = new HashMap<UUID, Integer>();
    private static Map<UUID, Boolean> isKiChargingPlayerMap = new HashMap<UUID, Boolean>();
    private static Map<UUID, Boolean> isFlyingPlayerMap = new HashMap<UUID, Boolean>();

    public static Map<UUID, Float> getMeleeDmgPlayerMap() {
        return MeleeDmgPlayerMap;
    }

    public static Map<UUID, Float> getPassDefPlayerMap() {
        return PassDefPlayerMap;
    }

    public static Map<UUID, Float> getSpiritPlayerMap() {
        return SpiritPlayerMap;
    }

    public static Map<UUID, Float> getWillPlayerMap() {
        return WillPlayerMap;
    }

    public static Map<UUID, Integer> getConstPlayerMap() {
        return ConstPlayerMap;
    }

    public static Map<UUID, Boolean> getIsKiChargingPlayerMap() {
        return isKiChargingPlayerMap;
    }

    public static Map<UUID, Boolean> getIsFlyingPlayerMap() {
        return isFlyingPlayerMap;
    }

    // Hash-Map of player characteristics to be sent to other clients
    private static Map<UUID, Float> MeleeDmgPlayerMapC = new HashMap<UUID, Float>();
    private static Map<UUID, Float> PassDefPlayerMapC = new HashMap<UUID, Float>();
    private static Map<UUID, Integer> ConstPlayerMapC = new HashMap<UUID, Integer>();

    /**
     * This Function updates with each client-side player tick. It recovers the characteristics of the
     * player then sends a packet to the server which places the characteristics in the corresponding hash maps.
     *
     * @param event
     * @author Hedaox
     */

    @SubscribeEvent
    public void updateEventOnPlayerTick(PlayerTickEvent event) {
        //is Client side ?
        if (event.player.worldObj.isRemote && event.player.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {

            EntityPlayer player = event.player;
            float dmg = getMeleeDamage(player);
            float passDef = getPassiveDefense(player);
            float spirit = JRMCoreH.maxEnergy;
            float will = getKiPower(player);
            Boolean isFlying = DBCKiTech.floating;

            CommonProxy.network.sendToServer(new MessageFloatMeleeDmg(dmg));
            CommonProxy.network.sendToServer(new MessageFloatPassDef(passDef));
            CommonProxy.network.sendToServer(new MessageFloatSpirit(spirit));
            CommonProxy.network.sendToServer(new MessageFloatWill(will));
            CommonProxy.network.sendToServer(new MessageIntConst(JRMCoreH.maxBody));
            CommonProxy.network.sendToServer(new MessageBooleanIsKiCharging(JRMCoreKeyHandler.KiCharge.getIsKeyPressed() || (JRMCoreKeyHandler.KiAscend.getIsKeyPressed() && JRMCoreH.TransSaiCurRg > 0)));
            CommonProxy.network.sendToServer(new MessageBooleanIsFlying(isFlying));
        }
    }

    /**
     * Recovers the player's passive defense.
     *
     * @param player
     * @author Hedaox
     */
    public float getPassiveDefense(EntityPlayer player) {
        if ((JRMCoreH.Pwrtyp != 3) && (JRMCoreH.Pwrtyp > 0)) {
            int DEX = JRMCoreH.TransPwrModAtr(JRMCoreH.PlyrAttrbts, 1, JRMCoreH.State, JRMCoreH.State2, JRMCoreH.Race, JRMCoreH.PlyrSkillX, JRMCoreH.curRelease, JRMCoreH.getArcRsrv(), JRMCoreH.StusEfctsMe(14), JRMCoreH.StusEfctsMe(12), JRMCoreH.StusEfctsMe(13), JRMCoreH.StusEfctsMe(19), JRMCoreH.Pwrtyp, JRMCoreH.PlyrSkills, (JRMCoreH.StusEfctsMe(10)) || (JRMCoreH.StusEfctsMe(11)));
            int SPI = JRMCoreH.PlyrAttrbts[5];
            int statSPI = JRMCoreH.stat(JRMCoreH.Pwrtyp, 5, SPI, JRMCoreH.Race, JRMCoreH.Class, JRMCoreH.SklLvl_KiBs(JRMCoreH.Pwrtyp));
            int def = JRMCoreH.stat(JRMCoreH.Pwrtyp, 1, DEX, JRMCoreH.Race, JRMCoreH.Class, 0.0F);
            double curAtr = (def * 0.01D * JRMCoreH.curRelease * JRMCoreH.weightPerc(1, player));

            if (!JRMCoreH.getBonusAttributes(1).equals("n")) {
                String attributeBonusStr = JRMCoreH.getBonusAttributes(1);
                for (String attributeStr : attributeBonusStr.split("\\|")) {
                    if (attributeStr.split(";").length == 2) {
                        curAtr *= Float.valueOf(attributeStr.split(";")[1].replaceAll("\\*", ""));
                    }
                }
            }

            //Si le joueur bloque
            ExtendedPlayer props = ExtendedPlayer.get(player);
            if (props.getBlocking() == 1) {

                if (!JRMCoreH.PlyrSettingsB(10)) {
                    return (float) (curAtr + ((JRMCoreH.SklLvl(11) * 0.005D * statSPI * JRMCoreH.curRelease * 0.01D)));
                }
                return (float) (curAtr);
            } else {
                if (!JRMCoreH.PlyrSettingsB(10)) {
                    return (float) (curAtr * JRMCoreConfig.StatPasDef / 100 + ((JRMCoreH.SklLvl(11) * 0.005D * statSPI * JRMCoreH.curRelease * 0.01D)));
                }
                return (float) (curAtr) * JRMCoreConfig.StatPasDef / 100;
            }
        }
        return 0.0F;
    }

    /**
     * Recovers the player's melee damage.
     *
     * @param player
     * @author Hedaox
     */
    public float getMeleeDamage(EntityPlayer player) {
        //is Client side ?
        if (player.worldObj.isRemote) {
            if ((JRMCoreH.Pwrtyp != 3) && (JRMCoreH.Pwrtyp > 0)) {
                int STR = JRMCoreH.TransPwrModAtr(JRMCoreH.PlyrAttrbts, 0, JRMCoreH.State, JRMCoreH.State2, JRMCoreH.Race, JRMCoreH.PlyrSkillX, JRMCoreH.curRelease, JRMCoreH.getArcRsrv(), JRMCoreH.StusEfctsMe(14), JRMCoreH.StusEfctsMe(12), JRMCoreH.StusEfctsMe(13), JRMCoreH.StusEfctsMe(19), JRMCoreH.Pwrtyp, JRMCoreH.PlyrSkills, (JRMCoreH.StusEfctsMe(10)) || (JRMCoreH.StusEfctsMe(11)));
                int SPI = JRMCoreH.PlyrAttrbts[5];
                int statSPI = JRMCoreH.stat(JRMCoreH.Pwrtyp, 5, SPI, JRMCoreH.Race, JRMCoreH.Class, JRMCoreH.SklLvl_KiBs(JRMCoreH.Pwrtyp));
                int dmg = JRMCoreH.stat(JRMCoreH.Pwrtyp, 0, STR, JRMCoreH.Race, JRMCoreH.Class, 0.0F);
                double curAtr = dmg * JRMCoreH.curRelease * 0.01D * JRMCoreH.weightPerc(0, player);

                if (!JRMCoreH.getBonusAttributes(0).equals("n")) {
                    String attributeBonusStr = JRMCoreH.getBonusAttributes(0);
                    for (String attributeStr : attributeBonusStr.split("\\|")) {
                        if (attributeStr.split(";").length == 2) {
                            curAtr *= Float.valueOf(attributeStr.split(";")[1].replaceAll("\\*", ""));
                        }
                    }
                }

                if (!JRMCoreH.PlyrSettingsB(9)) {
                    return (float) (curAtr + ((JRMCoreH.SklLvl(12) * 0.0025D * statSPI * JRMCoreH.curRelease * 0.01D)));
                }
                return (float) (curAtr);
            }
        }
        return 2.0F;
    }

    /**
     * Recover the player's ki power.
     *
     * @param player
     * @author Hedaox
     */
    public float getKiPower(EntityPlayer player) {
        //is Client side ?
        if (player.worldObj.isRemote) {
            if ((JRMCoreH.Pwrtyp != 3) && (JRMCoreH.Pwrtyp > 0)) {
                int WIL = JRMCoreH.TransPwrModAtr(JRMCoreH.PlyrAttrbts, 3, JRMCoreH.State, JRMCoreH.State2, JRMCoreH.Race, JRMCoreH.PlyrSkillX, JRMCoreH.curRelease, JRMCoreH.getArcRsrv(), JRMCoreH.StusEfctsMe(14), JRMCoreH.StusEfctsMe(12), JRMCoreH.StusEfctsMe(13), JRMCoreH.StusEfctsMe(19), JRMCoreH.Pwrtyp, JRMCoreH.PlyrSkills, (JRMCoreH.StusEfctsMe(10)) || (JRMCoreH.StusEfctsMe(11)));
                int statWIL = JRMCoreH.stat(JRMCoreH.Pwrtyp, 4, WIL, JRMCoreH.Race, JRMCoreH.Class, JRMCoreH.SklLvl_KiBs(JRMCoreH.Pwrtyp));

                if (!JRMCoreH.getBonusAttributes(3).equals("n")) {
                    String attributeBonusStr = JRMCoreH.getBonusAttributes(3);
                    for (String attributeStr : attributeBonusStr.split("\\|")) {
                        if (attributeStr.split(";").length == 2) {
                            statWIL *= Float.valueOf(attributeStr.split(";")[1].replaceAll("\\*", ""));
                        }
                    }
                }

                return (float) (statWIL * 0.01D * JRMCoreH.curRelease);
            }
        }
        return 0.0F;
    }
}
