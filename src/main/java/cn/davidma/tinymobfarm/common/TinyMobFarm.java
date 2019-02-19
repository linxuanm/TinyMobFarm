package cn.davidma.tinymobfarm.common;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.tinymobfarm.common.block.MobFarm;
import cn.davidma.tinymobfarm.common.item.Lasso;
import cn.davidma.tinymobfarm.common.tileentity.MobFarmTileEntity;
import cn.davidma.tinymobfarm.core.EnumMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(Reference.MOD_ID)
public class TinyMobFarm {

	public static TinyMobFarm instance;
	
	public static ItemGroup creativeTab;
	
	public static Item lasso;
	public static List<Block> mobFarms;
	public static TileEntityType<MobFarmTileEntity> mobFarmTileEntity;
	
	public TinyMobFarm() {
		instance = this;
		creativeTab = new ItemGroup("tiny_mob_farm") {
			@Override
			public ItemStack createIcon() {
				return new ItemStack(TinyMobFarm.mobFarms.get(0));
			}
		};
		
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::registerTileEntities);
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		
		mobFarms = new ArrayList<Block>();
		
		for (EnumMobFarm i: EnumMobFarm.values()) {
			Block mobFarm = new MobFarm(i).setRegistryName(Reference.getLocation(i.getRegistryName()));
			mobFarms.add(mobFarm);
			registry.register(mobFarm);
		}
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registry.register(lasso = new Lasso(new Item.Properties()).setRegistryName(Reference.getLocation("lasso")));
		
		for (Block i: mobFarms) {
			Item mobFarmItemBlock = new ItemBlock(i, new Item.Properties().group(creativeTab)).setRegistryName(i.getRegistryName());
			registry.register(mobFarmItemBlock);
		}
	}
	
	@SubscribeEvent
	public void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
		mobFarmTileEntity = TileEntityType.register(mobFarms.get(0).getRegistryName().toString(), TileEntityType.Builder.create(MobFarmTileEntity::new));
	}
}
