package hedaox.ninjinkb.knockbackPlus;

import JinRyuu.JRMCore.JRMCoreH;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import hedaox.ninjinkb.config.Config;
import hedaox.ninjinkb.statsInfos.StatsInfos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EnergyKnockback {

	private static Map<UUID, Boolean> KiChargingPlayerMap = new HashMap<UUID, Boolean>();
	private static Map<UUID, Boolean> FlyingPlayerMap = new HashMap<UUID, Boolean>();
	private static Map<UUID, Float> MeleeDmgPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Float> PassDefPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Float> WillPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Float> SpiritPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Integer> ConstPlayerMap = new HashMap<UUID, Integer>();
    
	private List<Entity> listEntities;
	private int inAreaZoneNumber = 0;
	private boolean playerInSafeZone = false;
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void updateEventOnPlayerTick(PlayerTickEvent event) {
		//is Server side ?
		if(!event.player.worldObj.isRemote) {

			KiChargingPlayerMap = StatsInfos.getIsKiChargingPlayerMap();
			MeleeDmgPlayerMap = StatsInfos.getMeleeDmgPlayerMap();
			PassDefPlayerMap = StatsInfos.getPassDefPlayerMap();
			WillPlayerMap = StatsInfos.getWillPlayerMap();
			SpiritPlayerMap = StatsInfos.getSpiritPlayerMap();
			ConstPlayerMap = StatsInfos.getConstPlayerMap();

			this.listEntities = event.player.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(event.player.posX - 60, event.player.posY - 60, event.player.posZ - 60, event.player.posX + 60, event.player.posY + 60, event.player.posZ + 60));

			if (KiChargingPlayerMap.get(event.player.getUniqueID()) != null && !playerInSafeZone) {
				if (KiChargingPlayerMap.get(event.player.getUniqueID())) {
					for (Entity entityToPush : listEntities) {
						if (entityToPush.getEntityId() != event.player.getEntityId() && (entityToPush instanceof EntityPlayerMP || entityToPush instanceof EntityLiving)) {
							intelligentEKB(event.player, entityToPush, caculEnergyKnockback(entityToPush, event.player));
						}
					}
				}
			}
		}
	}
	
    public void intelligentEKB(EntityPlayer playerPushing, Entity entityReceiving, double knockback)
    {
    	// If player is here (player leaving the server for example)
        if (playerPushing  != null)
        {
            double px = playerPushing.posX;
            double py = playerPushing.posY;
            double pz = playerPushing.posZ;
            
            double ex = entityReceiving.posX;
            double ey = entityReceiving.posY;
            double ez = entityReceiving.posZ;

            if(knockback >= 0.001D)
            {
		        Vec3 v = Vec3.createVectorHelper(ex - px, ey - py, ez - pz).normalize();
		        if(entityReceiving instanceof EntityPlayerMP && !((EntityPlayerMP) entityReceiving).capabilities.isCreativeMode)
		        {   
		        	if(FlyingPlayerMap.containsKey(entityReceiving.getUniqueID()) && FlyingPlayerMap.get(entityReceiving.getUniqueID()))
		        	{
		        		entityReceiving.motionY = v.yCoord*knockback;
		        	}
			        entityReceiving.addVelocity(v.xCoord*knockback, v.yCoord*knockback, v.zCoord*knockback);
		        	((EntityPlayerMP) entityReceiving).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entityReceiving));
		        }
		        else
		        {
		        	entityReceiving.addVelocity(v.xCoord*knockback, v.yCoord*knockback, v.zCoord*knockback);
		        }
            }
        }
    }
    
    public double caculEnergyKnockback(Entity entityBeingPushed, EntityPlayer playerPushing )
    {
    	double calculEnergyKnockBack = 0.0D;
    	double energyKnockBackReducer = 0.001D;
    	
    	float maxLifeEntityBeingPushed = 0.0f;
    	float passDefPlayerBeingPushed = 0.0f;
    	float kiPowerPlayerBeingPushed = 0.0f;
    	float kiPoolPlayerBeingPushed = 0.0f;
    	
    	float meleeDmgPlayerPushing = MeleeDmgPlayerMap.get(playerPushing.getUniqueID());
    	float maxLifePlayerPushing = ConstPlayerMap.get(playerPushing.getUniqueID());
    	float kiPowerPlayerPushing = WillPlayerMap.get(playerPushing.getUniqueID());
    	float kiPoolPlayerPushing = SpiritPlayerMap.get(playerPushing.getUniqueID());
    	
		if(entityBeingPushed instanceof EntityPlayerMP)
		{
	    	maxLifeEntityBeingPushed = ConstPlayerMap.get(entityBeingPushed.getUniqueID());
	    	passDefPlayerBeingPushed = PassDefPlayerMap.get(entityBeingPushed.getUniqueID());
	    	kiPowerPlayerBeingPushed = WillPlayerMap.get(entityBeingPushed.getUniqueID());
	    	kiPoolPlayerBeingPushed = SpiritPlayerMap.get(entityBeingPushed.getUniqueID());
	    	
			calculEnergyKnockBack = kiPowerPlayerPushing*0.45 + kiPoolPlayerPushing*0.25 + maxLifePlayerPushing*0.20 +  meleeDmgPlayerPushing*0.10;
			calculEnergyKnockBack = (calculEnergyKnockBack/(passDefPlayerBeingPushed*0.50 + kiPowerPlayerBeingPushed*0.20  + maxLifeEntityBeingPushed*0.15 + kiPoolPlayerBeingPushed*0.15))*energyKnockBackReducer;
			calculEnergyKnockBack = calculEnergyKnockBack/playerPushing.getDistanceToEntity(entityBeingPushed);
		}
		
		if(entityBeingPushed instanceof EntityLiving)
		{
	    	maxLifeEntityBeingPushed = ((EntityLiving) entityBeingPushed).getMaxHealth();
	    	
			calculEnergyKnockBack = kiPowerPlayerPushing*0.45 + kiPoolPlayerPushing*0.25 + maxLifePlayerPushing*0.20 +  meleeDmgPlayerPushing*0.10;
			calculEnergyKnockBack = ((calculEnergyKnockBack/maxLifeEntityBeingPushed)*energyKnockBackReducer);
			calculEnergyKnockBack = calculEnergyKnockBack/playerPushing.getDistanceToEntity(entityBeingPushed);
		}
		if (calculEnergyKnockBack > 0.15D)
		{
			calculEnergyKnockBack = 0.15D;
		}
    	return (calculEnergyKnockBack * JRMCoreH.getInt(playerPushing, "jrmcRelease")/100) * Config.strengthKiKnockback;
    }
}