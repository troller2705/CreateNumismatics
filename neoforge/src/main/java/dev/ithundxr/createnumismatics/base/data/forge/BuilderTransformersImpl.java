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

package dev.ithundxr.createnumismatics.base.data.forge;

import com.simibubi.create.Create;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.ithundxr.createnumismatics.Numismatics;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class BuilderTransformersImpl {
    public static <I extends BlockItem, P> NonNullUnaryOperator<ItemBuilder<I, P>> vendorItem(boolean creative) {
        return a -> a
            .model((c, p) -> p.withExistingParent(
                c.getName(),
                Numismatics.asResource("block/"+(creative?"creative_":"")+"display_case")
            ));
    }

}
