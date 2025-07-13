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
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.ithundxr.createnumismatics.AllPackets;
import dev.ithundxr.createnumismatics.content.backend.Coin;
import dev.ithundxr.createnumismatics.content.backend.behaviours.SliderStylePriceBehaviour;
import dev.ithundxr.createnumismatics.registry.neo_packets.NumismaticsCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SliderStylePriceConfigurationPacket extends BlockEntityBehaviourConfigurationPacket<SliderStylePriceBehaviour> {

    private final int[] prices;

    public static final StreamCodec<FriendlyByteBuf, SliderStylePriceConfigurationPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            NumismaticsCodecs.INT_ARRAY, packet -> packet.prices,
            SliderStylePriceConfigurationPacket::new
    );

    private SliderStylePriceConfigurationPacket(BlockPos pos, int[] prices) {
        super(pos);
        this.prices = prices;
    }

    public SliderStylePriceConfigurationPacket(SyncedBlockEntity be) {
        super(be.getBlockPos());
        this.prices = new int[Coin.values().length];
        SliderStylePriceBehaviour priceBehaviour = BlockEntityBehaviour.get(be, getType());
        priceBehaviour.enableClientRead();
        for (Coin coin : Coin.values()){
            this.prices[coin.ordinal()] = priceBehaviour.getPrice(coin);
        }
    }


    @Override
    protected void applySettings(SliderStylePriceBehaviour behaviour) {
        for (Coin coin : Coin.values()) {
            behaviour.setPrice(coin, prices[coin.ordinal()]);
        }
    }



    @Override
    protected BehaviourType<SliderStylePriceBehaviour> getType() { return SliderStylePriceBehaviour.TYPE; }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.SLIDER_STYLE_PRICE_CONFIGURATION; }
}
