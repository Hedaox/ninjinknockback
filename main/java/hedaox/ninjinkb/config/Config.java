package hedaox.ninjinkb.config;

import hedaox.ninjinkb.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {

    private static final String CATEGORY_KNOCKBACK = "knockback";
    private static final String CATEGORY_KNOCKBACKKI= "knockbackki";

    // This values below you can access elsewhere in your mod:
    public static float minDamage = 0.001F;
    public static float maxDamage = 10F;
    public static float minDamageForBigKnockback = 100F;
    public static float minDamageForBigKnockback2 = 1000F;
    public static float strengthKnockback = 1F;
    public static float strengthKiKnockback = 1F;

    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
    // exist yet and read the values if it does exist.
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception e1) {

        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_KNOCKBACK, "Knockback configuration");
        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
        minDamage = cfg.getFloat("minDamage", CATEGORY_KNOCKBACK, 0.001F, 0F, 100F, "Default value : Any damage value under this value will result in no knockback at all. Value is in % of damage taken. Should be smaller than maxDamage.");
        maxDamage = cfg.getFloat("maxDamage", CATEGORY_KNOCKBACK, 10F, 0F, 100F, "Any damage value under this value and above minDamage value will result in a normal knockback. Any damage above maxDamage will result in a big knockback. Value is in % of damage taken. Should be greater than minDamage.");
        minDamageForBigKnockback = cfg.getFloat("minDamageForBigKnockback", CATEGORY_KNOCKBACK, 100F, 0F, 100000000F, "Any damage value under this value will never result in a big knockback.");
        minDamageForBigKnockback2 = cfg.getFloat("minDamageForBigKnockback2", CATEGORY_KNOCKBACK, 1000F, 0F, 100000000F, "Any damage value under this value will never result in a very big knockback.");
        strengthKnockback = cfg.getFloat("strengthKnockback", CATEGORY_KNOCKBACK, 1F, 0F, 1000F, "Strength of the knockback. Send entities flying farther when attacking.");
        cfg.addCustomCategoryComment(CATEGORY_KNOCKBACK, "Knockback Energy configuration");
        strengthKiKnockback = cfg.getFloat("strengthKiKnockback", CATEGORY_KNOCKBACKKI, 1F, 0F, 1000F, "Strength of the ki knockback. Send entities flying farther when charging ki.");
    }
}
