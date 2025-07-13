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

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import dev.ithundxr.createnumismatics.AllPackets;
import dev.ithundxr.createnumismatics.content.backend.trust_list.TrustListHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class OpenTrustListPacket<BE extends SyncedBlockEntity & TrustListHolder> extends BlockEntityConfigurationPacket<BE> {

    public static final StreamCodec<FriendlyByteBuf, OpenTrustListPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            OpenTrustListPacket::new
    );

    public OpenTrustListPacket(BlockPos pos) {
        super(pos);
    }

    @Override
    protected void applySettings(ServerPlayer player, BE be) {
        be.openTrustListMenu(player);
    }

    @Override
    @SuppressWarnings("EmptyMethod")
    protected void applySettings(BE be) { }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.OPEN_TRUST_LIST; }
}
