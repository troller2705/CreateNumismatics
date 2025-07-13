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
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class VarIntContainerSetDataPacket implements ClientboundPacketPayload {

    private final int containerId;
    private final int id;
    private final int value;

    public static final StreamCodec<ByteBuf, VarIntContainerSetDataPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, packet -> packet.containerId,
            ByteBufCodecs.SHORT, packet -> packet.id,
            ByteBufCodecs.VAR_INT, packet -> packet.value,
            VarIntContainerSetDataPacket::new
    );

    public VarIntContainerSetDataPacket(int containerId, int id, int value) {
        this.containerId = containerId;
        this.id = id;
        this.value = value;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        // IntelliJ falsely things that player.containerMenu is never null
        //noinspection ConstantValue,DataFlowIssue
        if (player.containerMenu != null && player.containerMenu.containerId == containerId) {
            player.containerMenu.setData(id, value);
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.VAR_INT_CONTAINER_SET_DATA; }
}
