//package com.orbital.orbitalradish;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.network.NetworkDirection;
//import net.minecraftforge.network.NetworkRegistry;
//import net.minecraftforge.network.PacketDistributor;
//import net.minecraftforge.network.simple.SimpleChannel;
//
//import java.util.Optional;
//
//public final class NetworkHandler {
//    private static final String PROTOCOL = "1";
//    private static final ResourceLocation CHANNEL_NAME = ResourceLocation.fromNamespaceAndPath(OrbitalRadishMod.MODID, "network");
//    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
//            .named(CHANNEL_NAME)
//            .networkProtocolVersion(() -> PROTOCOL)
//            .clientAcceptedVersions(PROTOCOL::equals)
//            .serverAcceptedVersions(PROTOCOL::equals)
//            .simpleChannel();
//
//    private static int id = 0;
//
//    private NetworkHandler() {}
//
//    public static void register() {
//        CHANNEL.registerMessage(id++,
//                RadishPickupPacket.class,
//                RadishPickupPacket::encode,
//                RadishPickupPacket::decode,
//                RadishPickupPacket::handle,
//                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
//        );
//    }
//
//    public static void sendToAll(RadishPickupPacket pkt) {
//        CHANNEL.send(PacketDistributor.ALL.noArg(), pkt);
//    }
//
//    // If you later want to send to only one player:
//    // public static void sendToPlayer(ServerPlayer player, RadishPickupPacket pkt) { ... }
//}
