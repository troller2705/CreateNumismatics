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

import com.mojang.brigadier.CommandDispatcher;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;

import com.tterrag.registrate.providers.ProviderType;

import dev.ithundxr.createnumismatics.base.data.NumismaticsTagGen;
import dev.ithundxr.createnumismatics.base.data.emi.EmiExcludedTagGen;
import dev.ithundxr.createnumismatics.multiloader.Loader;
import dev.ithundxr.createnumismatics.registry.NumismaticsAdvancements;
import dev.ithundxr.createnumismatics.registry.NumismaticsCreativeModeTabs.Tabs;
import dev.ithundxr.createnumismatics.registry.NumismaticsPackets;
import dev.ithundxr.createnumismatics.util.MethodVarHandleUtils;
import dev.ithundxr.createnumismatics.util.Utils;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class Numismatics {
    public static final String MOD_ID = "numismatics";
    public static final String NAME = "Create: Numismatics";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);


    public static void init() {
        String createVersion = MethodVarHandleUtils.getStaticField(Create.class, "VERSION", String.class, "UNKNOWN");
        LOGGER.info("{} initializing!", NAME);
        
        ModSetup.register();
        finalizeRegistrate();

        NumismaticsPackets.PACKETS.registerC2SListener();

        if (Utils.isDevEnv() && Loader.FABRIC.isCurrent()) {
            SharedConstants.IS_RUNNING_IN_IDE = false; // enable this to test commands
        }

        //if (Utils.isDevEnv() && !Mods.SODIUM.isLoaded) // force all mixins to load in dev - this breaks model loading, only use sporadically to test
        //    MixinEnvironment.getCurrentEnvironment().audit();
    }

    public static void postRegistrationInit() {
        ModSetupLate.registerPostRegistration();
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static void finalizeRegistrate() {
        throw new AssertionError();
    }

    public static void gatherData(DataGenerator.PackGenerator gen) {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, NumismaticsTagGen::generateBlockTags);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, NumismaticsTagGen::generateItemTags);
        gen.addProvider(NumismaticsAdvancements::new);
        gen.addProvider(EmiExcludedTagGen::new);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void crashDev(String message) {
        if (Utils.isDevEnv()) {
            throw new RuntimeException(message);
        } else {
            LOGGER.error(message);
        }
    }

    public static void registerCommands(BiConsumer<CommandDispatcher<CommandSourceStack>, Boolean> consumer) {
        throw new AssertionError();
    }
}
