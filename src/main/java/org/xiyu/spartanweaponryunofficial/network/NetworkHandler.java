package org.xiyu.spartanweaponryunofficial.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    protected static final String PROTOCOL_VERSION = "1";

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(QuiverAccessPacket.TYPE, QuiverAccessPacket.STREAM_CODEC, QuiverAccessPacket::handle);
        registrar.playToServer(QuiverPrioritySlotPacket.TYPE, QuiverPrioritySlotPacket.STREAM_CODEC, QuiverPrioritySlotPacket::handle);
        registrar.playToServer(QuiverButtonPacket.TYPE, QuiverButtonPacket.STREAM_CODEC, QuiverButtonPacket::handle);
    }

    public static void sendPacketTo(CustomPacketPayload payload, ServerPlayer player) {
        if (!(player instanceof FakePlayer))
            PacketDistributor.sendToPlayer(player, payload);
    }

    public static void sendPacketToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }
}
