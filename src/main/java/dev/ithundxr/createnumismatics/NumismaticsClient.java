package dev.ithundxr.createnumismatics;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Numismatics.MODID, dist = Dist.CLIENT)
public class NumismaticsClient {

    public static final Map<UUID, String> bankAccountLabels = new HashMap<>();
    public static final Map<UUID, String> subAccountLabels = new HashMap<>();

    public NumismaticsClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}