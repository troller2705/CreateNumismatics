package dev.ithundxr.createnumismatics;

import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.ithundxr.createnumismatics.registry.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Numismatics.MODID)
public class Numismatics {
    public static final String MODID = "numismatics";
    @Deprecated
    public static final String MOD_ID = MODID;
    public static final String NAME = "Create: Numismatics";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public Numismatics(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("{} initializing!", NAME);

        REGISTRATE.registerEventListeners(modEventBus);

        NumismaticsCreativeModeTabs.register();
        NumismaticsItems.register();
        NumismaticsBlockEntities.register();
        NumismaticsBlocks.register();
        NumismaticsMenuTypes.register();
        NumismaticsTags.register();

        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {



    }
}
