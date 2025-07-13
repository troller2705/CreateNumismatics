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
import dev.ithundxr.createnumismatics.NumismaticsClient;
import dev.ithundxr.createnumismatics.content.backend.BankAccount;
import dev.ithundxr.createnumismatics.content.backend.sub_authorization.SubAccount;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.UUID;

public class BigStackSizeContainerSetSlotPacket implements ClientboundPacketPayload {

    private final int containerId;
    private final int stateId;
    private final int slot;
    private final ItemStack itemStack;

    public static final StreamCodec<RegistryFriendlyByteBuf, BigStackSizeContainerSetSlotPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, packet -> packet.containerId,
            ByteBufCodecs.VAR_INT, packet -> packet.stateId,
            ByteBufCodecs.VAR_INT, packet -> packet.slot,
            ItemStack.STREAM_CODEC, packet -> packet.itemStack,
            BigStackSizeContainerSetSlotPacket::new
    );

    public BigStackSizeContainerSetSlotPacket(int containerId, int stateId, int slot, ItemStack itemStack) {
        this.containerId = containerId;
        this.stateId = stateId;
        this.slot = slot;
        this.itemStack = itemStack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        // IntelliJ falsely thinks that player.containerMenu is never null
        //noinspection ConstantValue,DataFlowIssue
        if (player.containerMenu != null && player.containerMenu.containerId == containerId) {
            player.containerMenu.setItem(slot, stateId, itemStack);
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.BIG_STACK_SIZE_CONTAINER_SET_SLOT; }
}
