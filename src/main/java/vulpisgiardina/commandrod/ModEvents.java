package vulpisgiardina.commandrod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = CommandRod.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide()) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof CommandRodItem) {
                CommandRod.LOGGER.info("Left Click Empty with Command Rod");
                ClientPacketDistributor.sendToServer(new LeftClickPayload());
            }
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            boolean executed = handleLeftClick(serverPlayer, serverPlayer.getMainHandItem());

            if (executed) {
                event.setCanceled(true);
            }
        }
    }

    /**
     * 左クリック時の共通処理
     * @return コマンドを実行した場合は true、設定がなく何もしなかった場合は false
     */
    private static boolean handleLeftClick(ServerPlayer serverPlayer, ItemStack stack) {
        if (stack.getItem() instanceof CommandRodItem) {
            ClickCommandData data = stack.get(CommandRod.CLICK_COMMAND.get());

            if (data != null && data.left().isPresent()) {
                CommandRodItem.executeCommand(serverPlayer.level(), serverPlayer, data.left().get());
                return true;
            }
        }

        return false;
    }
}
