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

package dev.ithundxr.createnumismatics.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ArmorDyeRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ClientCraftingUtils {
    /**
     * Apply stacking crafts to an item stack.
     * @param targetStack The item stack to apply the stacking crafts to.
     * @param droppedStack The item stack to apply the stacking crafts from.
     * @return The item stack with the stacking crafts applied, or null if it couldn't be applied.
     *         Note: The stack may be identical, but it will never be the same instance.
     */
    public static @NotNull Result applyStackingCrafts(@NotNull ItemStack targetStack, @NotNull ItemStack droppedStack) {
        Result dyeResult = applyDye(targetStack, droppedStack);
        if (dyeResult.isOk())
            return dyeResult;
        return applyEnchant(targetStack, droppedStack);
    }

    /**
     * Apply an enchantment from an enchanted book to an item stack.
     * @param targetStack The item stack to apply the enchantment to.
     * @param enchantedBook The enchanted book to apply the enchantment from.
     * @return The item stack with the enchantment applied, or null if it couldn't be applied.
     *         Note: The stack may be identical, but it will never be the same instance.
     */
    public static @NotNull Result applyEnchant(@NotNull ItemStack targetStack, @NotNull ItemStack enchantedBook) {
        if (targetStack.isEmpty() || enchantedBook.isEmpty())
            return targetStack.isEmpty() ? Result.failureReplace(enchantedBook.copy()) : Result.failureKeep();

        if (!enchantedBook.is(Items.ENCHANTED_BOOK))
            return Result.failureReplace(enchantedBook.copy());


        targetStack = targetStack.copy();

        boolean someEnchantsSucceeded = false;
        boolean someEnchantsFailed = false;


        if (someEnchantsFailed && !someEnchantsSucceeded)
            return Result.failureKeep();

        return Result.ok(targetStack);
    }

    /**
     * Apply a dye to an item stack.
     * @param targetStack The item stack to apply the dye to (leather armor etc.).
     * @param dye The dye to apply.
     * @return The item stack with the dye applied, or null if it couldn't be applied.
     */
    public static @NotNull Result applyDye(@NotNull ItemStack targetStack, @NotNull ItemStack dye) {
        targetStack = targetStack.copy();
        ItemStack dye$ = dye.copy();

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return Result.failureKeep();

        CraftingContainer craftingContainer = new SimpleCraftingContainer(2, 1);
        craftingContainer.setItem(0, targetStack);
        craftingContainer.setItem(1, dye$);
        return null;
    }

    private static class SimpleCraftingContainer extends SimpleContainer implements CraftingContainer {

        protected int width;
        protected int height;

        public SimpleCraftingContainer(int width, int height) {
            super(width * height);

            this.width = width;
            this.height = height;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

    }

    public static class Result {
        private final @Nullable ItemStack stack;
        private final @NotNull ResultType type;

        private static final Result FAILURE_KEEP = new Result(null, ResultType.FAILURE_KEEP);

        private Result(@Nullable ItemStack stack, @NotNull ResultType type) {
            this.stack = stack;
            this.type = type;
        }

        public static Result ok(@NotNull ItemStack stack) {
            return new Result(stack, ResultType.SUCCESS);
        }

        public static Result failureReplace(@NotNull ItemStack new_) {
            return new Result(new_, ResultType.FAILURE_REPLACE);
        }

        public static Result failureKeep() {
            return FAILURE_KEEP;
        }

        public boolean isOk() {
            return type == ResultType.SUCCESS;
        }

        public @NotNull ItemStack getResult(@NotNull ItemStack existing, boolean inheritCount) {
            return switch (type) {
                case SUCCESS -> stack == null
                    ? existing :
                    (inheritCount
                        ? stack.copyWithCount(existing.getCount())
                        : stack
                    );
                case FAILURE_REPLACE -> stack == null
                    ? existing
                    : stack;
                case FAILURE_KEEP -> existing;
            };
        }
    }

    public enum ResultType {
        SUCCESS,
        FAILURE_REPLACE,
        FAILURE_KEEP
    }
}
