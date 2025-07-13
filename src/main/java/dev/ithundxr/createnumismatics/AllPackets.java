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

package dev.ithundxr.createnumismatics;

import dev.ithundxr.createnumismatics.registry.neo_packets.c2s.*;
import dev.ithundxr.createnumismatics.registry.neo_packets.s2c.*;
import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Locale;

public enum AllPackets  implements BasePacketPayload.PacketTypeProvider {
    // Client to Server
    SLIDER_STYLE_PRICE_CONFIGURATION(SliderStylePriceConfigurationPacket.class, SliderStylePriceConfigurationPacket.STREAM_CODEC),
    BLAZE_BANKER_EDIT(BlazeBankerEditPacket.class, BlazeBankerEditPacket.STREAM_CODEC),
    ANDESITE_DEPOSITOR_CONFIGURATION(AndesiteDepositorConfigurationPacket.class, AndesiteDepositorConfigurationPacket.STREAM_CODEC),
    OPEN_TRUST_LIST(OpenTrustListPacket.class, OpenTrustListPacket.STREAM_CODEC),

    // Server to Client
    BANK_ACCOUNT_LABEL(BankAccountLabelPacket.class, BankAccountLabelPacket.STREAM_CODEC),
    VAR_INT_CONTAINER_SET_DATA(VarIntContainerSetDataPacket.class, VarIntContainerSetDataPacket.STREAM_CODEC),
    BIG_STACK_SIZE_CONTAINER_SET_SLOT(BigStackSizeContainerSetSlotPacket.class, BigStackSizeContainerSetSlotPacket.STREAM_CODEC),
    BIG_STACK_SIZE_CONTAINER_SET_CONTENT(BigStackSizeContainerSetContentPacket.class, BigStackSizeContainerSetContentPacket.STREAM_CODEC),
    UPDATE_SUB_ACCOUNTS(UpdateSubAccountsPacket.class, UpdateSubAccountsPacket.STREAM_CODEC),
    SALEPOINT_CARD(SalepointCardPacket.class, SalepointCardPacket.STREAM_CODEC),
    SET_ADMIN_MODE(SetAdminModePacket.class, SetAdminModePacket.STREAM_CODEC),
    ;

    private final CatnipPacketRegistry.PacketType<?> type;

    <T extends BasePacketPayload> AllPackets(Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        String name = this.name().toLowerCase(Locale.ROOT);
        this.type = new CatnipPacketRegistry.PacketType<>(
                new CustomPacketPayload.Type<>(Numismatics.asResource(name)),
                clazz, codec
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
        return (CustomPacketPayload.Type<T>) this.type.type();
    }

    public static void register() {
        CatnipPacketRegistry packetRegistry = new CatnipPacketRegistry(Numismatics.MODID, 1);
        for (AllPackets packet : AllPackets.values()) {
            packetRegistry.registerPacket(packet.type);
        }
        packetRegistry.registerAllPackets();
    }
}
