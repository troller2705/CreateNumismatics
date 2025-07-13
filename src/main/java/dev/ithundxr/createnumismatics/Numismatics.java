/*
 * Numismatics
 * Copyright (c) 2023-2024 The Railways Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.ithundxr.createnumismatics;

import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.ithundxr.createnumismatics.config.NumismaticsConfig;
import dev.ithundxr.createnumismatics.content.backend.BankAccount;
import dev.ithundxr.createnumismatics.content.backend.GlobalBankManager;
import dev.ithundxr.createnumismatics.registry.*;
import dev.ithundxr.createnumismatics.registry.neo_packets.s2c.BankAccountLabelPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Numismatics {
    public static final String MODID = "numismatics";
    @Deprecated
    public static final String MOD_ID = MODID;
    public static final String NAME = "Create: Numismatics";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final GlobalBankManager BANK = new GlobalBankManager();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE
                .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public Numismatics(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("{} initializing!", NAME);

        REGISTRATE.registerEventListeners(modEventBus);

        AllPackets.register();
        NumismaticsConfig.register(modContainer);
        NumismaticsCreativeModeTabs.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        ModSetup.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

//        registerCommands(NumismaticsCommands::register);
//        NumismaticsPackets.PACKETS.registerC2SListener();
    }

//    public static void gatherData(DataGenerator.PackGenerator gen) {
//        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, NumismaticsTagGen::generateBlockTags);
//        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, NumismaticsTagGen::generateItemTags);
//        REGISTRATE.addDataGenerator(ProviderType.LANG, NumismaticsLangGen::generate);
//        PonderLocalization.provideRegistrateLang(REGISTRATE);
//        gen.addProvider(NumismaticsSequencedAssemblyRecipeGen::new);
//        gen.addProvider(NumismaticsStandardRecipeGen::new);
//        gen.addProvider(NumismaticsAdvancements::new);
//        gen.addProvider(EmiExcludedTagGen::new);
//    }
}
