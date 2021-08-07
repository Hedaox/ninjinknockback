# Ninjin Knockback

A simple knockback mod for DBC 1.4.68 - 1.4.73.

This mod was created by me (Hedaox). This mod add a proportional knockback when players or entities are attacking each over and also push back entities/players when stronger players are charging their ki. This mod works with the DBC mod made by Jinryuu (Now the mod is developped by his brother Ben). Here his website : http://main.jingames.net/.

Here a video showing the knockback in action : https://www.youtube.com/watch?v=_-yLpAelcm8
(If you want to know where this Gogeta is from check my other mod : https://github.com/Hedaox/ninjinentities)

The main motivation for making this mod is a scene where Mr. Satan is attacking Cell : https://www.youtube.com/watch?v=gEl8bxjRB90. Basically this mod will compare players/entities stats and then apply or not a knockback when attacking. Also when a player is charging his ki, every entities weaker than him will be push back. This should works with any basic mobs.

# Licensing

This mod is under open source license : GNU GENERAL PUBLIC LICENSE Version 3 What this means is that you can use this mod anyway you want for private use but if you use it any other way you will have to respect this :

  - You cannot claim this mod as being yours.
  
  - You will have to credit me (Hedaox) for using this mod.

If you want to modify the mod :

  - You will have to post the modified version publicly, meaning that your code should be easily accessible to everyone.
  
  - You will have to add this same license to your modified version of this mod.

You can found full license here : https://www.gnu.org/licenses/gpl-3.0.en.html or in the mod files.

# Config

You can change config by editing the file in config/ninjinknockback.cfg in your minecraft files.

- maxDamage : Any damage value under this value and above minDamage value will result in a normal knockback. Any damage above maxDamage will result in a big knockback. Value is in % of damage taken. Should be greater than minDamage. [range: 1.0E-5 ~ 100.0, default: 10.0]

- minDamage : Default value : Any damage value under this value will result in no knockback at all. Value is in % of damage taken. Should be smaller than maxDamage. [range: 1.0E-5 ~ 100.0, default: 0.001]

- strengthKnockback : Strength of the knockback. Send entities flying farther when attacking. [range: 1.0E-5 ~ 100.0, default: 1.0]

- strengthKiKnockback : Strength of the ki knockback. Send entities flying farther when charging ki. [range: 1.0E-5 ~ 100.0, default: 1.0]

# How to install the mod :

  - Install Forge 1.7.10 : https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.7.10.html
  
  - Install DBC : http://main.jingames.net/minecraft-mods/dragon-block-c/downloads-and-installation/ 
  
  - Put the mod in the ".Minecraft/mods" folder : https://github.com/Hedaox/ninjinknockback/releases/download/1.1.0.0/ninjinkb-1.7.10-1.1.0.0.jar

# Make a donation

If you like what I do, you can help by making a donation here : https://www.patreon.com/Hedaox

If you want to use this mod, do not hesitate to PM me, it will make me very happy ^^
