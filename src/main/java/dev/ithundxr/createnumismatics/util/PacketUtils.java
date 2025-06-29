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

package dev.ithundxr.createnumismatics.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PacketUtils {
    /**
     * Reads an item without the 127 count limit
     */
    public static ItemStack readBigStackSizeItem(FriendlyByteBuf buffer) {
        if (!buffer.readBoolean()) {
            return ItemStack.EMPTY;
        }
        return null;
    }

    /**
     * Writes an item without the 127 count limit
     */
    public static void writeBigStackSizeItem(FriendlyByteBuf buffer, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            buffer.writeBoolean(false);
        }
    }
}
