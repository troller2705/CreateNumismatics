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
import dev.ithundxr.createnumismatics.content.backend.Coin;
import dev.ithundxr.createnumismatics.content.depositor.AndesiteDepositorBlockEntity;
import dev.ithundxr.createnumismatics.registry.neo_packets.NumismaticsCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class AndesiteDepositorConfigurationPacket extends BlockEntityConfigurationPacket<AndesiteDepositorBlockEntity> {

    private final Coin coin;

    public static final StreamCodec<FriendlyByteBuf, AndesiteDepositorConfigurationPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            NumismaticsCodecs.COIN, packet -> packet.coin,
            AndesiteDepositorConfigurationPacket::new
    );

    public AndesiteDepositorConfigurationPacket(BlockPos pos, Coin coin) {
        super(pos);
        this.coin = coin;
    }

    @Override
    protected void applySettings(AndesiteDepositorBlockEntity andesiteDepositorBlockEntity) {
        andesiteDepositorBlockEntity.setCoin(coin);
    }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.ANDESITE_DEPOSITOR_CONFIGURATION; }
}
