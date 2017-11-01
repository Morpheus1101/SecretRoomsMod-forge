package com.wynprice.secretroomsmod;

import com.wynprice.secretroomsmod.proxy.ClientProxy;
import com.wynprice.secretroomsmod.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
		modid = SecretRooms2.MODID,
		name = SecretRooms2.MODNAME,
		version = SecretRooms2.VERSION,
		acceptedMinecraftVersions = "[1.12.2,1.13]",
		dependencies = "required-after:forge@[14.23.0.2502,)")
public class SecretRooms2
{
    public static final String MODID = "secretroomsmod";
    public static final String MODNAME = "Secret Rooms 2";
    public static final String VERSION = "2.0.0";
    
    @SidedProxy(modId = MODID, clientSide = "com.wynprice.secretroomsmod.proxy.ClientProxy", serverSide = "com.wynprice.secretroomsmod.proxy.ServerProxy")
    public static CommonProxy proxy;
    
    @Instance(MODID)
    public static SecretRooms2 instance;
    public static final CreativeTabs TAB = new CreativeTabs(MODID) {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(SecretItems.CAMOUFLAGE_PASTE);
		}
	};
	
    @EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    }
}