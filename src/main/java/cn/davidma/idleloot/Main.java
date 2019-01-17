package cn.davidma.idleloot;

import cn.davidma.idleloot.handler.GUIHandler;
import cn.davidma.idleloot.proxy.CommonProxy;
import cn.davidma.idleloot.reference.Info;
import cn.davidma.idleloot.tileentity.GeneratorTileEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid=Info.MOD_ID, name=Info.NAME, version=Info.VERSION)
public class Main {
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide=Info.CLIENT_PROXY, serverSide=Info.COMMON_PROXY)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {
		
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		GameRegistry.registerTileEntity(GeneratorTileEntity.class, Info.MOD_ID + "_GeneratorTileEntity");
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GUIHandler());
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event) {

	}
}
