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

package dev.ithundxr.createnumismatics.multiloader.fluid;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("removal")
public class MultiloaderFluidStack {

    public static Codec<MultiloaderFluidStack> makeCodec() {
        return FluidStack.CODEC.xmap(MultiloaderFluidStack::new, fs -> ((MultiloaderFluidStack) fs).wrapped);
    }

    private final FluidStack wrapped;

    public static MultiloaderFluidStack makeEmpty() {
        return new MultiloaderFluidStack(FluidStack.EMPTY);
    }

    public MultiloaderFluidStack(FluidStack wrapped) {
        this.wrapped = wrapped;
    }

    public MultiloaderFluidStack(Fluid fluid, int amount) {
        this(new FluidStack(fluid, amount));
    }

    public MultiloaderFluidStack(Fluid fluid, int amount, CompoundTag nbt) {
        this(new FluidStack(fluid, amount, nbt));
    }

    public MultiloaderFluidStack(FluidStack stack, int amount) {
        this(new FluidStack(stack, amount));
    }

    public MultiloaderFluidStack(MultiloaderFluidStack stack, int amount) {
        this(((MultiloaderFluidStack) stack).wrapped, amount);
    }

    public static final Codec<MultiloaderFluidStack> CODEC = makeCodec();

    public static final MultiloaderFluidStack EMPTY = makeEmpty();

    public static MultiloaderFluidStack create(Fluid fluid, long amount) {
        return create(fluid, amount, null);
    }

    public static MultiloaderFluidStack create(Fluid fluid, long amount, @Nullable CompoundTag nbt) {
        return new MultiloaderFluidStack(fluid, (int) amount, nbt);
    }

    public MultiloaderFluidStack setAmount(long amount){
        wrapped.setAmount((int) amount);
        return this;
    }

    public void grow(long amount) {
        setAmount(getAmount() + amount);
    }

    public Fluid getFluid() {
        return wrapped.getFluid();
    }

    public long getAmount() {
        return wrapped.getAmount();
    }

    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    public void shrink(int amount) {
        setAmount(getAmount() - amount);
    }

    public void shrink(long amount) {
        setAmount(getAmount() - amount);
    }

    /**
     * Determines if the FluidIDs and NBT Tags are equal. This does not check amounts.
     *
     * @param other
     *            The FluidStack for comparison
     * @return true if the Fluids (IDs and NBT Tags) are the same
     */
    public boolean isFluidEqual(MultiloaderFluidStack other) {
        return wrapped.isFluidEqual(((MultiloaderFluidStack) other).wrapped);
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        return wrapped.writeToNBT(nbt);
    }

    public static MultiloaderFluidStack loadFluidStackFromNBT(CompoundTag tag) {
        return new MultiloaderFluidStack(FluidStack.loadFluidStackFromNBT(tag));
    }

    public void setTag(CompoundTag tag) {
        wrapped.setTag(tag);
    }

    public @Nullable CompoundTag getTag() {
        return wrapped.getTag();
    }

    public CompoundTag getOrCreateTag() {
        if (getTag() == null) setTag(new CompoundTag());
        return getTag();
    }

    public void removeChildTag(String key) {
        if (getTag() == null) return;
        getTag().remove(key);
    }

    public Component getDisplayName() {
        return wrapped.getDisplayName();
    }

    public boolean hasTag() {
        return getTag() != null;
    }

    public static MultiloaderFluidStack readFromPacket(FriendlyByteBuf buffer) {
        return new MultiloaderFluidStack(FluidStack.readFromPacket(buffer));
    }

    public FriendlyByteBuf writeToPacket(FriendlyByteBuf buffer) {
        wrapped.writeToPacket(buffer);
        return buffer;
    }

    public MultiloaderFluidStack copy() {
        return new MultiloaderFluidStack(wrapped.copy());
    }

    private boolean isFluidStackTagEqual(MultiloaderFluidStack other) {
        CompoundTag tag = getTag();
        CompoundTag other$tag = other.getTag();
        return tag == null ? other$tag == null : other$tag != null && tag.equals(other$tag);
    }

    /**
     * Determines if the NBT Tags are equal. Useful if the FluidIDs are known to be equal.
     */
    public static boolean areFluidStackTagsEqual(@NotNull MultiloaderFluidStack stack1, @NotNull MultiloaderFluidStack stack2) {
        return stack1.isFluidStackTagEqual(stack2);
    }

    /**
     * Determines if the Fluids are equal and this stack is larger.
     *
     * @return true if this FluidStack contains the other FluidStack (same fluid and >= amount)
     */
    public boolean containsFluid(@NotNull MultiloaderFluidStack other) {
        return wrapped.containsFluid(((MultiloaderFluidStack) other).wrapped);
    }

    /**
     * Determines if the FluidIDs, Amounts, and NBT Tags are all equal.
     *
     * @param other
     *            - the FluidStack for comparison
     * @return true if the two FluidStacks are exactly the same
     */
    public boolean isFluidStackIdentical(MultiloaderFluidStack other) {
        return wrapped.isFluidStackIdentical(((MultiloaderFluidStack) other).wrapped);
    }

    /**
     * Determines if the FluidIDs and NBT Tags are equal compared to a registered container
     * ItemStack. This does not check amounts.
     *
     * @param other
     *            The ItemStack for comparison
     * @return true if the Fluids (IDs and NBT Tags) are the same
     */
    public boolean isFluidEqual(@NotNull ItemStack other) {
        return wrapped.isFluidEqual(other);
    }

    public boolean isLighterThanAir() {
        return getFluid().getFluidType().isLighterThanAir();
    }
}
