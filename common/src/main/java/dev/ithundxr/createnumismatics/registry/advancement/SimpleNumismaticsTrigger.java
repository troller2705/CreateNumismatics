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

package dev.ithundxr.createnumismatics.registry.advancement;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.advancement.CriterionTriggerBase;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SimpleNumismaticsTrigger extends CriterionTriggerBase<SimpleNumismaticsTrigger.Instance>
{

	public SimpleNumismaticsTrigger(String id) {
		super(id);
	}


	public void trigger(ServerPlayer player) {
		super.trigger(player, null);
	}


	@Override
	public Codec<Instance> codec()
	{
		return null;
	}

	public static class Instance extends CriterionTriggerBase.Instance {

		@Override
		protected boolean test(@Nullable List<Supplier<Object>> suppliers) {
			return true;
		}

		@Override
		public Optional<ContextAwarePredicate> player()
		{
			return Optional.empty();
		}
	}
}
