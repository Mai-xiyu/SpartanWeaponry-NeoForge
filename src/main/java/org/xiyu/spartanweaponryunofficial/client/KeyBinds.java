package org.xiyu.spartanweaponryunofficial.client;

import com.mojang.blaze3d.platform.InputConstants.Type;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

@Mod.EventBusSubscriber(modid = ModSpartanWeaponry.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBinds {
    public static KeyMapping KEY_ACCESS_QUIVER = new KeyMapping("key." + ModSpartanWeaponry.ID + ".access_quiver", KeyConflictContext.IN_GAME, Type.KEYSYM, GLFW.GLFW_KEY_I, "key." + ModSpartanWeaponry.ID + ".category.title");

    @SubscribeEvent
    public static void registerKeyBinds(RegisterKeyMappingsEvent ev) {
        ev.register(KeyBinds.KEY_ACCESS_QUIVER);
    }
}
