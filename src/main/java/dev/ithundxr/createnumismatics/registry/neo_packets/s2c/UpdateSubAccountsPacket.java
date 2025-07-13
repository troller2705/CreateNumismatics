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
import dev.ithundxr.createnumismatics.content.backend.BankAccount;
import dev.ithundxr.createnumismatics.content.bank.SubAccountListScreen;
import io.netty.buffer.Unpooled;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.UUID;

public class UpdateSubAccountsPacket implements ClientboundPacketPayload {

    private final UUID accountID;
    private final byte[] data;

    public static final StreamCodec<FriendlyByteBuf, UpdateSubAccountsPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, packet -> packet.accountID,
            ByteBufCodecs.BYTE_ARRAY, packet -> packet.data,
            UpdateSubAccountsPacket::new
    );

    private UpdateSubAccountsPacket(UUID accountID, byte[] data) {
        this.accountID = accountID;
        this.data = data;
    }

    public UpdateSubAccountsPacket(BankAccount account){
        this.accountID = account.id;
        var buf = new FriendlyByteBuf(Unpooled.buffer());

        account.sendSubAccountsOnlyToMenu(buf);

        byte[] data = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), data);

        this.data = data;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        if(Minecraft.getInstance().screen instanceof SubAccountListScreen sal){
            BankAccount account = sal.getMenu().contentHolder;
            if(account.id.equals(accountID)){
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
                account.updateSubAccountsFrom(buf);
            }
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.UPDATE_SUB_ACCOUNTS; }
}
