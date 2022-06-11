# Ninjin Knockback

A simple knockback mod for DBC 1.4.79.

This mod was created by me (Hedaox). This mod add a proportional knockback when players or entities are attacking each
over and also push back entities/players when stronger players are charging their ki. This mod works with the DBC mod
made by Jinryuu (Now the mod is developped by his brother Ben). Here his website : http://main.jingames.net/.

Here a video showing the knockback in action : https://www.youtube.com/watch?v=_-yLpAelcm8
(If you want to know where this Gogeta is from check my other mod : https://github.com/Hedaox/ninjinentities)

The main motivation for making this mod is a scene where Mr. Satan is attacking
Cell : https://www.youtube.com/watch?v=gEl8bxjRB90. Basically this mod will compare players/entities stats and then
apply or not a knockback when attacking. Also when a player is charging his ki, every entities weaker than him will be
push back. This should works with any basic mobs.

# Licensing

This mod is under open source license : GNU GENERAL PUBLIC LICENSE Version 3 What this means is that you can use this
mod anyway you want for private use but if you use it any other way you will have to respect this :

- You cannot claim this mod as being yours.

- You will have to credit me (Hedaox) for using this mod.

If you want to modify the mod :

- You will have to post the modified version publicly, meaning that your code should be easily accessible to everyone.

- You will have to add this same license to your modified version of this mod.

You can found full license here : https://www.gnu.org/licenses/gpl-3.0.en.html or in the mod files.

# Config

You can change config by editing the file in config/ninjinknockback.cfg in your minecraft files. (Only for 1.2.0.0 and
more)

```
knockback {
    # Any damage value under this value and above minDamage value will result in a normal knockback. Any damage above maxDamage will result in a big knockback. Value is in % of damage taken. Should be greater than minDamage. [range: 0.0 ~ 100.0, default: 10.0]
    S:maxDamage=10.0

    # Default value : Any damage value under this value will result in no knockback at all. Value is in % of damage taken. Should be smaller than maxDamage. [range: 0.0 ~ 100.0, default: 0.001]
    S:minDamage=0.001

    # Any damage value under this value will never result in a big knockback. [range: 0.0 ~ 1.0E8, default: 100.0]
    S:minDamageForBigKnockback=100.0

    # Any damage value under this value will never result in a very big knockback. [range: 0.0 ~ 1.0E8, default: 1000.0]
    S:minDamageForBigKnockback2=1000.0

    # Strength of the knockback. Send entities flying farther when attacking. [range: 0.0 ~ 1000.0, default: 1.0]
    S:strengthKnockback=1.0
            
    # CoolDown for the punch, prevent some bugs with players punching too quickly. [range: 0.0 ~ 1000.0, default: 0.1]
    S:punchCooldDown=0.1
}

knockbackki {
    # Strength of the ki knockback. Send entities flying farther when charging ki. [range: 0.0 ~ 1000.0, default: 1.0]
    S:strengthKiKnockback=1.0
}
```

# How to install the mod :

- Install Forge 1.7.10 : https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.7.10.html

- Install DBC : http://main.jingames.net/minecraft-mods/dragon-block-c/downloads-and-installation/

- Put the mod in the ".Minecraft/mods"
  folder : https://github.com/Hedaox/ninjinknockback/releases/download/1.2.4.1/ninjinkb-1.2.4.1.jar

# Make a donation

If you like what I do, you can help by making a donation here : https://www.patreon.com/Hedaox

If you want to use this mod, do not hesitate to PM me, it will make me very happy ^^
