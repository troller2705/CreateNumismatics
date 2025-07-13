/*
 * Numismatics
 * Copyright (c) 2025 The Railways Team
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

package dev.ithundxr.createnumismatics.registry.neo_packets.s2c;

import dev.ithundxr.createnumismatics.AllPackets;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class BigStackSizeContainerSetContentPacket implements ClientboundPacketPayload {

    private final int containerId;
    private final int stateId;
    private final List<ItemStack> items;
    private final ItemStack carriedItem;

    public static final StreamCodec<RegistryFriendlyByteBuf, BigStackSizeContainerSetContentPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, packet -> packet.containerId,
            ByteBufCodecs.VAR_INT, packet -> packet.stateId,
            ItemStack.LIST_STREAM_CODEC, packet -> packet.items,
            ItemStack.STREAM_CODEC, packet -> packet.carriedItem,
            BigStackSizeContainerSetContentPacket::new
    );

    public BigStackSizeContainerSetContentPacket(int containerId, int stateId, List<ItemStack> items, ItemStack carriedItem) {
        this.containerId = containerId;
        this.stateId = stateId;
//        this.items = NonNullList.withSize(items.size(), ItemStack.EMPTY);
        this.items = items;
        this.carriedItem = carriedItem;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        // IntelliJ falsely thinks that player.containerMenu is never null
        //noinspection ConstantValue,DataFlowIssue
        if (player.containerMenu != null && player.containerMenu.containerId == containerId) {
            player.containerMenu.initializeContents(stateId, items, carriedItem);
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.BIG_STACK_SIZE_CONTAINER_SET_CONTENT; }
}
