package hedaox.ninjinkb.knockbackPlus;

import hedaox.ninjinkb.config.Config;
import hedaox.ninjinkb.lib.ModVars;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * This allow players and entities to knockback depending on their strength, work with regular minecraft entities and DBC players
 *
 * @author Hedaox
 */
public class KnockbackPlus {

    private static float minDamage = Config.minDamage;
    private static float maxDamage = Config.maxDamage;

    private static HashMap<UUID, Long> lastPunchTimerMap = new HashMap<>();
    // Prevent multiple Kb For Players (the first is pve, second pvp)
    private static int authorizeKb = 0;
    private static int authorizeKb2 = 0;

    /**
     * This function compares player/entities strength
     * and will apply a knockback or not depending on the strength of the attacks
     */
    public static void ApplyIntelligentKB(LivingAttackEvent event, float lostLife, float damage) {
        //is Server side ?
        if (!event.entity.worldObj.isRemote) {

            if (!(event.source.getEntity() instanceof EntityPlayerMP) || authorizeKb == 1 && !(event.entity instanceof EntityPlayerMP) || authorizeKb2 == 3) {

                authorizeKb = 0;
                authorizeKb2 = 0;

                Entity entityVictim = event.entity;
                Entity entityAttacker = event.source.getEntity();

                if (!(entityAttacker instanceof EntityPlayerMP) || !lastPunchTimerMap.containsKey(entityAttacker.getUniqueID()) || (lastPunchTimerMap.get(entityAttacker.getUniqueID()) != null && (System.currentTimeMillis() - lastPunchTimerMap.get(entityAttacker.getUniqueID())) > Config.punchCooldDown)) {

                    // if creative cancel kb
                    if (entityVictim instanceof EntityPlayerMP && ((EntityPlayerMP) entityVictim).theItemInWorldManager.isCreative()) {
                        lostLife = 0;
                    }

                    if (entityAttacker instanceof EntityPlayerMP) {
                        lastPunchTimerMap.put(entityAttacker.getUniqueID(), System.currentTimeMillis());
                    }

                    // if the percentage of lost life is under 0.001% then the entity does not receive any damage
                    if (lostLife <= minDamage) {

                        //System.out.println("FailPunch");

                        //Cancel attack
                        event.setCanceled(true);

                        entityVictim.worldObj.playSoundAtEntity(entityVictim, ModVars.MOD_ID + ":failpunch", 1f, 1f);
                    }
                    // if the percentage of lost life is under 10% then the entity receive a normal knockback and attack and damage under 100
                    else if (lostLife <= maxDamage || damage < Config.minDamageForBigKnockback) {
                        System.out.println("NormalPunch");
                    }
                    // if the percentage of lost life is above 10% then the entity receive a big knockback
                    else {
                        // for preventing knockback that are too big
                        if (lostLife >= 50.0F && damage < Config.minDamageForBigKnockback2) {
                            lostLife = 50.0F;
                        } else if (lostLife >= 100.0F) {
                            lostLife = 100.0F;
                        }

                        //We apply the knockback, except from projectile
                        if (!event.source.isProjectile()) {
                            //System.out.println("SuperPunch");

                            ApplyKB(event, (lostLife / 20) * Config.strengthKnockback);
                        }
                    }
                } else {
                    //Cancel attack
                    //System.out.println("Canceled");
                    event.setCanceled(true);
                }
            } else {
                authorizeKb++;
                authorizeKb2++;
            }
        }
    }

    /**
     * Function to do a bigger knockback to an entity receiving an attack
     *
     * @param event
     * @param knockback
     * @author Hedaox
     */
    public static void ApplyKB(LivingAttackEvent event, double knockback) {
        // if source exists (player might be leaving server while being punched)
        if (event.source.getSourceOfDamage() != null) {
            Vec3 look = event.source.getSourceOfDamage().getLookVec().normalize();
            event.entityLiving.addVelocity(look.xCoord * knockback, look.yCoord * knockback, look.zCoord * knockback);
        }

        if (knockback >= 0.7d) {
            event.entityLiving.addVelocity(0, (knockback - 0.7d) / 2.0d, 0);
        }
    }
}