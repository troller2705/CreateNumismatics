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

package dev.ithundxr.createnumismatics.registry.packets;

import dev.ithundxr.createnumismatics.multiloader.S2CPacket;
import dev.ithundxr.createnumismatics.util.PacketUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BigStackSizeContainerSetContentPacket implements S2CPacket {
    private final int containerId;
    private final int stateId;
    private final List<ItemStack> items;

    public BigStackSizeContainerSetContentPacket(int containerId, int stateId, List<ItemStack> items, ItemStack carriedItem) {
        this.containerId = containerId;
        this.stateId = stateId;
        this.items = NonNullList.withSize(items.size(), ItemStack.EMPTY);

        for(int i = 0; i < items.size(); ++i) {
            this.items.set(i, items.get(i).copy());
        }
    }

    public BigStackSizeContainerSetContentPacket(FriendlyByteBuf buffer) {
        containerId = buffer.readUnsignedByte();
        stateId = buffer.readVarInt();
        items = buffer.readCollection(NonNullList::createWithCapacity, PacketUtils::readBigStackSizeItem);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeByte(containerId);
        buffer.writeVarInt(stateId);
        buffer.writeCollection(items, PacketUtils::writeBigStackSizeItem);
    }

    @Override
    public void handle(Minecraft mc) {
        Player player = mc.player;
        // IntelliJ falsely thinks that player.containerMenu is never null
        //noinspection ConstantValue,DataFlowIssue
    }
}
