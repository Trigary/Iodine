package hu.trigary.iodine.forge;

import hu.trigary.iodine.common.CommonTest;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = "iodine", useMetadata = true, clientSideOnly = true)
public class IodineMod {
	
	private static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// some example code
		Class<CommonTest> clazz = CommonTest.class;
		logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
	}
}
