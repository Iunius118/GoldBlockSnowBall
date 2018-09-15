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

    @Comment("Enable survival mode players to replace block")
    @LangKey(GoldBlockSnowBall.MOD_ID + ".config.enable_survival_mode_player")
	public static boolean enableSurvivalModePlayer = false;

    @Comment("Enable to replace with player's off-hand block")
    @LangKey(GoldBlockSnowBall.MOD_ID + ".config.enable_off-hand_block")
	public static boolean enableOffhandBlock = true;

    @Comment("Block ID to replace with")
    @LangKey(GoldBlockSnowBall.MOD_ID + ".config.block_id_to_replace_with")
    public static String blockIDToReplaceWith = "minecraft:gold_block";

    @Comment("Metadata of block to replace with")
    @LangKey(GoldBlockSnowBall.MOD_ID + ".config.block_metadata_to_replace_with")
    public static int blockMetadataToReplaceWith = 0;

}
