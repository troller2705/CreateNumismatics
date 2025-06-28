/*
 * Numismatics
 * Copyright (c) 2023-2024 The Railways Team
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

package dev.ithundxr.createnumismatics.registry.packets;

import net.minecraft.network.FriendlyByteBuf;

public class VendorConfigurationPacket {
    private boolean enableAutomatedExtraction;

    protected void writeSettings(FriendlyByteBuf buffer) {
        buffer.writeBoolean(enableAutomatedExtraction);
    }

    protected void readSettings(FriendlyByteBuf buf) {
        enableAutomatedExtraction = buf.readBoolean();
    }
}
