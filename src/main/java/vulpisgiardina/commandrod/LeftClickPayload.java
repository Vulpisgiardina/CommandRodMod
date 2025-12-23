package vulpisgiardina.commandrod;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LeftClickPayload() implements CustomPacketPayload {

    public static final Type<LeftClickPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CommandRod.MODID, "left_click_action"));

    public static final StreamCodec<ByteBuf, LeftClickPayload> STREAM_CODEC = StreamCodec.unit(new LeftClickPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(LeftClickPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                ItemStack stack = serverPlayer.getMainHandItem();

                if (stack.getItem() instanceof CommandRodItem) {
                    ClickCommandData data = stack.get(CommandRod.CLICK_COMMAND.get());

                    if (data != null && data.left().isPresent()) {
                        CommandRodItem.executeCommand(serverPlayer.level(), serverPlayer, data.left().get());
                    }
                }
            }
        });
    }
}
