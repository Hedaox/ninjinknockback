package hedaox.ninjinkb.knockbackPlus;

import hedaox.ninjinkb.config.Config;
import hedaox.ninjinkb.lib.ModVars;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This allow players and entities to knockback depending on their strength, work with regular minecraft entities and DBC players
 *
 * @author Hedaox
 */
public class KnockbackPlus {

    private static float minDamage = Config.minDamage;
    private static float maxDamage = Config.maxDamage;

    private static FailPunchTimer failPunchTimer = null;

    /**
     * This function compares player/entities strength
     * and will apply a knockback or not depending on the strength of the attacks
     */
    public static void ApplyIntelligentKB(LivingAttackEvent event, float lostLife, float damage) {
        //is Server side ?
        if (!event.entity.worldObj.isRemote) {
            // if the percentage of lost life is under 0.001% then the entity does not receive any damage
            if (lostLife <= minDamage) {
                //Make a fail punch sound
                FailPunchTimer failPunchTimer = FailPunchTimer.getInstance();

                if (!failPunchTimer.isTaskAlreadyScheduled() && failPunchTimer.isSafetoSoundFailPunch() && event.source.damageType != "player") {

                    if (event.entity instanceof EntityPlayerMP && !((EntityPlayerMP) event.entity).capabilities.isCreativeMode) {
                        event.entity.worldObj.playSoundAtEntity(event.entity, ModVars.MOD_ID + ":failpunch", 1f, 1f);
                        failPunchTimer.setTaskAlreadyScheduled(true);
                        failPunchTimer.setSafetoSoundFailPunch(false);
                        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
                        ses.schedule(failPunchTimer.taskSafeSoundFailPunch, 500, TimeUnit.MILLISECONDS);
                    } else if (!(event.entity instanceof EntityPlayerMP)) {
                        event.entity.worldObj.playSoundAtEntity(event.entity, ModVars.MOD_ID + ":failpunch", 1f, 1f);
                        failPunchTimer.setTaskAlreadyScheduled(true);
                        failPunchTimer.setSafetoSoundFailPunch(false);
                        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
                        ses.schedule(failPunchTimer.taskSafeSoundFailPunch, 500, TimeUnit.MILLISECONDS);
                    }
                } else if (event.source.damageType == "player") {
                    event.entity.worldObj.playSoundAtEntity(event.entity, ModVars.MOD_ID + ":failpunch", 1f, 1f);
                }

                //Cancel attack
                event.setCanceled(true);
            }
            // if the percentage of lost life is under 10% then the entity receive a normal knockback and attack and damage under 100
            else if (lostLife <= maxDamage || damage < Config.minDamageForBigKnockback) {
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
                    ApplyKB(event, (lostLife / 8.5) * Config.strengthKnockback);
                }
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
        if (event.source.getSourceOfDamage() instanceof EntityPlayerMP) {
            if (((EntityPlayerMP) event.source.getSourceOfDamage()).theItemInWorldManager.isCreative()) {
                knockback = 0;
            }
        }

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