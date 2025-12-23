package vulpisgiardina.commandrod;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod(CommandRod.MODID)
public class CommandRod {
    public static final String MODID = "commandrod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final Supplier<DataComponentType<ClickCommandData>> CLICK_COMMAND = DATA_COMPONENTS.registerComponentType(
            "command",
            builder -> builder
                    .persistent(ClickCommandData.CODEC)
                    .networkSynchronized(ClickCommandData.STREAM_CODEC)
                    .cacheEncoding());

    public static final DeferredItem<CommandRodItem> COMMAND_ROD = ITEMS.register(
            "command_rod",
            registryName -> new CommandRodItem(
                    new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                            .stacksTo(1)
                            .component(CLICK_COMMAND.get(), ClickCommandData.NULL))
            );

    public CommandRod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        DATA_COMPONENTS.register(modEventBus);
        ITEMS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerPacket);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(COMMAND_ROD);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    public void registerPacket(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MODID);
        registrar.playToServer(
                LeftClickPayload.TYPE,
                LeftClickPayload.STREAM_CODEC,
                LeftClickPayload::handle
        );
    }
}
