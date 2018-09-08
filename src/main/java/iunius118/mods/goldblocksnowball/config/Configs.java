package iunius118.mods.goldblocksnowball.config;

import iunius118.mods.goldblocksnowball.GoldBlockSnowBall;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;

@Config(modid = GoldBlockSnowBall.MOD_ID)
public class Configs {

    @Comment("Enable Gold Block Snow Ball")
    @LangKey(GoldBlockSnowBall.MOD_ID + ".config.enable_gold_block_snow_ball")
	public static boolean enableGoldBlockSnowBall = true;

    @Comment("Block ID to replace with")
    @LangKey(GoldBlockSnowBall.MOD_ID + ".config.block_id_to_replace_with")
    public static String blockIDToReplaceWith = "minecraft:gold_block";

}
