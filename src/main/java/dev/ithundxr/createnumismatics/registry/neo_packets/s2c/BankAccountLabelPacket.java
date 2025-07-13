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
import dev.ithundxr.createnumismatics.NumismaticsClient;
import dev.ithundxr.createnumismatics.content.backend.BankAccount;
import dev.ithundxr.createnumismatics.content.backend.sub_authorization.SubAccount;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import java.util.UUID;

public class BankAccountLabelPacket implements ClientboundPacketPayload {

    private final boolean isSubAccount;
    private final UUID id;
    private final String label;

    public static final StreamCodec<ByteBuf, BankAccountLabelPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, packet -> packet.isSubAccount,
            UUIDUtil.STREAM_CODEC, packet -> packet.id,
            ByteBufCodecs.STRING_UTF8, packet -> packet.label,
            BankAccountLabelPacket::new
    );


    private BankAccountLabelPacket(boolean isSubAccount, UUID id, String label) {
        this.isSubAccount = isSubAccount;
        this.id = id;
        this.label = label;
    }

    public BankAccountLabelPacket(BankAccount account){
        this(false, account.id, account.getLabel());
    }

    public BankAccountLabelPacket(SubAccount account){
        this(true, account.getAuthorizationID(), account.getLabel());
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        var labelMap = isSubAccount ? NumismaticsClient.subAccountLabels : NumismaticsClient.bankAccountLabels;
        if(label == null){
            labelMap.remove(id);
        }else{
            labelMap.put(id, label);
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() { return AllPackets.BANK_ACCOUNT_LABEL; }
}
