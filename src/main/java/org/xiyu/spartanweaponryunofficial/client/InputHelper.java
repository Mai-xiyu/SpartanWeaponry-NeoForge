package org.xiyu.spartanweaponryunofficial.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
/**
 * Client-only input utilities. Must only be referenced behind a
 * {@code FMLEnvironment.dist.isClient()} guard to avoid class-loading
 * crashes on the dedicated server.
 */
public final class InputHelper {
    private InputHelper() {}

    public static boolean isShiftDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(),
                InputConstants.KEY_LSHIFT)
                || InputConstants.isKeyDown(Minecraft.getInstance().getWindow(),
                InputConstants.KEY_RSHIFT);
    }
}
