package org.xiyu.spartanweaponryunofficial.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

public class NetworkHandler {
    private static final int PROTOCOL_VERSION = 1;

    public static final SimpleChannel CHANNEL = ChannelBuilder
            .named(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .clientAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .serverAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .simpleChannel();

    public static void register() {
        CHANNEL.messageBuilder(QuiverAccessPacket.class)
                .encoder(QuiverAccessPacket::encode)
                .decoder(QuiverAccessPacket::decode)
                .consumerMainThread(QuiverAccessPacket::handle)
                .add();
        CHANNEL.messageBuilder(QuiverPrioritySlotPacket.class)
                .encoder(QuiverPrioritySlotPacket::encode)
                .decoder(QuiverPrioritySlotPacket::decode)
                .consumerMainThread(QuiverPrioritySlotPacket::handle)
                .add();
        CHANNEL.messageBuilder(QuiverButtonPacket.class)
                .encoder(QuiverButtonPacket::encode)
                .decoder(QuiverButtonPacket::decode)
                .consumerMainThread(QuiverButtonPacket::handle)
                .add();
    }

    public static void sendPacketTo(Object packet, ServerPlayer player) {
        CHANNEL.send(packet, PacketDistributor.PLAYER.with(player));
    }

    public static void sendPacketToServer(Object packet) {
        CHANNEL.send(packet, PacketDistributor.SERVER.noArg());
    }
}
