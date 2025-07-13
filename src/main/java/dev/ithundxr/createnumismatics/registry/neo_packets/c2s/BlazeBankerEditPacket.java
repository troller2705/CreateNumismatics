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

package dev.ithundxr.createnumismatics.registry.neo_packets.c2s;

import dev.ithundxr.createnumismatics.AllPackets;
import dev.ithundxr.createnumismatics.content.bank.blaze_banker.BlazeBankerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.Nullable;

public class BlazeBankerEditPacket extends BlockEntityConfigurationPacket<BlazeBankerBlockEntity> {

    @Nullable
    private final String label;

    public static final StreamCodec<FriendlyByteBuf, BlazeBankerEditPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            ByteBufCodecs.STRING_UTF8, packet -> packet.label,
            BlazeBankerEditPacket::new
    );

    public BlazeBankerEditPacket(BlockPos pos, String label) {
        super(pos);
        this.label = label;
    }

    @Override
    protected void applySettings(BlazeBankerBlockEntity blazeBankerBlockEntity) {
        if(label != null)
            blazeBankerBlockEntity.setLabel(label);
    }


    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.BLAZE_BANKER_EDIT; }
}
