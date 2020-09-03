package hedaox.ninjinkb.statsInfos;

import JinRyuu.DragonBC.common.DBCKiTech;
import JinRyuu.JRMCore.JRMCoreConfig;
import JinRyuu.JRMCore.JRMCoreH;
import JinRyuu.JRMCore.JRMCoreHJYC;
import JinRyuu.JRMCore.JRMCoreKeyHandler;
import JinRyuu.JRMCore.i.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import hedaox.ninjinkb.network.server.*;
import hedaox.ninjinkb.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.*;

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
	private static Map<UUID, Float> AgePlayerMap = new HashMap<UUID, Float>();
	private static Map<UUID, Byte> StatePlayerMap = new HashMap<UUID, Byte>();
	private static Map<UUID, Integer> GenderPlayerMap = new HashMap<UUID, Integer>();
	private static Map<UUID, Byte> AlignPlayerMap = new HashMap<UUID, Byte>();
	private static Map<UUID, String> TextureBloodPlayerMap = new HashMap<UUID, String>();
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

	public static Map<UUID, Float> getAgePlayerMap() {
		return AgePlayerMap;
	}

	public static Map<UUID, Byte> getStatePlayerMap() {
		return StatePlayerMap;
	}

	public static Map<UUID, Integer> getGenderPlayerMap() {
		return GenderPlayerMap;
	}

	public static Map<UUID, Byte> getAlignPlayerMap() {
		return AlignPlayerMap;
	}

	public static Map<UUID, String> getTextureBloodPlayerMap() {
		return TextureBloodPlayerMap;
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

	public static Map<UUID, Float> getMeleeDmgPlayerMapC() {
		return MeleeDmgPlayerMapC;
	}

	public static Map<UUID, Float> getPassDefPlayerMapC() {
		return PassDefPlayerMapC;
	}

	public static Map<UUID, Integer> getConstPlayerMapC() {
		return ConstPlayerMapC;
	}

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
		if (event.player.worldObj.isRemote) {

			EntityPlayer player = event.player;
			float dmg = getMeleeDamage(player);
			float passDef = getPassiveDefense(player);
			float spirit = JRMCoreH.maxEnergy;
			float will = getKiPower(player);
			float age = JRMCoreHJYC.JYCAge(player);
			int state = JRMCoreH.State;
			int gender = JRMCoreH.dnsGender(JRMCoreH.dns);
			int race = JRMCoreH.Race;
			int align = JRMCoreH.align;
			Boolean isFlying = DBCKiTech.floating;

			// System.out.println("meleeDmg : " + dmg);
			// System.out.println("passDef : " + passDef);
			// System.out.println("will : " + will);
			// System.out.println("age : " + age);
			// System.out.println("current stam : " + JRMCoreH.curStamina);
			// System.out.println("max stam : " + JRMCoreH.maxStamina);
			// System.out.println("max health :" + JRMCoreH.maxBody);
			// System.out.println("health :" + JRMCoreH.curBody);
			// System.out.println("align :" + JRMCoreH.align);
			// System.out.println("JYearcAge :" + JRMCoreHJYC.JYCAge(player));
			// System.out.println("TextureBlood : " + textureBlood);
			// System.out.println("isFlying : " + isFlying);
			}*/

			if (JRMCoreH.curStamina < JRMCoreH.maxStamina) {
				JRMCoreH.curStamina += JRMCoreH.maxStamina / 10;
			}

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
	 * This Function is updated with each tick of the server. It sends for each player the characteristics
	 * from each player to the customers. Customers then place this data in hash maps ending in C.
	 *
	 * @param event
	 * @author Hedaox
	 */

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void updateEventOnServerTick(ServerTickEvent event)
			throws NullPointerException {
		//is Server side ?
		if (event.side.isServer()) {

			slowEvent++;
			if (event.side.isServer()
					&& slowEvent > 75) {

				this.listPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
				for (EntityPlayer loadedPlayer : this.listPlayers) {
					//Put the stamina to the max
					int[] PlyrAttrbts = JRMCoreH.PlyrAttrbts(loadedPlayer);
					byte pwr = JRMCoreH.getByte(loadedPlayer, "jrmcPwrtyp");
					byte rce = JRMCoreH.getByte(loadedPlayer, "jrmcRace");
					byte cls = JRMCoreH.getByte(loadedPlayer, "jrmcClass");
					int maxEnergy = JRMCoreH.stat(pwr, 5, PlyrAttrbts[5], rce, cls, JRMCoreH.SklLvl_KiBs(loadedPlayer, pwr));
					JRMCoreH.setInt(maxEnergy, loadedPlayer, "jrmcStamina");

					//Unique Player ID
					UUID playerUniqueId = loadedPlayer.getUniqueID();

					if (!getConstPlayerMap().isEmpty() && getConstPlayerMap().get(playerUniqueId) != null) {


						//String dmg = playerUniqueId.toString() + "#" + getMeleeDmgPlayerMap().get(playerUniqueId);
						//String passDef = playerUniqueId.toString() + "#" + getPassDefPlayerMap().get(playerUniqueId);
						//String constPlayer = playerUniqueId.toString() + "#" + getConstPlayerMap().get(playerUniqueId);
						//String healthPlayer = playerUniqueId.toString() + "#" + getHealthPlayerMap().get(playerUniqueId);
						String age = playerUniqueId.toString() + "#" + getAgePlayerMap().get(playerUniqueId);
						String state = playerUniqueId.toString() + "#" + getStatePlayerMap().get(playerUniqueId);
						String gender = playerUniqueId.toString() + "#" + getGenderPlayerMap().get(playerUniqueId);
						String align = playerUniqueId.toString() + "#" + getAlignPlayerMap().get(playerUniqueId);
						//String race = playerUniqueId.toString() + "#" + getRacePlayerMap().get(playerUniqueId);
						String textureBlood = playerUniqueId.toString() + "#" + getTextureBloodPlayerMap().get(playerUniqueId);
						//String isSafeZone = playerUniqueId.toString() + "#" + atLeastInOneAreaZone;

						// TargetPoint point = new TargetPoint(loadedPlayer.dimension, loadedPlayer.chunkCoordX, loadedPlayer.chunkCoordY, loadedPlayer.chunkCoordZ, 32);

						// ServerProxy.network.sendToAll(new MessageStringUUIDMeleeDmgC(dmg));
						// ServerProxy.network.sendToAll(new MessageStringUUIDPassDefC(passDef));
						// ServerProxy.network.sendToAll(new MessageStringUUIDConstC(constPlayer));
						// ServerProxy.network.sendToAll(new MessageStringUUIDHealthC(healthPlayer));
						// ServerProxy.network.sendToAll(new MessageStringUUIDRaceC(race));
						//ServerProxy.network.sendToAll(new MessageStringUUIDIsSafeZoneC(isSafeZone));

						slowEvent = 0;

						//here to test by sending a debug message from the server to the client

						/*MinecraftServer
								.getServer()
								.getCommandManager()
								.executeCommand(
										MinecraftServer.getServer(),
										"say Name :"
												+ loadedPlayer.getDisplayName());
						MinecraftServer
								.getServer()
								.getCommandManager()
								.executeCommand(MinecraftServer.getServer(),
										"say isSafeZone :" + isSafeZone);
						MinecraftServer
								.getServer()
								.getCommandManager()
								.executeCommand(MinecraftServer.getServer(),
										"say --------------------------- ");*/

					}
				}
			}
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

				return (float) (statWIL * 0.01D * JRMCoreH.curRelease);
			}
		}
		return 0.0F;
	}
}
