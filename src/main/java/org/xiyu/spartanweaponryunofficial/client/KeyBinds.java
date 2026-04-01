package org.xiyu.spartanweaponryunofficial.client;

import com.mojang.blaze3d.platform.InputConstants.Type;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID, value = Dist.CLIENT)
public class KeyBinds {
    public static final KeyMapping.Category MOD_CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "category"));
    public static KeyMapping KEY_ACCESS_QUIVER = new KeyMapping("key." + ModSpartanWeaponry.ID + ".access_quiver", Type.KEYSYM, GLFW.GLFW_KEY_I, MOD_CATEGORY);

    @SubscribeEvent
    public static void registerKeyBinds(RegisterKeyMappingsEvent ev) {
        ev.register(KeyBinds.KEY_ACCESS_QUIVER);
    }
}
