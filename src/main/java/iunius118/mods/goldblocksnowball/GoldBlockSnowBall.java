package iunius118.mods.goldblocksnowball;

import org.apache.logging.log4j.Logger;

import iunius118.mods.goldblocksnowball.config.Configs;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
    public static final String MOD_VERSION = "1.12-0.0.2.0";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12-14.21.1.2387,)";

    public static final Configs CONFIGS = new Configs();
    public static Logger logger;

    public static Block blockToReplaceWith = Blocks.GOLD_BLOCK;
    public static boolean hasCheckedBlock = false;


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

    	if (entity instanceof EntitySnowball && !entity.world.isRemote)
    	{
    		EntityLivingBase thrower = entity.getThrower();

    		if (Configs.enableSurvivalModePlayer == false
    				&& thrower instanceof EntityPlayer
    				&& ((EntityPlayer) thrower).capabilities.isCreativeMode == false)
    		{
    			return;
    		}

    		RayTraceResult result = event.getRayTraceResult();

        	if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
        	{
        		BlockPos pos = result.getBlockPos();
        		Vec3d hit = result.hitVec;
        		World world = entity.world;
        		IBlockState iblockstate;

        		// Replace block with player's off-hand block
        		if (Configs.enableOffhandBlock && thrower != null)
        		{
        			ItemStack stack = thrower.getHeldItemOffhand();

        			if (stack.isEmpty() == false && stack.getItem() instanceof ItemBlock)
        			{
        				Block offhandBlock = ((ItemBlock) stack.getItem()).getBlock();

        				if (offhandBlock != null)
        				{
        					Block block = offhandBlock;
        					int metadate = stack.getMetadata();
        					iblockstate = block.getStateForPlacement(world, pos, result.sideHit, (float) hit.x, (float) hit.y, (float) hit.z, metadate, thrower, EnumHand.MAIN_HAND);
        					setBlockState(world, pos, iblockstate);
        					return;
        				}
        			}
        		}

        		// Replace block with configured block
        		if (blockToReplaceWith == null)
        		{
        			return;
        		}

        		Block block = blockToReplaceWith;
        		int metadate = Configs.blockMetadataToReplaceWith;

        		if (hasCheckedBlock)
        		{
            		iblockstate = block.getStateForPlacement(world, pos, result.sideHit, (float) hit.x, (float) hit.y, (float) hit.z, metadate, thrower, EnumHand.MAIN_HAND);
        		}
        		else
        		{
        			// Check block metadata
        			try
        			{
                		iblockstate = block.getStateForPlacement(world, pos, result.sideHit, (float) hit.x, (float) hit.y, (float) hit.z, metadate, thrower, EnumHand.MAIN_HAND);
        			}
        			catch(Exception e)
        			{
        				blockToReplaceWith = null;
        				return;
        			}

        			hasCheckedBlock = true;
        		}

        		setBlockState(world, pos, iblockstate);
        	}
    	}

    }

    public static void setBlockState(World world, BlockPos pos, IBlockState iblockstate)
    {
    	if (world != null && pos != null && iblockstate != null)
    	{
    		world.setBlockState(pos, iblockstate);
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

        if (blockToReplaceWith != null)
        {
        	hasCheckedBlock = false;
        }
    }

}
