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

package dev.ithundxr.createnumismatics.registry.neo_packets;

import dev.ithundxr.createnumismatics.content.backend.Coin;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class NumismaticsCodecs {
    public static final StreamCodec<FriendlyByteBuf, int[]> INT_ARRAY = new StreamCodec<>() {
        @Override
        public int[] decode(FriendlyByteBuf buf) {
            int length = buf.readVarInt();
            int[] result = new int[length];
            for (int i = 0; i < length; i++) {
                result[i] = buf.readVarInt();
            }
            return result;
        }

        @Override
        public void encode(FriendlyByteBuf buf, int[] value) {
            buf.writeVarInt(value.length);
            for (int i : value) {
                buf.writeVarInt(i);
            }
        }
    };

    public static  final  StreamCodec<FriendlyByteBuf, Coin> COIN = new StreamCodec<>() {
        @Override
        public Coin decode(FriendlyByteBuf buf) {
            var coinName = ByteBufCodecs.STRING_UTF8.decode(buf);
            return Coin.getCoinFromName(coinName);
        }

        @Override
        public void encode(FriendlyByteBuf buf, Coin coin) {
            ByteBufCodecs.STRING_UTF8.encode(buf, coin.getName());
        }
    };
}
