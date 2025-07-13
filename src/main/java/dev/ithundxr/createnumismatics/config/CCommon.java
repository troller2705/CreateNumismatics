/*
 * Numismatics
 * Copyright (c) 2024 The Railways Team
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

package dev.ithundxr.createnumismatics.config;

import dev.ithundxr.createnumismatics.content.backend.Coin;
import net.createmod.catnip.config.ConfigBase;


@SuppressWarnings("unused")
public class CCommon extends ConfigBase {

    public final net.createmod.catnip.config.ConfigBase.ConfigGroup coins = group(0, "coins", Comments.coins);

    public final ConfigEnum<Coin> referenceCoin = e(Coin.COG, "defaultCoin", Comments.referenceCoin);

    @Override
    public String getName() {
        return "common";
    }

    private static class Comments {
        static final String coins = "Coin settings";

        static final String referenceCoin = "The reference coin to be used in UI related displays";
    }
}

