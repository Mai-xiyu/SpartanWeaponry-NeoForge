package org.xiyu.spartanweaponryunofficial.event;

import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.glfw.GLFW;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.client.KeyBinds;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.inventory.tooltip.OilCoatingTooltip;
import org.xiyu.spartanweaponryunofficial.network.NetworkHandler;
import org.xiyu.spartanweaponryunofficial.network.QuiverAccessPacket;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

import java.util.Optional;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEventHandler {
    // NeoForge 1.21: MouseButton is now abstract, must use Post subclass
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onMouseEvent(InputEvent.MouseButton.Post ev) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.screen != null || mc.isPaused())
            return;

        if (ev.getButton() == KeyBinds.KEY_ACCESS_QUIVER.getKey().getValue() && ev.getAction() == GLFW.GLFW_PRESS) {
            NetworkHandler.sendPacketToServer(new QuiverAccessPacket());
            //Log.info("Mouse: Attack pressed!");
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onKeyEvent(InputEvent.Key ev) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.screen != null || mc.isPaused())
            return;

        if (ev.getKey() == KeyBinds.KEY_ACCESS_QUIVER.getKey().getValue() && ev.getAction() == GLFW.GLFW_PRESS) {
            NetworkHandler.sendPacketToServer(new QuiverAccessPacket());
            //Log.info("Keyboard: Attack pressed!");
        }
    }

    // High priority to allow other mods to place elements further above this
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderTooltip(RenderTooltipEvent.GatherComponents ev) {
        ItemStack stack = ev.getItemStack();

        if (stack.is(ModItemTags.OILABLE_WEAPONS)) {
            IOilHandler oilHandler = stack.getCapability(ModCapabilities.OIL_CAPABILITY);
            if (oilHandler != null) {
                if (oilHandler.isOiled() && oilHandler.getEffect().isPresent()) {
                    OilEffect oilEffect = oilHandler.getEffect().get();
                    Optional<Potion> potionOpt = oilHandler.getPotion();
                    ItemStack oilStack = potionOpt.map(OilHelper::makePotionOilStack).orElseGet(() -> OilHelper.makeOilStack(oilEffect));

                    ev.getTooltipElements().add(1, Either.right(new OilCoatingTooltip(oilStack, oilHandler.getUsesLeft(), oilEffect.getMaxUses())));
                } else
                    ev.getTooltipElements().add(1, Either.left(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".oilable").withStyle(ChatFormatting.BLUE)));
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayPre(RenderGuiLayerEvent.Pre ev) {
        Minecraft mc = Minecraft.getInstance();
        if (ev.getName().equals(VanillaGuiLayers.CROSSHAIR) && mc.player.getMainHandItem().is(ModItemTags.HAS_CUSTOM_CROSSHAIR))
            ev.setCanceled(true);
    }

    // Debug NBT viewer; Enable if NBT needs to be debugged
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent ev) {
        ItemStack stack = ev.getItemStack();

        // Debug (Show NBT data on *EVERYTHING*)

        if (!FMLLoader.isProduction() && ItemStackDataHelper.hasTag(stack) && ev.getFlags().isAdvanced()) {
            // Format NBT debug string
            String nbtStr = ItemStackDataHelper.getTag(stack).toString();
            ev.getToolTip().add(Component.literal("NBT: " + ChatFormatting.DARK_GRAY + nbtStr).withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    @SubscribeEvent
    public static void onScreenInitPost(ScreenEvent.Init.Post event) {
        // Only inject Discord button if the current screen is our generated config screen
        if (ModSpartanWeaponry.myConfigScreenInstance != null && event.getScreen() == ModSpartanWeaponry.myConfigScreenInstance) {
            int screenWidth = event.getScreen().width;

            // Generate "Discord" Button (width: 80, height: 20, 10px padding from top right)
            Button discordButton = Button.builder(Component.literal("Discord"), button -> {
                ConfirmLinkScreen.confirmLinkNow(event.getScreen(), "https://discord.com/invite/h88UDxwUHm");
            }).bounds(screenWidth - 80 - 10, 10, 80, 20).build();

            event.addListener(discordButton);
        }
    }
}
