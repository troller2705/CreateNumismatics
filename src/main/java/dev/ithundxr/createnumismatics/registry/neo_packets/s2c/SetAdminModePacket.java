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
import dev.ithundxr.createnumismatics.mixin_interfaces.IAdminModePlayer;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SetAdminModePacket implements ClientboundPacketPayload {

    private final boolean adminMode;

    public static final StreamCodec<RegistryFriendlyByteBuf, SetAdminModePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, packet -> packet.adminMode,
            SetAdminModePacket::new
    );

    public SetAdminModePacket(boolean adminMode) {
        this.adminMode = adminMode;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        if(player instanceof IAdminModePlayer adminModePlayer){
            adminModePlayer.numismatics$setAdminMode(adminMode);
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.SET_ADMIN_MODE; }
}
