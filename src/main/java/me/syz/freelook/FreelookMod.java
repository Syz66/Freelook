package me.syz.freelook;

import me.syz.freelook.config.FreelookConfig;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


// TODO: Clean up Hypixel stuff.
@Mod(modid = FreelookMod.MODID, name = FreelookMod.NAME, version = FreelookMod.VERSION)
public class FreelookMod {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";

    public static FreelookConfig config;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new FreelookConfig();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (Freelook.mc.thePlayer != null && Freelook.mc.theWorld != null) {
            if (event.gui != null && Freelook.INSTANCE.perspectiveToggled && FreelookConfig.hold) {
                Freelook.INSTANCE.resetPerspective();
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        if (Freelook.mc.thePlayer != null && Freelook.mc.theWorld != null) {
            if (event.phase.equals(TickEvent.Phase.START)) return;

            boolean down = FreelookConfig.keyBind.isActive();
            if (down != Freelook.INSTANCE.prevState && Freelook.mc.currentScreen == null) {
                Freelook.INSTANCE.onPressed(down);
                Freelook.INSTANCE.prevState = down;
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (Freelook.mc.thePlayer != null && Freelook.mc.theWorld != null) {
            if (Freelook.INSTANCE.perspectiveToggled) {
                Freelook.INSTANCE.resetPerspective();
            }
        }
    }
}
