package vulpisgiardina.commandrod;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CommandRodItem extends Item {
    public CommandRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // サーバー側でのみ実行
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            ClickCommandData data = stack.get(CommandRod.CLICK_COMMAND.get());
            if (data != null && data.right().isPresent()) {
                executeCommand(serverLevel, serverPlayer, data.right().get());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level level, BlockPos pos, LivingEntity entity) {
        ClickCommandData data = stack.get(CommandRod.CLICK_COMMAND.get());
        if (data != null && data.left().isPresent()) {
            if (!level.isClientSide() && entity instanceof ServerPlayer player) {
                executeCommand((ServerLevel) level, player, data.left().get());
            }
            return false;
        }
        return super.canDestroyBlock(stack, state, level, pos, entity);
    }

    public static void executeCommand(ServerLevel level, ServerPlayer player, String command) {
        if (command == null || command.isBlank()) return;

        MinecraftServer server = level.getServer();

        CommandSourceStack source = player.createCommandSourceStack().withPermission(Math.max(player.getPermissionLevel(), 2));

        try {
            server.getCommands().performPrefixedCommand(source, command);
        } catch (Exception e) {
            CommandRod.LOGGER.info("Failed to execute command: {}", command);
        }
    }
}
