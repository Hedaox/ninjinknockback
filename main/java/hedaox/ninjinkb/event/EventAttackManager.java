package hedaox.ninjinkb.event;

import JinRyuu.JRMCore.JRMCoreH;
import JinRyuu.JRMCore.entity.EntityNPCshadow;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import hedaox.ninjinkb.knockbackPlus.KnockbackPlus;
import hedaox.ninjinkb.statsInfos.StatsInfos;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventAttackManager {
    private static Map<UUID, Float> MeleeDmgPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Float> PassDefPlayerMap = new HashMap<UUID, Float>();
    private static Map<UUID, Integer> ConstPlayerMap = new HashMap<UUID, Integer>();
    private float ammount = 0.0F;
    private float damageA = 0.0F;
    private int maxLifeR = 20;
    private float lostLife = 0.0F;
    private boolean isPlayerDbcR = false;
    private boolean isPlayerDbcA = false;
    private float meleeDmgPlayerA = 0;
    private float passDefPlayerR = 0;


    /**
     * Event is launch everytime an entity attack. Is use for Knockback entities
     *
     * @author Hedaox
     */
    @SubscribeEvent
    public void checkAttackEvent(LivingAttackEvent event) {
        //is Server side ?
        if (!event.entity.worldObj.isRemote) {
            MeleeDmgPlayerMap = StatsInfos.getMeleeDmgPlayerMap();
            PassDefPlayerMap = StatsInfos.getPassDefPlayerMap();
            ConstPlayerMap = StatsInfos.getConstPlayerMap();
            // If what is attacking is a mob or a player
            if (event.source.damageType == "mob" || event.source.damageType == "player" || event.source.isProjectile()) {
                isPlayerDbcA = event.source.getSourceOfDamage() instanceof EntityPlayer && !event.source.isProjectile() && JRMCoreH.getInt((EntityPlayer) event.source.getSourceOfDamage(), "jrmcCncI") > 1;
                isPlayerDbcR = event.entity instanceof EntityPlayer && JRMCoreH.getInt((EntityPlayer) event.entity, "jrmcCncI") > 1;
                // If a DBC player attacks an entity
                if (isPlayerDbcA && !isPlayerDbcR) {
                    // System.out.println("player is player : " + isPlayerDbcA + " and is a(n) " + event.source.getSourceOfDamage().getClass().getName());
                    // System.out.println("Receiver is player : " + isPlayerDbcR + " and is a(n) " + event.entity.getClass().getName());

                    meleeDmgPlayerA = MeleeDmgPlayerMap.get(event.source.getSourceOfDamage().getUniqueID());
                    ammount = (float) ((EntityPlayer) event.source.getSourceOfDamage()).getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
                    damageA = meleeDmgPlayerA + ammount;
                    maxLifeR = (int) event.entityLiving.getMaxHealth();

                    // System.out.println("���Player is Attacking an Entity���");
                    // System.out.println("EntityA event ammount = " + event.ammount);
                    // System.out.println("EntityA Power = " + ammount);
                    // System.out.println("EntityR Receive Damage = " + damageA);
                    // System.out.println("EntityR Max life = " + maxLifeR);
                    // System.out.println("Life lost : " + damageA + " / " + maxLifeR + " X 100.0F = " + damageA / maxLifeR * 100.0F + " %");
                }

                // If an entity attacks a DBC player
                else if (!isPlayerDbcA && isPlayerDbcR) {
                    // System.out.println("player is player : " + isPlayerDbcA + " and is a(n) " + event.source.getSourceOfDamage().getClass().getName());
                    // System.out.println("Receiver is player : " + isPlayerDbcR + " and is a(n) " + event.entity.getClass().getName());

                    passDefPlayerR = PassDefPlayerMap.get(event.entityLiving.getUniqueID());
                    if (event.source.getSourceOfDamage() instanceof EntityNPCshadow) {
                        ammount = (float) ((EntityNPCshadow) event.source.getSourceOfDamage()).getDam();
                    } else {
                        ammount = event.ammount;
                    }

                    damageA = (ammount - passDefPlayerR) / 10;
                    maxLifeR = ConstPlayerMap.get(event.entityLiving
                            .getUniqueID());

                    // System.out.println("###Entity is Attacking the Player###");
                    // System.out.println("EntityA ammount = " + ammount);
                    // System.out.println("EntityR def = " + passDefPlayerR);
                    // System.out.println("EntityR Receive Damage = " + damageA);
                    // System.out.println("EntityR Max life = " + maxLifeR);
                    // System.out.println("Life lost : " + damageA + " / " + maxLifeR + " X 100.0F = " + damageA / maxLifeR * 100.0F + " %");
                }

                // If a DBC player attacks a DBC player
                else if (isPlayerDbcA && isPlayerDbcR) {
                    // System.out.println("player is player : " + isPlayerDbcA + " and is a(n) " + event.source.getSourceOfDamage().getClass().getName());
                    // System.out.println("Receiver is player : " + isPlayerDbcR + " and is a(n) " + event.entity.getClass().getName());

                    meleeDmgPlayerA = MeleeDmgPlayerMap.get(event.source.getSourceOfDamage().getUniqueID());
                    passDefPlayerR = PassDefPlayerMap.get(event.entityLiving.getUniqueID());
                    ammount = (float) ((EntityPlayer) event.source.getSourceOfDamage()).getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
                    damageA = meleeDmgPlayerA - passDefPlayerR + ammount;
                    maxLifeR = ConstPlayerMap.get(event.entityLiving.getUniqueID());

                    // System.out.println("###Player is Attacking another Player###");
                    // System.out.println("EntityA event ammount = " + event.ammount);
                    // System.out.println("EntityA Power = " + ammount);
                    // System.out.println("EntityR Receive Damage = " + damageA);
                    // System.out.println("EntityR Max life = " + maxLifeR);
                    // System.out.println("Life lost : " + damageA + " / " + maxLifeR + " X 100.0F = " + damageA / maxLifeR * 100.0F + " %");
                }
                // If an entity attacks another
                else if (!isPlayerDbcA && !isPlayerDbcR) {
                    // System.out.println("player is player : " + isPlayerDbcA + " and is a(n) " + event.source.getSourceOfDamage().getClass().getName());
                    // System.out.println("Receiver is player : " + isPlayerDbcR + " and is a(n) " + event.entity.getClass().getName());
                    if (event.source.getSourceOfDamage() instanceof EntityNPCshadow) {
                        ammount = (float) ((EntityNPCshadow) event.source.getSourceOfDamage()).getDam();
                    } else {
                        ammount = event.ammount;
                    }

                    damageA = ammount / 10;
                    maxLifeR = (int) event.entityLiving.getMaxHealth();

                    // System.out.println("###Entity is Attacking an Entity###");
                    // System.out.println("EntityA event ammount = " + event.ammount);
                    // System.out.println("EntityA Power = " + ammount);
                    // System.out.println("EntityR Receive Damage = " + damageA);
                    // System.out.println("EntityR Max life = " + maxLifeR);
                    // System.out.println("Life lost : " + damageA + " / " + maxLifeR + " X 100.0F = " + damageA / maxLifeR * 100.0F + " %");
                }

                lostLife = damageA / maxLifeR * 100.0f;

                if (lostLife < 0) {
                    lostLife = 0;
                }

                KnockbackPlus.ApplyIntelligentKB(event, lostLife, damageA);

            }
        }
    }
}
