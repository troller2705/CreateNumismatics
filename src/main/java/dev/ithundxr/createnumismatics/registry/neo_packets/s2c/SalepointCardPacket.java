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
import dev.ithundxr.createnumismatics.content.salepoint.SalepointPurchaseScreen;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Optional;

public class SalepointCardPacket implements ClientboundPacketPayload {

    private final Optional<Component> message;
    private final int maxWithdrawal;
    private final Optional<Component> stateMessage;

    public static final StreamCodec<RegistryFriendlyByteBuf, SalepointCardPacket> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.OPTIONAL_STREAM_CODEC, packet -> packet.message,
            ByteBufCodecs.VAR_INT, packet -> packet.maxWithdrawal,
            ComponentSerialization.OPTIONAL_STREAM_CODEC, packet -> packet.stateMessage,
            SalepointCardPacket::new
    );

    public SalepointCardPacket(Optional<Component> message, int maxWithdrawal, Optional<Component> stateMessage) {
        this.message = message;
        this.maxWithdrawal = maxWithdrawal;
        this.stateMessage = stateMessage;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        if(Minecraft.getInstance().screen instanceof SalepointPurchaseScreen salepointPurchaseScreen){
            salepointPurchaseScreen.getMenu().serverSentCardMessage = message.orElse(null);
            salepointPurchaseScreen.getMenu().serverSentMaxWithdrawal = maxWithdrawal;
            salepointPurchaseScreen.getMenu().serverSentStateMessage = stateMessage.orElse(null);
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.SALEPOINT_CARD; }
}
