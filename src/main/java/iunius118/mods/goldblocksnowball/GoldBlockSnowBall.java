package iunius118.mods.goldblocksnowball;

import org.apache.logging.log4j.Logger;

import iunius118.mods.goldblocksnowball.config.Configs;
import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.ThrowableImpactEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
        modid = GoldBlockSnowBall.MOD_ID,
        name = GoldBlockSnowBall.MOD_NAME,
        version = GoldBlockSnowBall.MOD_VERSION,
        dependencies = GoldBlockSnowBall.MOD_DEPENDENCIES,
        guiFactory = "iunius118.mods.goldblocksnowball.client.gui.ConfigGuiFactory",
        useMetadata = true)
@EventBusSubscriber
public class GoldBlockSnowBall
{

    public static final String MOD_ID = "goldblocksnowball";
    public static final String MOD_NAME = "Gold Block Snow Ball";
    public static final String MOD_VERSION = "1.12-0.0.0.0";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12-14.21.1.2387,)";

    public static final Configs CONFIGS = new Configs();
    public static Logger logger;

    public static Block blockToReplaceWith = Blocks.GOLD_BLOCK;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        updateBlock();

        if (event.getSide().isClient())
        {
        	MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        }
    }

    @SubscribeEvent
    public static void onThrowableImpact(ThrowableImpactEvent event)
    {
    	if (Configs.enableGoldBlockSnowBall == false) return;

    	EntityThrowable entity = event.getEntityThrowable();

    	if (entity instanceof EntitySnowball)
    	{
        	RayTraceResult result = event.getRayTraceResult();

        	if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
        	{
        		BlockPos pos = result.getBlockPos();
        		World world = entity.world;

        		world.setBlockState(pos, blockToReplaceWith.getDefaultState());
        	}
    	}

    }

    @SideOnly(Side.CLIENT)
    public class ClientEventHandler
    {

    	@SubscribeEvent
        public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(MOD_ID))
            {
                ConfigManager.sync(MOD_ID, Type.INSTANCE);

                updateBlock();
            }
        }

    }

    public void updateBlock()
    {
        blockToReplaceWith = Block.getBlockFromName(Configs.blockIDToReplaceWith);

        if (blockToReplaceWith == null) {
        	blockToReplaceWith = Blocks.GOLD_BLOCK;
        }
    }

}